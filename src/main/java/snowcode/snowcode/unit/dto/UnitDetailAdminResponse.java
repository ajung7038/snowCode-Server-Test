package snowcode.snowcode.unit.dto;

import snowcode.snowcode.assignment.dto.AssignmentDetailAdminResponse;

import java.util.List;

public record UnitDetailAdminResponse(Long id, String title, String releaseDate, String dueDate, boolean isOpen, int assignmentCount, List<AssignmentDetailAdminResponse> assignments) {
}
