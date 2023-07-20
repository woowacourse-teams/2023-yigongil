package com.yigongil.backend.application;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.optionaltodo.OptionalTodo;
import com.yigongil.backend.domain.optionaltodo.OptionalTodoRepository;
import com.yigongil.backend.domain.roundofmember.RoundOfMemberRepository;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyRepository;
import com.yigongil.backend.exception.NotTodoOwnerException;
import com.yigongil.backend.exception.StudyNotFoundException;
import com.yigongil.backend.exception.TodoNotFoundException;
import com.yigongil.backend.request.TodoCreateRequest;
import com.yigongil.backend.request.TodoUpdateRequest;
import java.util.Objects;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoService {

    private final EntityManager entityManager;
    private final StudyRepository studyRepository;
    private final OptionalTodoRepository optionalTodoRepository;
    private final RoundOfMemberRepository roundOfMemberRepository;

    public TodoService(
            EntityManager entityManager,
            StudyRepository studyRepository,
            OptionalTodoRepository optionalTodoRepository,
            RoundOfMemberRepository roundOfMemberRepository) {
        this.entityManager = entityManager;
        this.studyRepository = studyRepository;
        this.optionalTodoRepository = optionalTodoRepository;
        this.roundOfMemberRepository = roundOfMemberRepository;
    }

    @Transactional
    public Long create(Member member, Long studyId, TodoCreateRequest request) {
        Study study = findStudyById(studyId);
        if (request.isNecessary()) {
            return study.createNecessaryTodo(member, request.roundId(), request.content());
        }
        OptionalTodo todo = study.createOptionalTodo(member, request.roundId(), request.content());
        entityManager.flush();
        return todo.getId();
    }

    @Transactional
    public void update(Member member, Long studyId, Long todoId, TodoUpdateRequest request) {
        if (request.isNecessary()) {
            updateNecessaryTodo(member, studyId, todoId, request);
            return;
        }

        updateOptionalTodo(todoId, request);
    }

    private void updateNecessaryTodo(Member member, Long studyId, Long todoId, TodoUpdateRequest request) {
        Study study = findStudyById(studyId);
        if (Objects.nonNull(request.content())) {
            study.updateNecessaryTodoContent(todoId, request.content());
        }
        if (Objects.nonNull(request.isDone())) {
            study.updateNecessaryTodoIsDone(member, todoId, request.isDone());
        }
    }

    private Study findStudyById(Long studyId) {
        return studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException("해당 스터디가 존재하지 않습니다.", studyId));
    }

    private void updateOptionalTodo(Long todoId, TodoUpdateRequest request) {
        OptionalTodo todo = findOptionalTodoById(todoId);
        if (Objects.nonNull(request.content())) {
            todo.updateContent(request.content());
        }
        if (Objects.nonNull(request.isDone())) {
            todo.updateIsDone(request.isDone());
        }
    }

    @Transactional
    public void delete(Member member, Long studyId, Long todoId) {
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
