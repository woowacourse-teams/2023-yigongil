package com.yigongil.backend.domain.certification;

import com.yigongil.backend.config.auth.Authorization;
import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.request.CertificationCreateRequest;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CertificationController implements CertificationApi {

    private final CertificationService certificationService;

    public CertificationController(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    @PostMapping("/studies/{id}/certifications")
    public ResponseEntity<Void> createCertification(
        @Authorization Member member,
        @PathVariable Long id,
        @RequestBody CertificationCreateRequest request
    ) {
        Long certificationId = certificationService.createCertification(member, id, request);
        return ResponseEntity.created(URI.create("/studies/" + id + "/certifications/" + certificationId)).build();
    }
}
