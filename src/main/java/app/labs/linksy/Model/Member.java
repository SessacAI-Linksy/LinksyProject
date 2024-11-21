package app.labs.linksy.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MEMBER")
public class Member {
    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "USER_NICKNAME")
    private String userNickname;

    @Column(name = "USER_PWD")
    private String userPwd;

    @Column(name = "USER_EMAIL")
    private String userEmail;

    @Column(name = "USER_IMG")
    private String userImg;

    @Column(name = "USER_INTRODUCE")
    private String userIntroduce;
}
