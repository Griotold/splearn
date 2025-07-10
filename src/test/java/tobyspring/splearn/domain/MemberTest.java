package tobyspring.splearn.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberTest {

    // 도메인 규칙을 잘 지키고 있는지 검증해 보는 것이다.
    @Test
    void createMember() {
        var member = new Member("rio@splrean.app", "rio", "secret");

        Assertions.assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

}