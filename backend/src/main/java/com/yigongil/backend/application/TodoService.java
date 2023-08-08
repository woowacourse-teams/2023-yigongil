package com.yigongil.backend.application;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.optionaltodo.OptionalTodo;
import com.yigongil.backend.domain.optionaltodo.OptionalTodoRepository;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.round.RoundRepository;
import com.yigongil.backend.domain.roundofmember.RoundOfMemberRepository;
import com.yigongil.backend.exception.NotTodoOwnerException;
import com.yigongil.backend.exception.RoundNotFoundException;
import com.yigongil.backend.exception.TodoNotFoundException;
import com.yigongil.backend.request.TodoCreateRequest;
import com.yigongil.backend.request.TodoUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoService {

    private final OptionalTodoRepository optionalTodoRepository;
    private final RoundOfMemberRepository roundOfMemberRepository;
    private final RoundRepository roundRepository;

    public TodoService(
            OptionalTodoRepository optionalTodoRepository,
            RoundOfMemberRepository roundOfMemberRepository,
            RoundRepository roundRepository
    ) {
        this.optionalTodoRepository = optionalTodoRepository;
        this.roundOfMemberRepository = roundOfMemberRepository;
        this.roundRepository = roundRepository;
    }

    @Transactional
    public Long create(Member member, TodoCreateRequest request) {
        Round round = findRoundById(request.roundId());
        if (request.isNecessary()) {
            return round.createNecessaryTodo(member, request.content());
        }
        OptionalTodo todo = round.createOptionalTodo(member, request.content());
        optionalTodoRepository.save(todo);
        return todo.getId();
    }

    private Round findRoundById(Long roundId) {
        return roundRepository.findById(roundId)
                              .orElseThrow(() -> new RoundNotFoundException("존재하지 않는 회차입니다.", roundId));
    }

    @Transactional
    public void update(Member member, Long todoId, TodoUpdateRequest request) {
        if (request.isNecessary()) {
            updateNecessaryTodo(member, todoId, request);
            return;
        }

        updateOptionalTodo(todoId, request);
    }

    private void updateNecessaryTodo(Member member, Long todoId, TodoUpdateRequest request) {
        Round round = findRoundById(todoId);
        if (request.content() != null) {
            round.updateNecessaryTodoContent(request.content());
        }
        if (request.isDone() != null) {
            round.updateNecessaryTodoIsDone(member, request.isDone());
        }
    }

    private void updateOptionalTodo(Long todoId, TodoUpdateRequest request) {
        OptionalTodo todo = findOptionalTodoById(todoId);
        if (request.content() != null) {
            todo.updateContent(request.content());
        }
        if (request.isDone() != null) {
            todo.updateIsDone(request.isDone());
        }
    }

    @Transactional
    public void delete(Member member, Long todoId) {
        OptionalTodo todo = findOptionalTodoById(todoId);
        if (roundOfMemberRepository.existsByOptionalTodosAndMember(todo, member)) {
            optionalTodoRepository.deleteById(todoId);
            return;
        }
        throw new NotTodoOwnerException("투두 작성자가 아닙니다.", String.valueOf(member.getId()));
    }

    private OptionalTodo findOptionalTodoById(Long todoId) {
        return optionalTodoRepository.findById(todoId)
                                     .orElseThrow(() -> new TodoNotFoundException("해당 투두가 존재하지 않습니다.", String.valueOf(todoId)));
    }
}
