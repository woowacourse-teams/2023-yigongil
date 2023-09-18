package com.yigongil.backend.domain.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.yigongil.backend.config.JpaConfig;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.fixture.MemberFixture;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member 김진우;
    private Member 노이만;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        김진우 = memberRepository.save(MemberFixture.김진우.toMember());
        노이만 = memberRepository.save(MemberFixture.폰노이만.toMember());
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void 메시지를_주고받은_상대들을_조회하는_쿼리() {
        messageRepository.save(new Message(null, 김진우, 노이만, "Hello, world!"));
        messageRepository.save(new Message(null, 노이만, 김진우, "Hello, world!"));

        List<MessageSenderReceiverDto> result = messageRepository.findBySenderOrReceiverOrderByIdDesc(김진우);

        assertThat(result).hasSize(2);
    }

    @Test
    void 메시지를_주고받은_상대들을_조회하는_쿼리2() {
        messageRepository.save(new Message(null, 김진우, 노이만, "Hello, world!"));
        messageRepository.save(new Message(null, 김진우, 노이만, "Hello, world!"));
        messageRepository.save(new Message(null, 노이만, 김진우, "Hello, world!"));

        List<MessageSenderReceiverDto> result = messageRepository.findBySenderOrReceiverOrderByIdDesc(김진우);

        assertThat(result).hasSize(2);
    }

    @Test
    void 메시지를_주고받은_상대들을_조회하는_쿼리3() {
        Member 파울러 = memberRepository.save(MemberFixture.마틴파울러.toMemberWithoutId());

        messageRepository.save(new Message(null, 김진우, 노이만, "Hello, world!"));
        messageRepository.save(new Message(null, 김진우, 노이만, "Hello, world!"));
        messageRepository.save(new Message(null, 노이만, 김진우, "Hello, world!"));
        messageRepository.save(new Message(null, 파울러, 김진우, "Hello, world!"));

        List<MessageSenderReceiverDto> result = messageRepository.findBySenderOrReceiverOrderByIdDesc(김진우);

        assertThat(result).hasSize(3);
    }

    @Test
    void 메시지를_주고받은_상대들을_조회하는_쿼리4() {
        Member 파울러 = memberRepository.save(MemberFixture.마틴파울러.toMemberWithoutId());

        messageRepository.save(new Message(null, 김진우, 노이만, "Hello, world!"));
        messageRepository.save(new Message(null, 김진우, 노이만, "Hello, world!"));
        messageRepository.save(new Message(null, 노이만, 김진우, "Hello, world!"));
        messageRepository.save(new Message(null, 파울러, 김진우, "Hello, world!"));
        messageRepository.save(new Message(null, 노이만, 파울러, "Hello, world!"));

        List<MessageSenderReceiverDto> result = messageRepository.findBySenderOrReceiverOrderByIdDesc(김진우);

        assertThat(result).hasSize(3);
    }

    @Test
    void 주고받은_쪽지를_정렬한다() {
        messageRepository.save(new Message(null, 김진우, 노이만, "Hello, world!"));
        messageRepository.save(new Message(null, 김진우, 노이만, "Hello, world!"));
        messageRepository.save(new Message(null, 노이만, 김진우, "Hello, world!"));

        Slice<MessageListDto> result = messageRepository.findAllMessageByMemberAndPaging(김진우.getId(), 노이만.getId(), pageable);

        assertAll(
                () -> assertThat(result).hasSize(3),
                () -> assertThat(result.getContent().get(0).getIsMine()).isFalse(),
                () -> assertThat(result.getContent().get(1).getIsMine()).isTrue(),
                () -> assertThat(result.getContent().get(2).getIsMine()).isTrue()
        );
    }

    @Test
    void 주고받은_쪽지를_페이징한다() {
        for (int i = 0; i < 11; i++) {
            messageRepository.save(new Message(null, 김진우, 노이만, "Hello, world!"));
        }

        Slice<MessageListDto> result = messageRepository.findAllMessageByMemberAndPaging(김진우.getId(), 노이만.getId(), pageable);

        assertAll(
                () -> assertThat(result).hasSize(pageable.getPageSize()),
                () -> assertThat(result.hasNext()).isTrue()
        );
    }

    @Test
    void 주고받은_쪽지를_페이징한다2() {
        for (int i = 0; i < 9; i++) {
            messageRepository.save(new Message(null, 김진우, 노이만, "Hello, world!"));
        }

        Slice<MessageListDto> result = messageRepository.findAllMessageByMemberAndPaging(김진우.getId(), 노이만.getId(), pageable);

        assertAll(
                () -> assertThat(result).hasSize(9),
                () -> assertThat(result.hasNext()).isFalse(),
                () -> assertThat(result).map(MessageListDto::getIsMine).doesNotContain(false)
        );
    }

    @Test
    void 파라미터_순서를_바꿔서_주고받은_쪽지를_페이징한다() {
        for (int i = 0; i < 9; i++) {
            messageRepository.save(new Message(null, 김진우, 노이만, "Hello, world!"));
        }

        Slice<MessageListDto> result = messageRepository.findAllMessageByMemberAndPaging(노이만.getId(), 김진우.getId(), pageable);

        assertAll(
                () -> assertThat(result).hasSize(9),
                () -> assertThat(result.hasNext()).isFalse(),
                () -> assertThat(result).map(MessageListDto::getIsMine).doesNotContain(true)
        );
    }
}
