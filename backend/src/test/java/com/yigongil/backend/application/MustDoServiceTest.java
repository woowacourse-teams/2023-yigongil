package com.yigongil.backend.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.willReturn;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.domain.round.MustDoService;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.round.RoundRepository;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;
import com.yigongil.backend.exception.InvalidTodoLengthException;
import com.yigongil.backend.fixture.MemberFixture;
import com.yigongil.backend.request.MustDoUpdateRequest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MustDoServiceTest {

    @InjectMocks
    private MustDoService mustDoService;

    @Mock
    private RoundRepository roundRepository;

    private Member member;
    private Round round;

    @BeforeEach
    void setUp() {
        member = MemberFixture.김진우.toMember();

        RoundOfMember roundOfMember = RoundOfMember.builder()
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
    class _머스트두 {

        @Test
        void 머스트두를_생성한다() {
            //given
            willReturn(Optional.of(round)).given(roundRepository).findById(3L);

            MustDoUpdateRequest request = new MustDoUpdateRequest(" 머스트두");

            //when
            //then
            assertDoesNotThrow(() -> mustDoService.updateMustDo(member, round.getId(), request));
        }

        @Test
        void 머스트두가_20자_이상이면_예외가_발생한다() {
            //given
            willReturn(Optional.of(round)).given(roundRepository).findById(3L);

            MustDoUpdateRequest request = new MustDoUpdateRequest(" 머스트두를 20글자 이상으로 만들고 있습니다 아아");

            //when
            //then
            assertThatThrownBy(() -> mustDoService.updateMustDo(member, round.getId(), request))
                    .isInstanceOf(InvalidTodoLengthException.class);
        }
    }
}
