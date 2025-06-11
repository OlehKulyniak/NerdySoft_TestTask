package cat.project.model.entity;

import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class MemberBookCompositeKey implements Serializable {
    @Serial
    private final static long serialVersionUID = 4231214214512L;

    private int member_id;
    private int book_id;
}
