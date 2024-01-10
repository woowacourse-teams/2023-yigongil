package com.yigongil.backend.query.certification;

import com.yigongil.backend.config.auth.Authorization;
import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.response.CertificationResponse;
import com.yigongil.backend.response.MembersCertificationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CertificationQueryController implements CertificationQueryApi {

    private final CertificationQueryService certificationQueryService;

    public CertificationQueryController(CertificationQueryService certificationQueryService) {
        this.certificationQueryService = certificationQueryService;
    }

    @GetMapping("/studies/{id}/rounds/{roundId}/members/{memberId}")
    public ResponseEntity<CertificationResponse> findMemberCertification(
        @PathVariable Long roundId,
        @PathVariable Long memberId
    ) {
        CertificationResponse response = certificationQueryService.findCertification(roundId, memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/studies/{id}/certifications")
    public ResponseEntity<MembersCertificationResponse> findAllMembersCertification(
        @Authorization Member member,
        @PathVariable Long id
    ) {
        MembersCertificationResponse response = certificationQueryService.findAllMembersCertification(member, id);
        return ResponseEntity.ok(response);
    }
}
