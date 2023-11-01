package com.yigongil.backend.domain.round;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.exception.RoundNotFoundException;
import com.yigongil.backend.request.MustDoUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MustDoService {

    private final RoundRepository roundRepository;

    public MustDoService(
            RoundRepository roundRepository
    ) {
        this.roundRepository = roundRepository;
    }

    @Transactional
    public void updateMustDo(Member member, Long roundId, MustDoUpdateRequest request) {
        Round round = findRoundById(roundId);
        round.updateMustDo(member, request.content());
    }

    private Round findRoundById(Long roundId) {
        return roundRepository.findById(roundId)
                              .orElseThrow(() -> new RoundNotFoundException("존재하지 않는 회차입니다.", roundId));
    }
}
