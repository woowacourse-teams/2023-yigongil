package com.yigongil.backend.application;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.optionaltodo.OptionalTodo;
import com.yigongil.backend.domain.optionaltodo.OptionalTodoRepository;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.round.RoundRepository;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;
import com.yigongil.backend.exception.RoundNotFoundException;
import com.yigongil.backend.request.TodoCreateRequest;
import com.yigongil.backend.request.TodoUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoService {

    private final OptionalTodoRepository optionalTodoRepository;
    private final RoundRepository roundRepository;

    public TodoService(
            OptionalTodoRepository optionalTodoRepository,
            RoundRepository roundRepository
    ) {
        this.optionalTodoRepository = optionalTodoRepository;
        this.roundRepository = roundRepository;
    }

    @Transactional
    public void createNecessaryTodo(Member member, Long roundId, TodoCreateRequest request) {
        Round round = findRoundById(roundId);
        round.createNecessaryTodo(member, request.content());
    }

    private Round findRoundById(Long roundId) {
        return roundRepository.findById(roundId)
                              .orElseThrow(() -> new RoundNotFoundException("존재하지 않는 회차입니다.", roundId));
    }

    @Transactional
    public void updateNecessaryTodo(Member member, Long roundId, TodoUpdateRequest request) {
        Round round = findRoundById(roundId);
        if (request.isDone() != null) {
            round.updateNecessaryTodoIsDone(member, request.isDone());
        }
        if (request.content() != null) {
            round.updateNecessaryTodoContent(member, request.content());
        }
    }

    @Transactional
    public Long createOptionalTodo(Member member, Long roundId, TodoCreateRequest request) {
        Round round = findRoundById(roundId);
        OptionalTodo todo = optionalTodoRepository.save(round.createOptionalTodo(member, request.content()));
        return todo.getId();
    }

    @Transactional
    public void updateOptionalTodo(Member member, Long roundId, Long todoId, TodoUpdateRequest request) {
        Round round = findRoundById(roundId);
        if (request.content() != null) {
            round.updateOptionalTodoContent(member, todoId, request.content());
        }
        if (request.isDone() != null) {
            round.updateOptionalTodoIsDone(member, todoId, request.isDone());
        }
    }

    @Transactional
    public void deleteOptionalTodo(Member member, Long roundId, Long todoId) {
        Round round = findRoundById(roundId);
        RoundOfMember roundOfMember = round.findRoundOfMemberBy(member);
        roundOfMember.removeOptionalTodoById(todoId);
    }
}
