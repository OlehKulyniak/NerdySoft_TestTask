package cat.project.model.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

// This table is used for a future update with possibility to borrow two same books by one member
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "member_book")
public class MemberBook {
    @EmbeddedId
    private MemberBookCompositeKey id;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    @MapsId("member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    @MapsId("book_id")
    private Book book;

}
