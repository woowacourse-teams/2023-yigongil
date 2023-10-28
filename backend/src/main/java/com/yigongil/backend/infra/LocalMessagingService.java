package com.yigongil.backend.infra;

import com.yigongil.backend.domain.member.Member;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile(value = {"local", "test"})
@Service
public class LocalMessagingService implements MessagingService {

    @Override
    public void registerToken(final String token, final Member member) {

    }

    @Override
    public void sendToMember(final String message, final Long memberId) {

    }

    @Override
    public void sendToStudy(final String message, final Long studyId) {

    }
}
