package tobyspring.splearn.application.provided;

import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberRegisterRequest;

/**
 * 회원의 등록 관련된기능을 제고한다.
 * */
public interface MemberRegister {

    Member register(MemberRegisterRequest registerRequest);
}
