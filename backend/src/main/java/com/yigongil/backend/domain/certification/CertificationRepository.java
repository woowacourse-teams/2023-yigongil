package com.yigongil.backend.domain.certification;

import com.yigongil.backend.exception.NoCertificationException;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface CertificationRepository extends Repository<Certification, Long> {

    Certification save(Certification feedPost);

    Optional<Certification> findByRoundIdAndAuthorId(Long roundId, Long memberId);

    default Certification getByRoundIdAndAuthorId(Long roundId, Long memberId) {
        return findByRoundIdAndAuthorId(roundId, memberId).orElseThrow(() -> new NoCertificationException("인증을 찾을 수 없습니다", String.valueOf(memberId)));
    }
}
