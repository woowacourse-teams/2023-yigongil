package com.yigongil.backend.domain.certification;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyRepository;
import com.yigongil.backend.request.CertificationCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final StudyRepository studyRepository;

    public CertificationService(CertificationRepository certificationRepository, StudyRepository studyRepository) {
        this.certificationRepository = certificationRepository;
        this.studyRepository = studyRepository;
    }

    @Transactional
    public Long createCertification(Member member, Long studyId, CertificationCreateRequest request) {
        Study study = studyRepository.getById(studyId);
        return createCertification(study, member, request).getId();
    }

    private Certification createCertification(Study study, Member member, CertificationCreateRequest request) {
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
}
