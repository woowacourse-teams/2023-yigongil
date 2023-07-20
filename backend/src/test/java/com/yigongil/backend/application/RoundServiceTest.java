package com.yigongil.backend.application;

import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.fixture.RoundFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.yigongil.backend.utils.DateConverter.toStringFormat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

        roundService.updateRoundsStartAt(rounds, time, 3);

        assertAll(
                () -> assertThat(toStringFormat(round.getStartAt())).isEqualTo("2023.07.20"),
                () -> assertThat(toStringFormat(round2.getStartAt())).isEqualTo("2023.07.23"),
                () -> assertThat(toStringFormat(round3.getStartAt())).isEqualTo("2023.07.26"),
                () -> assertThat(toStringFormat(round1.getStartAt())).isEqualTo("2023.07.29")

        );
    }
}
