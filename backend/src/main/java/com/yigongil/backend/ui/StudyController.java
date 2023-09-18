package com.yigongil.backend.ui;

import com.yigongil.backend.application.StudyService;
import com.yigongil.backend.config.auth.Authorization;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.request.StudyUpdateRequest;
import com.yigongil.backend.response.MyStudyResponse;
import com.yigongil.backend.response.RecruitingStudyResponse;
import com.yigongil.backend.response.StudyDetailResponse;
import com.yigongil.backend.response.StudyMemberResponse;
import com.yigongil.backend.response.StudyMemberRoleResponse;
import com.yigongil.backend.ui.doc.StudyApi;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/studies")
@RestController
public class StudyController implements StudyApi {

    private final StudyService studyService;

    public StudyController(StudyService studyService) {
        this.studyService = studyService;
    }

    @PostMapping
    public ResponseEntity<Void> createStudy(
            @Authorization Member member,
            @RequestBody @Valid StudyUpdateRequest request
    ) {
        Long studyId = studyService.create(member, request);
        return ResponseEntity.created(URI.create("/v1/studies/" + studyId)).build();
    }

    @PutMapping("/{studyId}")
    public ResponseEntity<Void> updateStudy(
            @Authorization Member member,
            @PathVariable Long studyId,
            @RequestBody @Valid StudyUpdateRequest request
    ) {
        studyService.update(member, studyId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{studyId}/applicants")
    public ResponseEntity<Void> applyStudy(
            @Authorization Member member,
            @PathVariable Long studyId
    ) {
        studyService.apply(member, studyId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{studyId}/applicants/{memberId}")
    public ResponseEntity<Void> permitApplicant(
            @Authorization Member master,
            @PathVariable Long studyId,
            @PathVariable Long memberId
    ) {
        studyService.permitApplicant(master, studyId, memberId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{studyId}/applicants")
    public ResponseEntity<Void> deleteApplicant(
            @Authorization Member member,
            @PathVariable Long studyId
    ) {
        studyService.deleteApplicant(member, studyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudyDetailResponse> viewStudyDetail(@PathVariable Long id) {
        StudyDetailResponse response = studyService.findStudyDetailByStudyId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recruiting")
    public ResponseEntity<List<RecruitingStudyResponse>> findRecruitingStudies(int page) {
        List<RecruitingStudyResponse> response = studyService.findRecruitingStudies(page);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recruiting/search")
    public ResponseEntity<List<RecruitingStudyResponse>> findRecruitingStudiesWithSearch(
            int page,
            @RequestParam(name = "q") String word
    ) {
        List<RecruitingStudyResponse> response = studyService.findRecruitingStudiesWithSearch(page, word);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/applicants")
    public ResponseEntity<List<StudyMemberResponse>> findApplicantOfStudy(
            @PathVariable Long id,
            @Authorization Member master
    ) {
        List<StudyMemberResponse> applicants = studyService.findApplicantsOfStudy(id, master);
        return ResponseEntity.ok(applicants);
    }

    @GetMapping("/my")
    public ResponseEntity<List<MyStudyResponse>> findMyStudies(@Authorization Member member) {
        List<MyStudyResponse> myStudies = studyService.findMyStudies(member);
        return ResponseEntity.ok(myStudies);
    }

    @PatchMapping("/{id}/start")
    public ResponseEntity<Void> startStudy(@Authorization Member member, @PathVariable Long id) {
        studyService.startStudy(member, id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{studyId}/members/role")
    public ResponseEntity<StudyMemberRoleResponse> getStudyMemberRole(
            @Authorization Member member,
            @PathVariable Long studyId
    ) {
        StudyMemberRoleResponse response = studyService.getMemberRoleOfStudy(member, studyId);
        return ResponseEntity.ok(response);
    }
}

