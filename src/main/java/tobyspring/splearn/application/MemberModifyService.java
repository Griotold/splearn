package tobyspring.splearn.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tobyspring.splearn.application.provided.MemberFinder;
import tobyspring.splearn.application.provided.MemberRegister;
import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.application.required.MemberRepository;
import tobyspring.splearn.domain.*;

@Validated
@Transactional
@RequiredArgsConstructor
@Service
public class MemberModifyService implements MemberRegister {
    private final MemberFinder memberFinder;
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

    @Override
    public Member activate(Long memberId) {
        Member member = memberFinder.find(memberId);

        member.activate();

        // 왜 save를 호출해야할까? JPA를 쓰는 게 아니라 Spring data JPA를 사용하기 때문에 그렇다.
        // 1. Spring Data 는 JPA 뿐만 아니라 다양한 데이터베이스 접근 기술을 추상화한 repository abstraction 이기 때문에
        // 2. 도메인 이벤트 발행 기능을 사용하려면 save가 호출이 되어야 한다.
        return memberRepository.save(member);
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
