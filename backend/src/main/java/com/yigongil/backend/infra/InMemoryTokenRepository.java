package com.yigongil.backend.infra;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Profile(value = {"test", "local"})
@Repository
public class InMemoryTokenRepository implements TokenRepository {

    private final List<FirebaseToken> firebaseTokens;

    public InMemoryTokenRepository() {
        this.firebaseTokens = new ArrayList<>();
    }

    @Override
    public void save(FirebaseToken firebaseToken) {
        firebaseTokens.add(firebaseToken);
    }

    @Override
    public List<FirebaseToken> findAllByMemberId(Long memberId) {
        return firebaseTokens.stream()
                             .filter(firebaseToken -> firebaseToken.getMember().getId().equals(memberId))
                             .toList();
    }
}
