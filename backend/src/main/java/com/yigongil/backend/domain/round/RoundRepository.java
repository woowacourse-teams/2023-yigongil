package com.yigongil.backend.domain.round;

import com.yigongil.backend.exception.RoundNotFoundException;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

public interface RoundRepository extends Repository<Round, Long> {

    @EntityGraph(attributePaths = "roundOfMembers")
    Optional<Round> findById(Long id);

    List<Round> findAllByStudyIdAndWeekNumber(Long studyId, Integer weekNumber);

    Round save(Round round);

    List<Round> findByRoundStatusAndDayOfWeek(
        RoundStatus roundStatus,
        DayOfWeek dayOfWeek
    );

    @EntityGraph(attributePaths = "roundOfMembers")
    List<Round> findAllByStudyIdAndWeekNumberIn(Long studyId, List<Integer> weekNumbers);

    void saveAll(Iterable<Round> rounds);

    List<Round> findAllByStudyId(Long studyId);

    Optional<Round> findByStudyIdAndRoundStatus(Long studyId, RoundStatus roundStatus);

    default Round getByStudyIdAndRoundStatus(Long studyId, RoundStatus roundStatus) {
        return findByStudyIdAndRoundStatus(studyId, roundStatus).orElseThrow(() -> new RoundNotFoundException("", -1));
    }
}
