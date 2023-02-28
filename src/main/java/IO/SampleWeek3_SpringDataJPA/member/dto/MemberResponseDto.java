package IO.SampleWeek3_SpringDataJPA.member.dto;

import IO.SampleWeek3_SpringDataJPA.member.entity.Member;
import IO.SampleWeek3_SpringDataJPA.member.entity.Stamp;
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
    private Stamp stamp;

    public String getMemberStatus() {
        return memberStatus.getStatus();
    }
    public int getStamp() {
        return stamp.getStampCount();
    }
}
