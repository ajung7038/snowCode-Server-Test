package snowcode.snowcode.student.dto;

import jakarta.validation.constraints.NotBlank;

public record StudentRequest(@NotBlank String studentId) {
}
