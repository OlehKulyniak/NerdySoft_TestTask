package cat.project.repository;

import cat.project.model.entity.Book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Book findById(int id);

    // We can change this method to search by Author Id for possibility to have authors with same fullname
    Book findByTitleAndAuthor_Name(String title, String authorName);
}
