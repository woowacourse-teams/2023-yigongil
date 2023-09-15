package com.yigongil.backend.domain.feedpost.certificationfeedpost;

import org.springframework.data.repository.Repository;

public interface CertificationRepository extends Repository<Certification, Long> {

    Certification save(Certification feedPost);

    Certification findById(Long id);
}
