package com.yigongil.backend.application;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.report.MemberReport;
import com.yigongil.backend.domain.report.ReportRepository;
import com.yigongil.backend.domain.report.StudyReport;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.request.MemberReportCreateRequest;
import com.yigongil.backend.request.StudyReportCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberService memberService;
    private final StudyService studyService;

    public ReportService(
            ReportRepository reportRepository,
            MemberService memberService,
            StudyService studyService
    ) {
        this.reportRepository = reportRepository;
        this.memberService = memberService;
        this.studyService = studyService;
    }

    public void reportMember(Member reporter, MemberReportCreateRequest request) {
        Member reportedMember = memberService.findMemberById(request.reportedMemberId());
        reportRepository.save(MemberReport.builder()
                                                .reporter(reporter)
                                                .reportedMember(reportedMember)
                                                .title(request.title())
                                                .content(request.content())
                                                .problemOccurredAt(request.problemOccurredAt())
                                                .build());
    }

    public void reportStudy(Member reporter, StudyReportCreateRequest request) {
        Study reportedStudy = studyService.findStudyById(request.reportedStudyId());
        reportRepository.save(StudyReport.builder()
                                               .reporter(reporter)
                                               .reportedStudy(reportedStudy)
                                               .title(request.title())
                                               .content(request.content())
                                               .problemOccurredAt(request.problemOccurredAt())
                                               .build());
    }
}
