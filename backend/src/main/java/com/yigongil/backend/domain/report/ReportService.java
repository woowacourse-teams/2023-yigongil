package com.yigongil.backend.domain.report;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.domain.member.domain.MemberRepository;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyService;
import com.yigongil.backend.request.MemberReportCreateRequest;
import com.yigongil.backend.request.StudyReportCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final StudyService studyService;

    public ReportService(
            ReportRepository reportRepository,
            MemberRepository memberRepository,
            StudyService studyService
    ) {
        this.reportRepository = reportRepository;
        this.memberRepository = memberRepository;
        this.studyService = studyService;
    }

    @Transactional
    public void reportMember(Member reporter, MemberReportCreateRequest request) {
        Member reportedMember = memberRepository.getById(request.reportedMemberId());
        reportRepository.save(
                MemberReport.builder()
                            .reporter(reporter)
                            .reportedMember(reportedMember)
                            .title(request.title())
                            .content(request.content())
                            .problemOccurredAt(request.problemOccurredAt())
                            .build()
        );
    }

    @Transactional
    public void reportStudy(Member reporter, StudyReportCreateRequest request) {
        Study reportedStudy = studyService.findStudyById(request.reportedStudyId());
        reportRepository.save(
                StudyReport.builder()
                           .reporter(reporter)
                           .reportedStudy(reportedStudy)
                           .title(request.title())
                           .content(request.content())
                           .problemOccurredAt(request.problemOccurredAt())
                           .build()
        );
    }
}
