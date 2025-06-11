package cat.project.mapper;

import cat.project.model.dto.AuthorDto;
import cat.project.model.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    Author dtoToAuthor(AuthorDto authorDto);

    AuthorDto authorToDto(Author author);
}
