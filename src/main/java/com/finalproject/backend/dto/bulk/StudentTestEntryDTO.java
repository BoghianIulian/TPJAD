package com.finalproject.backend.dto.bulk;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentTestEntryDTO {

    @NotNull(message = "(student id must not be null)")
    private Long studentId;

    @Min(value = 1, message = "(minimum grade value is 1)")
    @Max(value = 10, message = "(maximum grade value is 10)")
    private Integer grade; // optional

    private Boolean absent; // optional
}
