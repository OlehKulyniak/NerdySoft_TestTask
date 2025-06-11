package cat.project.mapper;

import cat.project.model.dto.MemberDto;
import cat.project.model.entity.Member;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    Member dtoToMember(MemberDto memberDto);

    MemberDto memberToDto(Member member);
}
