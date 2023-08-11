package com.yigongil.backend.application;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.report.Report;
import com.yigongil.backend.domain.report.ReportRepository;
import com.yigongil.backend.request.ReportCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberService memberService;

    public ReportService(ReportRepository reportRepository, MemberService memberService) {
        this.reportRepository = reportRepository;
        this.memberService = memberService;
    }

    @Transactional
    public void report(Member reporter, ReportCreateRequest request) {
        Member reportedMember = memberService.findMemberById(request.reportedMemberId());
        reportRepository.save(Report.builder()
                                    .reporter(reporter)
                                    .reported(reportedMember)
                                    .title(request.title())
                                    .content(request.content())
                                    .problemOccurredAt(request.problemOccuredAt())
                                    .build());
    }
}
