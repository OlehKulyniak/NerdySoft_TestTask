package cat.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import cat.project.exception.NotFoundException;
import cat.project.mapper.AuthorMapper;
import cat.project.model.dto.AuthorDto;
import cat.project.model.entity.Author;
import cat.project.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mapstruct.factory.Mappers;


import java.util.List;

@ExtendWith({MockitoExtension.class} )
class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;
    @Spy
    private AuthorMapper authorMapper = AuthorMapper.INSTANCE;
    @InjectMocks
    private AuthorService authorService;


    @Test
    void getAllAuthorsReturnsListOfDtos() {
        Author firstAuthor = new Author(1, "John Doe");
        Author secondAuthor = new Author(2, "Jane Roe");
        when(authorRepository.findAll()).thenReturn(List.of(firstAuthor, secondAuthor));

        List<AuthorDto> authorDtos = authorService.getAllAuthors();

        assertEquals(2, authorDtos.size());
        assertEquals("John Doe", authorDtos.get(0).getName());
        assertEquals("Jane Roe", authorDtos.get(1).getName());
    }

    @Test
    void getAuthorByIdReturnsDto() throws NotFoundException {
        Author author = new Author(5, "Foo Bar");
        when(authorRepository.findById(5)).thenReturn(author);

        AuthorDto authorDto = authorService.getAuthorById(5);

        assertEquals(5, authorDto.getId());
        assertEquals("Foo Bar", authorDto.getName());
    }

    @Test
    void getAuthorByIdThrowsNotFoundException() {
        when(authorRepository.findById(10)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> authorService.getAuthorById(10));
        assertTrue(exception.getMessage().contains("Author with id - 10"));
    }

}