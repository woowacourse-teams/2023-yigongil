package com.yigongil.backend.infra;

import com.yigongil.backend.domain.member.Member;

public interface MessagingService {

    void registerToken(String token, Member member);

    void sendToMember(String message, Long memberId);

    void sendToStudy(String message, Long studyId);
}
