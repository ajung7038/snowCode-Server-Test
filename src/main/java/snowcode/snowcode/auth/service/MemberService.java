package snowcode.snowcode.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.domain.Role;
import snowcode.snowcode.auth.dto.MemberRequest;
import snowcode.snowcode.auth.dto.MemberResponse;
import snowcode.snowcode.auth.exception.AuthErrorCode;
import snowcode.snowcode.auth.exception.AuthException;
import snowcode.snowcode.auth.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse signup(MemberRequest rq) {
        Member member = Member.createMember(rq.name(), Role.of(rq.role()), rq.email());
        memberRepository.save(member);
        return MemberResponse.from(member);
    }

    public MemberResponse findMemberById(long id) {
        Member member = findMember(id);
        return MemberResponse.from(member);
    }

    public Member findMember(long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new AuthException(AuthErrorCode.MEMBER_NOT_FOUND)
        );
    }
}
