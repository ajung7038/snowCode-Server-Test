package snowcode.snowcode.unit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignmentRegistration.domain.AssignmentRegistration;
import snowcode.snowcode.assignmentRegistration.service.RegistrationService;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.course.service.CourseService;
import snowcode.snowcode.student.dto.StudentProgressListResponse;
import snowcode.snowcode.student.dto.StudentProgressResponse;
import snowcode.snowcode.submission.dto.SubmissionScore;
import snowcode.snowcode.submission.service.SubmissionService;
import snowcode.snowcode.unit.domain.Unit;
import snowcode.snowcode.unit.domain.UnitAssignmentStatus;
import snowcode.snowcode.unit.dto.UnitProgressResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnitProgressFacade {

    private final UnitService unitService;
    private final RegistrationService registrationService;
    private final SubmissionService submissionService;
    private final CourseService courseService;

    public StudentProgressListResponse findAllStudents(List<Member> members, Long courseId) {
        Course course = courseService.findCourse(courseId);
        List<Unit> units = unitService.findAllByCourseId(courseId);

        // 1. <유닛 id, 제출 리스트> 매핑
        Map<Long, List<AssignmentRegistration>> regsByUnit = loadRegistrationsByUnits(units);

        // 2. 유닛별 총점 계산 (제출하지 않은 과제까지 모두 고려)
        Map<Long, Integer> unitTotalScore = calculateUnitTotalScores(units, regsByUnit);

        // 3. 현재 제출 점수 (member, registration 별 최고 점수) 조회
        Map<Long, Map<Long, Integer>> bestScoreByMemberAndReg = loadBestScores(members, regsByUnit);

        // 4. 학생별 진행도 계산
        List<StudentProgressResponse> studentResponses = buildStudentProgress(
                members, units, unitTotalScore, regsByUnit, bestScoreByMemberAndReg);

        return StudentProgressListResponse.of(course, units.size(), studentResponses);
    }

    private Map<Long, List<AssignmentRegistration>> loadRegistrationsByUnits(List<Unit> units) {
        List<Long> unitIds = units.stream().map(Unit::getId).toList();
        List<AssignmentRegistration> allRegs = registrationService.findAllByUnitIdIn(unitIds);

        return allRegs.stream()
                .collect(Collectors.groupingBy(ar -> ar.getUnit().getId()));
    }

    private Map<Long, Integer> calculateUnitTotalScores(
            List<Unit> units,
            Map<Long, List<AssignmentRegistration>> regsByUnit) {

        Map<Long, Integer> result = new HashMap<>();
        for (Unit u : units) {
            // 유닛을 꺼내거나, 등록된 단원이 없다면 빈 리스트 값 반환
            int sum = regsByUnit.getOrDefault(u.getId(), List.of())
                    .stream()
                    .mapToInt(ar -> ar.getAssignment().getScore())
                    .sum();
            result.put(u.getId(), sum);
        }
        return result;
    }

    private Map<Long, Map<Long, Integer>> loadBestScores(
            List<Member> members,
            Map<Long, List<AssignmentRegistration>> regsByUnit) {

        List<Long> memberIds = members.stream().map(Member::getId).toList();
        List<Long> regIds = regsByUnit.values().stream()
                .flatMap(List::stream)
                .map(AssignmentRegistration::getId)
                .toList();

        if (memberIds.isEmpty() || regIds.isEmpty()) {
            return Map.of();
        }

        List<SubmissionScore> scores = submissionService.findMaxScoreByMemberIdsAndRegsIds(memberIds, regIds);

        Map<Long, Map<Long, Integer>> result = new HashMap<>();
        for (SubmissionScore ss : scores) {
            result.computeIfAbsent(ss.memberId(), k -> new HashMap<>())
                    .put(ss.registrationId(), ss.maxScore());
        }
        return result;
    }

    private List<StudentProgressResponse> buildStudentProgress(
            List<Member> members,
            List<Unit> units,
            Map<Long, Integer> unitTotalScore,
            Map<Long, List<AssignmentRegistration>> regsByUnit,
            Map<Long, Map<Long, Integer>> bestScore) {

        List<StudentProgressResponse> results = new ArrayList<>();

        int courseTotalScore = unitTotalScore.values().stream().mapToInt(Integer::intValue).sum();

        for (Member m : members) {
            List<UnitProgressResponse> unitProgresses = new ArrayList<>();
            int courseEarnedScore = 0;

            Map<Long, Integer> myScores = bestScore.getOrDefault(m.getId(), Map.of());

            for (Unit u : units) {
                int unitEarned = 0;
                boolean anySubmitted = false;

                for (AssignmentRegistration ar : regsByUnit.getOrDefault(u.getId(), List.of())) {
                    Integer pts = myScores.get(ar.getId());
                    if (pts != null) {
                        unitEarned += pts;
                        anySubmitted = true;
                    }
                }

                String status = decideStatus(unitEarned, unitTotalScore.getOrDefault(u.getId(), 0), anySubmitted);
                unitProgresses.add(new UnitProgressResponse(status));

                courseEarnedScore += unitEarned;
            }

            results.add(StudentProgressResponse.of(m, courseEarnedScore, courseTotalScore, unitProgresses));
        }

        return results;
    }

    private String decideStatus(int unitEarned, int unitTotal, boolean anySubmitted) {
        if (!anySubmitted) return UnitAssignmentStatus.NOT_SUBMITTED.toString();
        if (unitEarned == unitTotal) return UnitAssignmentStatus.PASSED.toString();
        if (unitEarned > 0) return UnitAssignmentStatus.PARTIAL.toString();
        return UnitAssignmentStatus.FAILED.toString();
    }
}
