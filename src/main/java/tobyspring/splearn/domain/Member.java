package tobyspring.splearn.domain;

import lombok.Getter;
import lombok.ToString;

import static java.util.Objects.*;
import static org.springframework.util.Assert.*;

@ToString
@Getter
public class Member {

    private Email email;

    private String nickname;

    private String  passwordHash;

    private MemberStatus status;

    private Member() {
    }

    public static Member register(MemberRegisterRequest registerRequest, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.email = new Email(registerRequest.email());
        member.nickname = requireNonNull(registerRequest.nickname());
        member.passwordHash = requireNonNull(passwordEncoder.encode(registerRequest.password()));

        member.status = MemberStatus.PENDING;
        return member;
    }

    public void activate() {
        state(status == MemberStatus.PENDING, "MemberStatus is Not Pending.");

        this.status = MemberStatus.ACTIVE;
    }

    public void deactivate() {
        state(status == MemberStatus.ACTIVE, "MemberStatus is Not Active.");

        this.status = MemberStatus.DEACTIVATED;
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, passwordHash);
    }

    public void changeNickname(String nickname) {
        this.nickname = requireNonNull(nickname);
    }

    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(requireNonNull(password));
    }

    public boolean isActive() {
        return status == MemberStatus.ACTIVE;
    }
}
