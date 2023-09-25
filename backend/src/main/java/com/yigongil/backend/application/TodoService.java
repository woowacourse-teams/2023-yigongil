package com.yigongil.backend.application;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.round.RoundRepository;
import com.yigongil.backend.exception.RoundNotFoundException;
import com.yigongil.backend.request.TodoCreateRequest;
import com.yigongil.backend.request.TodoUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoService {

    private final RoundRepository roundRepository;

    public TodoService(
            RoundRepository roundRepository
    ) {
        this.roundRepository = roundRepository;
    }

    @Transactional
    public void createNecessaryTodo(Member member, Long roundId, TodoCreateRequest request) {
        Round round = findRoundById(roundId);
        round.createNecessaryTodo(member, request.content());
    }

    @Transactional
    public void updateNecessaryTodo(Member member, Long roundId, TodoUpdateRequest request) {
        Round round = findRoundById(roundId);
        if (request.content() != null) {
            round.updateNecessaryTodoContent(member, request.content());
        }
    }

    private Round findRoundById(Long roundId) {
        return roundRepository.findById(roundId)
                              .orElseThrow(() -> new RoundNotFoundException("존재하지 않는 회차입니다.", roundId));
    }
}
