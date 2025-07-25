```java
package tobyspring.splearn.application.provided;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import tobyspring.splearn.application.MemberModifyService;
import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.application.required.MemberRepository;
import tobyspring.splearn.domain.Email;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.MemberStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class MemberRegisterManualTest {

    @Test
    void registerTestStub() {
        MemberService memberService 
                = new MemberService(new MemberRepositoryStub(), 
                new EmailSenderStub(), 
                MemberFixture.createPasswordEncoder());

        Member member = memberService.register(MemberFixture.createMemberRegisterRequest());
        
        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void registerTestMock() {
        EmailSenderMock emailSenderMock = new EmailSenderMock();
        MemberService memberService
                = new MemberService(new MemberRepositoryStub(),
                emailSenderMock,
                MemberFixture.createPasswordEncoder());

        Member member = memberService.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        assertThat(emailSenderMock.getTos()).hasSize(1);
        assertThat(emailSenderMock.getTos().getFirst()).isEqualTo(member.getEmail());
    }

    @Test
    void registerTestMockito() {
        EmailSender emailSenderMock = Mockito.mock(EmailSender.class);
        MemberService memberService
                = new MemberService(new MemberRepositoryStub(),
                emailSenderMock,
                MemberFixture.createPasswordEncoder());

        Member member = memberService.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        Mockito.verify(emailSenderMock).send(any(Email.class), any(), any());
    }

    static class MemberRepositoryStub implements MemberRepository {

        @Override
        public Member save(Member member) {
            ReflectionTestUtils.setField(member, "id", 1L);
            return member;
        }

        @Override
        public Optional<Member> findByEmail(Email email) {
            return Optional.empty();
        }
    }
    
    static class EmailSenderStub implements EmailSender {

        @Override
        public void send(Email email, String subject, String body) {}
    }

    static class EmailSenderMock implements EmailSender {
        List<Email> tos =  new ArrayList<>();
        @Override
        public void send(Email email, String subject, String body) {
            tos.add(email);
        }

        public List<Email> getTos() {
            return tos;
        }
    }
}
```