package com.yigongil.backend.infra;

import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.Repository;

@Profile(value = {"dev", "prod"})
public interface FirebaseTokenRepository extends Repository<FirebaseToken, Long>, TokenRepository {

    void save(FirebaseToken firebaseToken);

    List<FirebaseToken> findAllByMemberId(Long memberId);
}
