package com.yigongil.backend.ui;

import com.yigongil.backend.application.StudyService;
import com.yigongil.backend.config.auth.Authorization;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.study.ProcessingStatus;
import com.yigongil.backend.request.CertificationCreateRequest;
import com.yigongil.backend.request.FeedPostCreateRequest;
import com.yigongil.backend.request.StudyStartRequest;
import com.yigongil.backend.request.StudyUpdateRequest;
import com.yigongil.backend.response.CertificationResponse;
import com.yigongil.backend.response.FeedPostResponse;
import com.yigongil.backend.response.MembersCertificationResponse;
import com.yigongil.backend.response.MyStudyResponse;
import com.yigongil.backend.response.StudyDetailResponse;
import com.yigongil.backend.response.StudyListItemResponse;
import com.yigongil.backend.response.StudyMemberResponse;
import com.yigongil.backend.response.StudyMemberRoleResponse;
import com.yigongil.backend.ui.doc.StudyApi;
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

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequestMapping("/studies")
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
        return ResponseEntity.created(URI.create("/studies/" + studyId)).build();
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

    @GetMapping
    public ResponseEntity<List<StudyListItemResponse>> findStudies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "all") ProcessingStatus status
    ) {
        List<StudyListItemResponse> response = studyService.findStudies(page, search, status);
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
    public ResponseEntity<Void> startStudy(
            @Authorization Member member,
            @PathVariable Long id,
            @RequestBody StudyStartRequest studyStartRequest
    ) {
        studyService.start(member, id, studyStartRequest);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/feeds")
    public ResponseEntity<List<FeedPostResponse>> findFeedPosts(
            @PathVariable Long id,
            @RequestParam Optional<Long> oldestFeedPostId
    ) {
        List<FeedPostResponse> response = studyService.findFeedPosts(
                id,
                oldestFeedPostId.orElse(Long.MAX_VALUE)
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/feeds")
    public ResponseEntity<Void> createFeedPost(
            @Authorization Member member,
            @PathVariable Long id,
            @RequestBody FeedPostCreateRequest request
    ) {
        studyService.createFeedPost(member, id, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/certifications")
    public ResponseEntity<Void> createCertification(
            @Authorization Member member,
            @PathVariable Long id,
            @RequestBody CertificationCreateRequest request
    ) {
        Long certificationId = studyService.createCertification(member, id, request);
        return ResponseEntity.created(URI.create("/studies/" + id + "/certifications/" + certificationId)).build();
    }

    @GetMapping("/{id}/certifications")
    public ResponseEntity<MembersCertificationResponse> findAllMembersCertification(
            @Authorization Member member,
            @PathVariable Long id
    ) {
        MembersCertificationResponse response = studyService.findAllMembersCertification(member, id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/certifications/{certificationId}")
    public ResponseEntity<CertificationResponse> findMemberCertification(
            @PathVariable Long certificationId
    ) {
        CertificationResponse response = studyService.findCertification(certificationId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{studyId}/members/role")
    public ResponseEntity<StudyMemberRoleResponse> getStudyMemberRole(
            @Authorization Member member,
            @PathVariable Long studyId
    ) {
        StudyMemberRoleResponse response = studyService.getMemberRoleOfStudy(member, studyId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/applied")
    public ResponseEntity<List<StudyListItemResponse>> findAppliedStudies(
            @Authorization Member member,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search
    ) {
        List<StudyListItemResponse> response = studyService.findAppliedStudies(member, page, search);
        return ResponseEntity.ok(response);

    @PatchMapping("/{studyId}/end")
    public ResponseEntity<Void> endStudy(
            @Authorization Member member,
            @PathVariable Long studyId
    ) {
        studyService.finish(member, studyId);
        return ResponseEntity.ok().build();
    }
}

