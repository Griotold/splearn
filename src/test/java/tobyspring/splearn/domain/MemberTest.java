package tobyspring.splearn.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.*;

class MemberTest {

    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
        member = Member.create(new MemberCreateRequest("rio@splearn.app", "rio", "secret"), passwordEncoder);
    }

    // 도메인 규칙을 잘 지키고 있는지 검증해 보는 것이다.
    @Test
    void createMember() {
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void activate() {
        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    void activateFail() {
        member.activate();

        assertThatThrownBy(() -> member.activate())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deactivate() {
        member.activate();

        member.deactivate();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    // 검증 하는 게 두 부분이지만, 간단하니까 같이 검증해본다.
    @Test
    void deactivateFail() {
        assertThatThrownBy(() -> member.deactivate()).isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(()-> member.deactivate()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void verifyPassword() {
        assertThat(member.verifyPassword("secret", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("false", passwordEncoder)).isFalse();
    }

    @Test
    void changeNickname() {
        assertThat(member.getNickname()).isEqualTo("rio");

        member.changeNickname("mimi");

        assertThat(member.getNickname()).isEqualTo("mimi");
    }

    @Test
    void changePassword() {
        member.changePassword("verySecret", passwordEncoder);
        
        assertThat(member.verifyPassword("verySecret", passwordEncoder)).isTrue();
    }

    @Test
    void isActive() {
        assertThat(member.isActive()).isFalse();

        member.activate();

        assertThat(member.isActive()).isTrue();

        member.deactivate();

        assertThat(member.isActive()).isFalse();
    }
}