package tobyspring.splearn.application.provided;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.*;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@Import(SplearnTestConfiguration.class)
@SpringBootTest
public record MemberRegisterTest(MemberRegister memberRegister, EntityManager em) {

    @Test
    void register() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmailFail() {
        memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest()))
                .isInstanceOf(DuplicateEmailException.class);
        
    }

    @Test
    void memberRegisterRequestFail() {
        MemberRegisterRequest registerRequest = new MemberRegisterRequest("splearn@email.com", "rio", "secret");
        extracted("splearn@email.com", "rio", "longSecret");
        extracted("splearn@email.com", "rio1234", "secret");
        extracted("splearnemail.com", "rio1234", "longSecret");
    }

    @Test
    void activate() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        em.flush();
        em.clear();

        member = memberRegister.activate(member.getId());

        em.flush();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);

    }

    private void extracted(String email, String nickname, String password) {
        assertThatThrownBy(() -> memberRegister.register(new MemberRegisterRequest(email, nickname, password)))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
