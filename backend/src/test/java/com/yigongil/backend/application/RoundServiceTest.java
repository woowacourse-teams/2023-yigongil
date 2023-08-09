package com.yigongil.backend.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.fixture.RoundFixture;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoundServiceTest {

    @Autowired
    RoundService roundService;

    @Test
    void 시작_시간을_기준으로_라운드_시간을_할당한다() {
        Round round = RoundFixture.아이디없는_라운드.toRound();
        Round round1 = RoundFixture.아이디없는_라운드4.toRound();
        Round round2 = RoundFixture.아이디없는_라운드2.toRound();
        Round round3 = RoundFixture.아이디없는_라운드3.toRound();

        List<Round> rounds = new ArrayList<>(List.of(round1, round2, round3, round));
        LocalDateTime time = LocalDateTime.of(2023, 7, 20, 3, 2, 3);

        roundService.updateRoundsEndAt(rounds, time, 3);

        assertAll(
                () -> assertThat(round.getEndAt().toLocalDate()).isEqualTo(LocalDate.of(2023, 7, 23)),
                () -> assertThat(round2.getEndAt().toLocalDate()).isEqualTo(LocalDate.of(2023, 7, 26)),
                () -> assertThat(round3.getEndAt().toLocalDate()).isEqualTo(LocalDate.of(2023, 7, 29)),
                () -> assertThat(round1.getEndAt().toLocalDate()).isEqualTo(LocalDate.of(2023, 8, 1))
        );
    }
}
