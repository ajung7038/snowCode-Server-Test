package snowcode.snowcode.course.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;
import snowcode.snowcode.course.dto.CourseRequest;
import snowcode.snowcode.course.dto.CourseResponse;
import snowcode.snowcode.course.service.CourseService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    public BasicResponse<CourseResponse> createCourse(@Valid @RequestBody CourseRequest dto) {
        CourseResponse course = courseService.createCourse(dto);
        return ResponseUtil.success(course);
    }

    @PutMapping("/{id}")
    public BasicResponse<CourseResponse> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseRequest dto) {
        CourseResponse course = courseService.updateCourse(id, dto);
        return ResponseUtil.success(course);
    }

    @DeleteMapping("/{id}")
    public BasicResponse<String> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseUtil.success("강의 삭제에 성공하였습니다.");
    }
}
