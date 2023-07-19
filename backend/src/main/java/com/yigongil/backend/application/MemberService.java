package com.yigongil.backend.application;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.request.MemberJoinRequest;
import com.yigongil.backend.request.ProfileUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void updateMember(Member member, ProfileUpdateRequest request) {
        member.updateProfile(request.nickname(), request.introduction());
    }

    @Transactional
    public Long joinMember(MemberJoinRequest request) {
        Member member = memberRepository.save(
                Member.builder()
                        .githubId(request.githubId())
                        .tier(1)
                        .build()
        );

        return member.getId();
    }
}
