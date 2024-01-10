package com.yigongil.backend.query.certification;

import com.yigongil.backend.domain.certification.Certification;
import com.yigongil.backend.domain.certification.CertificationRepository;
import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.round.RoundOfMember;
import com.yigongil.backend.domain.round.RoundRepository;
import com.yigongil.backend.domain.round.RoundStatus;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyRepository;
import com.yigongil.backend.response.CertificationResponse;
import com.yigongil.backend.response.MembersCertificationResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CertificationQueryService {

    private final CertificationRepository certificationRepository;
    private final StudyRepository studyRepository;
    private final RoundRepository roundRepository;


    public CertificationQueryService(
        CertificationRepository certificationRepository,
        StudyRepository studyRepository,
        RoundRepository roundRepository
    ) {
        this.certificationRepository = certificationRepository;
        this.studyRepository = studyRepository;
        this.roundRepository = roundRepository;
    }

    public CertificationResponse findCertification(Long roundId, Long memberId) {
        Certification certification = certificationRepository.getByRoundIdAndAuthorId(roundId, memberId);
        return CertificationResponse.from(certification);
    }

    public MembersCertificationResponse findAllMembersCertification(Member member, Long studyId) {
        Study study = studyRepository.getById(studyId);
        Round round = roundRepository.getByStudyIdAndRoundStatus(studyId, RoundStatus.IN_PROGRESS);
        final List<RoundOfMember> roundOfMembers = round.getRoundOfMembers();
        return MembersCertificationResponse.of(study.getName(), round, member, roundOfMembers);
    }
}
