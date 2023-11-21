package com.yigongil.backend.domain.certification;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.round.RoundRepository;
import com.yigongil.backend.domain.round.RoundStatus;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyRepository;
import com.yigongil.backend.exception.RoundNotFoundException;
import com.yigongil.backend.request.CertificationCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final StudyRepository studyRepository;
    private final RoundRepository roundRepository;

    public CertificationService(CertificationRepository certificationRepository, StudyRepository studyRepository, RoundRepository roundRepository) {
        this.certificationRepository = certificationRepository;
        this.studyRepository = studyRepository;
        this.roundRepository = roundRepository;
    }

    @Transactional
    public Long createCertification(Member member, Long studyId, CertificationCreateRequest request) {
        Study study = studyRepository.getById(studyId);
        return createCertification(study, member, request).getId();
    }

    private Certification createCertification(Study study, Member member, CertificationCreateRequest request) {
        Round round = roundRepository.findByStudyIdAndRoundStatus(study.getId(), RoundStatus.IN_PROGRESS)
                                     .orElseThrow(() -> new RoundNotFoundException("", -1L));
        Certification feedPost = Certification.builder()
                                              .author(member)
                                              .study(study)
                                              .round(round)
                                              .content(request.content())
                                              .imageUrl(request.imageUrl())
                                              .build();
        round.completeRound(member);
        return certificationRepository.save(feedPost);
    }
}
