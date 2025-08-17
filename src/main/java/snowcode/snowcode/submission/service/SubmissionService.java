package snowcode.snowcode.submission.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignmentRegistration.domain.AssignmentRegistration;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.submission.domain.Submission;
import snowcode.snowcode.submission.repository.SubmissionRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    public Submission createSubmission(Member member, AssignmentRegistration assignmentRegistration) {
        Submission submission = Submission.createSubmission(0, member, assignmentRegistration);
        submissionRepository.save(submission);
        return submission;
    }

    @Transactional(readOnly = true)
    public Optional<Submission> isSubmitted(Long memberId, AssignmentRegistration registration) {
        List<Submission> submissions = submissionRepository.findByMemberIdAndAssignmentRegistrationId(memberId, registration.getId());

        return submissions.stream()
                .max(Comparator.comparingInt(Submission::getScore));
    }

    @Transactional(readOnly = true)
    public List<Long> findAllByRegistrationId(Long registrationId) {
        return submissionRepository.findIdsByRegistrationId(registrationId);
    }

    public void deleteAllById(List<Long> submissionIds) {
        submissionRepository.deleteAllById(submissionIds);
    }
}
