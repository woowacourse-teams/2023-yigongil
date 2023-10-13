package com.yigongil.backend.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.willReturn;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.round.RoundRepository;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;
import com.yigongil.backend.exception.InvalidTodoLengthException;
import com.yigongil.backend.exception.NecessaryTodoAlreadyExistException;
import com.yigongil.backend.fixture.MemberFixture;
import com.yigongil.backend.request.TodoCreateRequest;
import com.yigongil.backend.request.TodoUpdateRequest;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @InjectMocks
    private TodoService todoService;

    @Mock
    private RoundRepository roundRepository;

    private Member member;
    private Round round;
    private RoundOfMember roundOfMember;

    @BeforeEach
    void setUp() {
        member = MemberFixture.김진우.toMember();

        roundOfMember = RoundOfMember.builder()
                                     .member(member)
                                     .build();
        round = Round.builder()
                     .id(3L)
                     .roundOfMembers(List.of(roundOfMember))
                     .master(member)
                     .mustDo(null)
                     .build();
    }

    @Nested
    class 필수_투두 {

        @Test
        void 투두를_생성한다() {
            //given
            willReturn(Optional.of(round)).given(roundRepository).findById(3L);

            TodoCreateRequest request = new TodoCreateRequest("필수 투두");

            //when
            //then
            assertDoesNotThrow(() -> todoService.createNecessaryTodo(member, round.getId(), request));
        }

        @Test
        void 투두가_20자_이상이면_예외가_발생한다() {
            //given
            willReturn(Optional.of(round)).given(roundRepository).findById(3L);

            TodoCreateRequest request = new TodoCreateRequest("필수 투두를 20글자 이상으로 만들고 있습니다 아아");

            //when
            //then
            assertThatThrownBy(() -> todoService.createNecessaryTodo(member, round.getId(), request))
                    .isInstanceOf(InvalidTodoLengthException.class);
        }

        @Test
        void 투두가_이미_존재하면_예외를_던진다() {
            //given
            willReturn(Optional.of(round)).given(roundRepository).findById(3L);

            TodoCreateRequest request1 = new TodoCreateRequest("필수 투두1");
            todoService.createNecessaryTodo(member, 3L, request1);
            TodoCreateRequest request2 = new TodoCreateRequest("필수 투두2");

            //when
            ThrowingCallable throwable = () -> todoService.createNecessaryTodo(member, 3L, request2);

            //then
            assertThatThrownBy(throwable)
                    .isInstanceOf(NecessaryTodoAlreadyExistException.class);
        }

        @Test
        void 투두를_수정한다() {
            //given
            willReturn(Optional.of(round)).given(roundRepository).findById(3L);

            TodoUpdateRequest request = new TodoUpdateRequest("hey");
            round.createNecessaryTodo(member, "기존 투두");

            //when
            todoService.updateNecessaryTodo(member, round.getId(), request);

            //then
            assertThat(round.getMustDo()).isEqualTo(request.content());
        }
    }
}
