package com.yigongil.backend.fixture;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.domain.study.ProcessingStatus;
import com.yigongil.backend.domain.study.Study;
import java.time.LocalDateTime;

public enum StudyFixture {

    자바_스터디_모집중(null, LocalDateTime.now(), "자바", "스터디소개", ProcessingStatus.RECRUITING, 4, 2, 4),
    자바_스터디_모집중_정원_2(null, LocalDateTime.now(), "자바", "스터디소개", ProcessingStatus.RECRUITING, 2, 2, 4);

    private final Long id;
    private final String name;
    private final String introduction;
    private final ProcessingStatus processingStatus;
    private final Integer numberOfMaximumMember;
    private final Integer meetingDaysPerWeek;
    private final Integer minimumWeeks;

    StudyFixture(Long id, LocalDateTime startAt, String name, String introduction,
            ProcessingStatus processingStatus, Integer numberOfMaximumMember, Integer meetingDaysPerWeek, Integer minimumWeeks) {
        this.id = id;
        this.name = name;
        this.introduction = introduction;
        this.processingStatus = processingStatus;
        this.numberOfMaximumMember = numberOfMaximumMember;
        this.meetingDaysPerWeek = meetingDaysPerWeek;
        this.minimumWeeks = minimumWeeks;
    }

    public Study toStudy() {
        return Study.builder()
                    .id(id)
                    .name(name)
                    .introduction(introduction)
                    .master(MemberFixture.김진우.toMember())
                    .processingStatus(processingStatus)
                    .numberOfMaximumMembers(numberOfMaximumMember)
                    .meetingDaysCountPerWeek(meetingDaysPerWeek)
                    .minimumWeeks(minimumWeeks)
                    .build();
    }

    public Study toStudyWithMaster(Member master) {
        return Study.builder()
                    .id(id)
                    .name(name)
                    .introduction(introduction)
                    .master(master)
                    .processingStatus(processingStatus)
                    .numberOfMaximumMembers(numberOfMaximumMember)
                    .meetingDaysCountPerWeek(meetingDaysPerWeek)
                    .minimumWeeks(minimumWeeks)
                    .build();
    }
}
