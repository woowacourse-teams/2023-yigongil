package com.yigongil.backend.infra;

import java.util.List;

public interface TokenRepository {

    void save(FirebaseToken firebaseToken);

    List<FirebaseToken> findAllByMemberId(Long memberId);
}
