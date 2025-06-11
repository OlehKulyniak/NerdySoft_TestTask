package cat.project.model.dto;

import jakarta.validation.constraints.Pattern;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {
    private int id;

    @Pattern(regexp = "[A-Z][a-z]* [A-Z][a-z]*")
    private String name;
}
