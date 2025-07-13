package snowcode.snowcode.unit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.course.service.CourseService;
import snowcode.snowcode.unit.dto.UnitRequest;
import snowcode.snowcode.unit.dto.UnitResponse;
import snowcode.snowcode.unit.service.UnitService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/units")
public class UnitController {

    private final UnitService unitService;
    private final CourseService courseService;

    @PostMapping("/{courseId}")
    public BasicResponse<UnitResponse> createUnit(@PathVariable Long courseId, @Valid @RequestBody UnitRequest dto) {
        Course course = courseService.findCourse(courseId);
        UnitResponse unit = unitService.createUnit(course, dto);
        return ResponseUtil.success(unit);
    }
}
