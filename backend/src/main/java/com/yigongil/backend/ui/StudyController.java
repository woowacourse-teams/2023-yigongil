package com.yigongil.backend.ui;

import com.yigongil.backend.application.StudyService;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.request.StudyCreateRequest;
import com.yigongil.backend.response.StudyDetailResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequestMapping("/v1/studies")
@RestController
public class StudyController {

    private final StudyService studyService;

    public StudyController(StudyService studyService) {
        this.studyService = studyService;
    }

    @PostMapping
    public ResponseEntity<Void> createStudy(Member member, @RequestBody StudyCreateRequest request) {
        Long studyId = studyService.create(member, request);
        return ResponseEntity.created(URI.create("/v1/studies/" + studyId)).build();
    }

    @RequestMapping("/{id}")
    @GetMapping
    public ResponseEntity<StudyDetailResponse> viewStudyDetail(Member member, @PathVariable Long id) {
        StudyDetailResponse response = studyService.findStudyDetailByStudyId(id);
        return ResponseEntity.ok(response);
    }
}
