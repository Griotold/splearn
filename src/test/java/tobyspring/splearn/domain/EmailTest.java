package tobyspring.splearn.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class EmailTest {
    // 값 객체의 동등성이 잘 지켜지는가
    @Test
    void equality() {
        Email email1 = new Email("rio@naver.com");
        Email email2 = new Email("rio@naver.com");

        assertThat(email1).isEqualTo(email2);
    }

}