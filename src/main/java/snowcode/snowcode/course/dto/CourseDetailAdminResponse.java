package snowcode.snowcode.course.dto;

import snowcode.snowcode.unit.dto.UnitDetailAdminResponse;

import java.util.List;

public record CourseDetailAdminResponse(Long id, String title, int year, String semester, String section, int studentCount, int unitCount, List<UnitDetailAdminResponse> units) {
}
