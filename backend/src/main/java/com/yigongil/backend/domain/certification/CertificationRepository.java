package com.yigongil.backend.domain.certification;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface CertificationRepository extends Repository<Certification, Long> {

    Certification save(Certification feedPost);

    Optional<Certification> findByRoundIdAndAuthorId(Long roundId, Long memberId);
}
