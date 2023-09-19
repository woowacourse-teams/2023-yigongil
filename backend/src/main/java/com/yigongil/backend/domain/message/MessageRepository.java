package com.yigongil.backend.domain.message;

import com.yigongil.backend.domain.member.Member;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface MessageRepository extends Repository<Message, Long> {

    Message save(Message message);

    @Query("""
                        select distinct m.sender as sender, m.receiver as receiver
                        from Message m
                        where m.sender = :member or m.receiver = :member
            """)
    List<MessageSenderReceiverDto> findBySenderOrReceiver(Member member);

    @Query("""
                        select m.content as content, m.createdAt as createdAt,
                        case when m.sender.id = :memberId then true else false end as isMine
                        from Message m
                        where (m.sender.id = :memberId and m.receiver.id = :opponentId)
                        or (m.sender.id = :opponentId and m.receiver.id = :memberId)
                        order by m.id desc
            """)
    Slice<MessageListDto> findAllMessageByMemberAndPaging(Long memberId, Long opponentId, Pageable pageable);
}
