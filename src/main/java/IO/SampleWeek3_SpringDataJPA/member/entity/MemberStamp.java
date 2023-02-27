package IO.SampleWeek3_SpringDataJPA.member.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "STAMP")
public class MemberStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stampId;

    @Column(nullable = false, unique = true, length = 100)
    private int stampCount;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column(nullable = false, name = "LAST_MODIFIED_AT")
    private LocalDateTime modifiedAt;

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
