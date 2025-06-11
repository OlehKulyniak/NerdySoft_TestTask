package cat.project.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private int id;

    @NotNull
    @Pattern(regexp = "[A-Z].{2,}")
    private String title;

    @NotNull
    private AuthorDto authorDto;

    private int amount;
}
