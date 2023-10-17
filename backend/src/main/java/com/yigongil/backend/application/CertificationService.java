package com.yigongil.backend.application;

import com.yigongil.backend.domain.certification.Certification;
import com.yigongil.backend.domain.certification.CertificationRepository;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.request.CertificationCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CertificationService {

    private final CertificationRepository certificationRepository;

    public CertificationService(CertificationRepository certificationRepository) {
        this.certificationRepository = certificationRepository;
    }

    @Transactional
    public Certification createCertification(Study study, Member member, CertificationCreateRequest request) {
        Certification feedPost = Certification.builder()
                                              .author(member)
                                              .study(study)
                                              .round(study.getCurrentRound())
                                              .content(request.content())
                                              .imageUrl(request.imageUrl())
                                              .build();
        study.completeRound(member);
        return certificationRepository.save(feedPost);
    }

    @Transactional(readOnly = true)
    public Certification findById(Long id) {
        return certificationRepository.findById(id);
    }
}