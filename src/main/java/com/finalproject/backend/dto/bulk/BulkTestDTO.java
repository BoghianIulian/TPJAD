package com.finalproject.backend.dto.bulk;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkTestDTO {

    @NotNull(message = "(classroom id must not be null)")
    private Long classroomId;

    @NotNull(message = "(classCourse id must not be null)")
    private Long classCourseId;

    @NotNull(message = "(test date must not be null)")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate testDate;

    @NotNull(message = "(entries list must not be null)")
    private List<StudentTestEntryDTO> entries;
}
