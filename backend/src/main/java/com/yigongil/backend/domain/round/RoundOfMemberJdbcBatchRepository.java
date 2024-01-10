package com.yigongil.backend.domain.round;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class RoundOfMemberJdbcBatchRepository implements RoundOfMemberBatchRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public RoundOfMemberJdbcBatchRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void batchSaveAll(Map<Long, List<RoundOfMember>> roundOfMembersMap) {
        String sql = """
            INSERT INTO round_of_member (round_id, member_id, is_done, created_at /* other columns */) 
            VALUES (:roundId, :memberId, :isDone, :createdAt /* other values */)
            """;

        List<SqlParameterSource> parameters = new ArrayList<>();
        for (Map.Entry<Long, List<RoundOfMember>> entry : roundOfMembersMap.entrySet()) {
            Long roundId = entry.getKey();
            for (RoundOfMember roundOfMember : entry.getValue()) {
                SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("roundId", roundId)
                    .addValue("memberId", roundOfMember.getMember().getId())
                    .addValue("isDone", roundOfMember.isDone())
                    .addValue("createdAt", LocalDateTime.now());

                // Add other parameters as needed
                parameters.add(param);
            }
        }

        SqlParameterSource[] batch = new SqlParameterSource[parameters.size()];
        batch = parameters.toArray(batch);

        namedParameterJdbcTemplate.batchUpdate(sql, batch);
    }

}
