package tobyspring.splearn.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tobyspring.splearn.application.provided.MemberRegister;
import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.application.required.MemberRepository;
import tobyspring.splearn.domain.*;

@Validated
@Transactional
@RequiredArgsConstructor
@Service
public class MemberService implements MemberRegister {
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;


    /**
     * 애플리케이션 서비스의 public 메서드는 추상화 수준이 높아야 한다.
     * 코드를 읽어나갈 때, 문서 처럼 읽혀야 한다.
     * 그렇지 않으면 리팩토링이 필요하다.
     * */
    @Override
    public Member register(MemberRegisterRequest registerRequest) {
        // check
        checkDuplicateEmail(registerRequest);

        // domain model
        Member member = Member.register(registerRequest, passwordEncoder);

        // repository
        memberRepository.save(member);

        // post process
        sendWelcomeEmail(member);
        return member;
    }

    private void sendWelcomeEmail(Member member) {
        emailSender.send(member.getEmail(), "등록을 완료해주세요.", "아래 링크를 클릭해서 등록을 완료해주세요.");
    }

    private void checkDuplicateEmail(MemberRegisterRequest registerRequest) {
        if (memberRepository.findByEmail(new Email(registerRequest.email())).isPresent()) {
            throw new DuplicateEmailException("이메일이 중복 됩니다.");
        }
    }

}
