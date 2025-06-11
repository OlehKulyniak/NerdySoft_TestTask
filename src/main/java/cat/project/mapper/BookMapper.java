package cat.project.mapper;

import cat.project.model.dto.AuthorDto;
import cat.project.model.entity.Author;
import cat.project.model.entity.Book;
import cat.project.model.dto.BookDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(target = "author", source = "bookDto.authorDto", qualifiedByName = "mapDtoToAuthor")
    Book dtoToBook(BookDto bookDto);

    @Mapping(target = "authorDto", source = "book.author", qualifiedByName = "mapAuthorToDto")
    BookDto bookToDto(Book book);

    @Named("mapAuthorToDto")
    default AuthorDto mapAuthorToDto(Author author) {
        return AuthorMapper.INSTANCE.authorToDto(author);
    }

    @Named("mapDtoToAuthor")
    default Author mapDtoToAuthor(AuthorDto authorDto) {
        return AuthorMapper.INSTANCE.dtoToAuthor(authorDto);
    }
}
