package com.yigongil.backend.domain.round;

import java.util.List;
import java.util.Map;

public interface RoundOfMemberBatchRepository {
    void batchSaveAll(Map<Long, List<RoundOfMember>> roundOfMembers);
}
