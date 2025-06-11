package cat.project.repository;

import cat.project.model.dto.BorrowedBookDto;
import cat.project.model.entity.Book;
import cat.project.model.entity.MemberBook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface MemberBookRepository extends JpaRepository<MemberBook, Integer> {
    List<MemberBook> findAllByBook_Id(int bookId);
    List<MemberBook> findAllByMember_Id(int memberId);

    @Query("SELECT mb.book FROM MemberBook mb JOIN mb.member mbMember WHERE mbMember.name = :name")
    List<Book> findAllBookByMember_Name(@Param("name") String name);

    @Query("SELECT DISTINCT mbBook.title FROM MemberBook mb JOIN mb.book mbBook")
    List<String> findAllDistinctBook_Title();

    @Query("SELECT new cat.project.model.dto.BorrowedBookDto(mbBook.title, SUM(COUNT(mbBook.title))) FROM MemberBook mb JOIN mb.book mbBook GROUP BY mbBook.title")
    List<BorrowedBookDto> findAllDistinctBook_TitleAndCount();
}
