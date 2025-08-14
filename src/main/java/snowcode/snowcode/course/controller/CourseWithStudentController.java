package snowcode.snowcode.course.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;
import snowcode.snowcode.course.service.CourseWithEnrollmentFacade;
import snowcode.snowcode.student.dto.StudentRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseWithStudentController {

    private final CourseWithEnrollmentFacade courseWithEnrollmentFacade;

    @PostMapping("/{courseId}/enrollments")
    public BasicResponse<String> addStudent(@PathVariable Long courseId, @Valid @RequestBody StudentRequest dto) {
        courseWithEnrollmentFacade.addStudentWithEnroll(courseId, dto);
        return ResponseUtil.success("학생 추가에 성공하였습니다.");
    }
}
