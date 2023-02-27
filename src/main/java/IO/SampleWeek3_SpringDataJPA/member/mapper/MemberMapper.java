package IO.SampleWeek3_SpringDataJPA.member.mapper;

import IO.SampleWeek3_SpringDataJPA.member.dto.MemberPatchDto;
import IO.SampleWeek3_SpringDataJPA.member.dto.MemberPostDto;
import IO.SampleWeek3_SpringDataJPA.member.dto.MemberResponseDto;
import IO.SampleWeek3_SpringDataJPA.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
    Member memberPostDtoToMember(MemberPostDto memberPostDto);
    Member memberPatchDtoToMember(MemberPatchDto memberPatchDto);
    MemberResponseDto memberToMemberResponseDto(Member member);
    List<MemberResponseDto> membersToMemberResponseDtos(List<Member> members);
}
