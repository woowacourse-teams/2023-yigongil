package com.yigongil.backend.application;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.optionaltodo.OptionalTodo;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyRepository;
import com.yigongil.backend.exception.StudyNotFoundException;
import com.yigongil.backend.request.TodoCreateRequest;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoService {

    private final EntityManager entityManager;
    private final StudyRepository studyRepository;

    public TodoService(EntityManager entityManager, StudyRepository studyRepository) {
        this.entityManager = entityManager;
        this.studyRepository = studyRepository;
    }

    @Transactional
    public Long create(Member member, Long studyId, TodoCreateRequest request) {
        Study study = studyRepository.findById(studyId).
                orElseThrow(() -> new StudyNotFoundException("해당 스터디가 존재하지 않습니다.", studyId));
        if (request.isNecessary()) {
            return study.createNecessaryTodo(member, request.roundId(), request.content());
        }
        OptionalTodo todo = study.createOptionalTodo(member, request.roundId(), request.content());
        entityManager.flush();
        return todo.getId();
    }
}
