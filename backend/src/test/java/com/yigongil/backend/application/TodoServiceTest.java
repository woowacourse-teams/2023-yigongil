package com.yigongil.backend.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.willReturn;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.optionaltodo.OptionalTodo;
import com.yigongil.backend.domain.optionaltodo.OptionalTodoRepository;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyRepository;
import com.yigongil.backend.exception.NecessaryTodoAlreadyExistException;
import com.yigongil.backend.fixture.MemberFixture;
import com.yigongil.backend.fixture.RoundFixture;
import com.yigongil.backend.fixture.StudyFixture;
import com.yigongil.backend.request.TodoCreateRequest;
import com.yigongil.backend.request.TodoUpdateRequest;
import java.util.Optional;
import javax.persistence.EntityManager;
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
    private StudyRepository studyRepository;

    @Mock
    private OptionalTodoRepository optionalTodoRepository;

    @Mock
    private EntityManager entityManager;

    Member member;
    Round round;
    Study study;
    OptionalTodo todo;

    @BeforeEach
    void setUp() {
        member = MemberFixture.김진우.toMember();
        round = RoundFixture.아이디_삼_투두없는_라운드.toRound();
        study = StudyFixture.자바_스터디_진행중.toStudy();
        todo = OptionalTodo.builder()
                           .id(1L)
                           .content("투두").build();
    }

    @Nested
    class 필수_투두 {

        @Test
        void 투두를_생성한다() {
            //given
            willReturn(Optional.of(study)).given(studyRepository).findById(1L);

            TodoCreateRequest request = new TodoCreateRequest(true, round.getId(), "필수 투두");

            //when
            Long id = todoService.create(member, study.getId(), request);

            //then
            assertThat(id).isEqualTo(round.getId());
        }

        @Test
        void 투두가_이미_존재하면_예외를_던진다() {
            //given
            willReturn(Optional.of(study)).given(studyRepository).findById(1L);

            TodoCreateRequest request1 = new TodoCreateRequest(true, round.getId(), "필수 투두1");
            todoService.create(member, study.getId(), request1);
            TodoCreateRequest request2 = new TodoCreateRequest(true, round.getId(), "필수 투두2");

            //when
            ThrowingCallable throwable = () -> todoService.create(member, study.getId(), request2);

            //then
            assertThatThrownBy(throwable)
                    .isInstanceOf(NecessaryTodoAlreadyExistException.class);
        }

        @Test
        void 투두를_수정한다() {
            //given
            willReturn(Optional.of(study)).given(studyRepository).findById(1L);

            Round roundById = study.findRoundById(round.getId());
            TodoUpdateRequest request = new TodoUpdateRequest(true, true, "hey");
            study.createNecessaryTodo(member, roundById.getId(), "기존 투두");

            //when
            todoService.update(member, study.getId(), roundById.getId(), request);

            //then
            assertAll(
                    () -> assertThat(roundById.getNecessaryToDoContent()).isEqualTo(
                            request.content()),
                    () -> assertThat(roundById.findRoundOfMemberBy(member).isDone()).isEqualTo(
                            request.isDone())
            );
        }
    }

    @Nested
    class 선택_투두 {

        @Test
        void 투두를_생성한다() {
            //given
            willReturn(Optional.of(study)).given(studyRepository).findById(1L);

            TodoCreateRequest request = new TodoCreateRequest(false, round.getId(), "선택 투두");

            //when
            //then
            assertDoesNotThrow(() -> todoService.create(member, study.getId(), request));
        }

        @Test
        void 투두를_수정한다() {
            //given
            willReturn(Optional.of(todo)).given(optionalTodoRepository).findById(1L);

            study.findRoundById(round.getId())
                 .findRoundOfMemberBy(member)
                 .getOptionalTodos()
                 .add(todo);
            TodoUpdateRequest request = new TodoUpdateRequest(false, true, "수정된 내용");

            //when
            todoService.update(member, study.getId(), todo.getId(), request);

            //then
            assertAll(
                    () -> assertThat(todo.getContent()).isEqualTo(request.content()),
                    () -> assertThat(todo.isDone()).isEqualTo(request.isDone())
            );
        }
    }
}
