package IO.SampleWeek3_SpringDataJPA.member.dto;

import IO.SampleWeek3_SpringDataJPA.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberResponseDto {
    private long memberId;
    private String email;
    private String name;
    private String phone;
    private Member.MemberStatus memberStatus;

    public String getMemberStatus() {
        return memberStatus.getStatus();
    }
}
