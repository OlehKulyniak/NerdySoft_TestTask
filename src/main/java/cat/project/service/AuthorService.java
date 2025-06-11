package cat.project.service;

import cat.project.exception.NotFoundException;
import cat.project.mapper.AuthorMapper;
import cat.project.model.dto.AuthorDto;
import cat.project.model.entity.Author;
import cat.project.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public List<AuthorDto> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        List<AuthorDto> authorDtos = new ArrayList<>();
        for(var author : authors) {
            authorDtos.add(authorMapper.authorToDto(author));
        }
        return authorDtos;
    }

    public AuthorDto getAuthorById(int id) throws NotFoundException {
        Author author = authorRepository.findById(id);
        if(author == null) {
            throw new NotFoundException("Author with id - " + id + " was not found.");
        }
        return authorMapper.authorToDto(author);
    }

    public void deleteAuthorById(int id) {
        authorRepository.deleteById(id);
    }
}
