package com.yigongil.backend.domain.message;

import com.yigongil.backend.domain.member.Member;

public interface MessageSenderReceiverDto {

    Member getSender();

    Member getReceiver();
}
