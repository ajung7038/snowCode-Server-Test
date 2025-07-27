package snowcode.snowcode.assignment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import snowcode.snowcode.testcase.dto.TestcaseCreateRequest;

import java.util.List;

public record AssignmentUpdateWithTestcaseRequest(@NotBlank String title, @NotNull int score, String description, List<TestcaseCreateRequest> testcases) {
}
