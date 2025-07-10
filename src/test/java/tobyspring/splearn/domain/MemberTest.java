package tobyspring.splearn.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MemberTest {

    // 도메인 규칙을 잘 지키고 있는지 검증해 보는 것이다.
    @Test
    void createMember() {
        var member = new Member("rio@splrean.app", "rio", "secret");

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void constructorNullCheck() {
        assertThatThrownBy(() -> new Member(null, "rio", "secret"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void activate() {
        Member member = new Member("rio@splrean.app", "rio", "secret");

        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    void activateFail() {
        Member member = new Member("rio@splearn.app", "rio", "secret");

        member.activate();

        assertThatThrownBy(() -> member.activate())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deactivate() {
        Member member = new Member("rio@splearn.app", "rio", "secret");

        member.activate();

        member.deactivate();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    // 검증 하는 게 두 부분이지만, 간단하니까 같이 검증해본다.
    @Test
    void deactivateFail() {
        Member member = new Member("rio@splearn.app", "rio", "secret");

        assertThatThrownBy(() -> member.deactivate()).isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(()-> member.deactivate()).isInstanceOf(IllegalStateException.class);
    }

}