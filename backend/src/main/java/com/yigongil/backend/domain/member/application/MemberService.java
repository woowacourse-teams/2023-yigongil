package com.yigongil.backend.domain.member.application;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.domain.member.domain.MemberRepository;
import com.yigongil.backend.domain.member.domain.Nickname;
import com.yigongil.backend.request.ProfileUpdateRequest;
import com.yigongil.backend.response.NicknameValidationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void update(Member member, ProfileUpdateRequest request) {
        member.updateProfile(request.nickname(), request.introduction());
    }

    @Transactional
    public void delete(Member member) {
        memberRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public NicknameValidationResponse existsByNickname(String nickname) {
        boolean exists = memberRepository.existsByNickname(new Nickname(nickname));
        return new NicknameValidationResponse(exists);
    }
}
