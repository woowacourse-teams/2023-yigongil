package com.yigongil.backend.domain.applicant;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.study.Study;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ApplicantRepository extends Repository<Applicant, Long> {

    Applicant save(Applicant applicant);

    boolean existsByMemberAndStudy(Member member, Study study);

    List<Applicant> findAllByStudy(Study study);
}
