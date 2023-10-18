package com.yigongil.backend.domain.certification;

import org.springframework.data.repository.Repository;

public interface CertificationRepository extends Repository<Certification, Long> {

    Certification save(Certification feedPost);

    Certification findById(Long id);

    Certification findByRoundIdAndAuthorId(Long roundId, Long memberId);
}
