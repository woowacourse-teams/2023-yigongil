package com.yigongil.backend.domain.feedpost.certificationfeedpost;

import java.util.List;
import org.springframework.data.repository.Repository;

public interface CertificationFeedRepository extends Repository<CertificationFeedPost, Long> {

    List<CertificationFeedPost> findAllByIdIn(List<Long> ids);
}
