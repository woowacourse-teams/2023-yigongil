package com.yigongil.backend.ui;

import com.yigongil.backend.application.StudyService;
import com.yigongil.backend.application.TodoService;
import com.yigongil.backend.config.auth.Authorization;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.request.StudyCreateRequest;
import com.yigongil.backend.request.TodoCreateRequest;
import com.yigongil.backend.response.RecruitingStudyResponse;
import com.yigongil.backend.response.StudyDetailResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RequestMapping("/v1/studies")
@RestController
public class StudyController {

    private final StudyService studyService;
    private final TodoService todoService;

    public StudyController(StudyService studyService, TodoService todoService) {
        this.studyService = studyService;
        this.todoService = todoService;
    }

    @PostMapping
    public ResponseEntity<Void> createStudy(@Authorization Member member, @RequestBody StudyCreateRequest request) {
        Long studyId = studyService.create(member, request);
        return ResponseEntity.created(URI.create("/v1/studies/" + studyId)).build();
    }

    @PostMapping("/{studyId}/todos")
    public ResponseEntity<Void> createTodo(
            @Authorization Member member,
            @PathVariable Long studyId,
            @RequestBody TodoCreateRequest request
    ) {
        Long todoId = todoService.create(member, studyId, request);
        return ResponseEntity.created(URI.create("/v1/studies/" + studyId + "/todos/" + todoId)).build();
    }

    @PostMapping("/{studyId}/applicants")
    public ResponseEntity<Void> applyStudy(@Authorization Member member, @PathVariable Long studyId) {
        studyService.apply(member, studyId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping("/{id}")
    @GetMapping
    public ResponseEntity<StudyDetailResponse> viewStudyDetail(@Authorization Member member, @PathVariable Long id) {
        StudyDetailResponse response = studyService.findStudyDetailByStudyId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recruiting")
    public ResponseEntity<List<RecruitingStudyResponse>> readRecruitingStudies(int page) {
        List<RecruitingStudyResponse> response = studyService.findRecruitingStudies(page);
        return ResponseEntity.ok(response);
    }
}

