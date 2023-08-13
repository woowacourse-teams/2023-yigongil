package com.yigongil.backend.ui;

import com.yigongil.backend.application.ReportService;
import com.yigongil.backend.config.auth.Authorization;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.request.MemberReportCreateRequest;
import com.yigongil.backend.request.StudyReportCreateRequest;
import com.yigongil.backend.ui.doc.ReportApi;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/reports")
@RestController
public class ReportController implements ReportApi {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/by-member")
    public ResponseEntity<Void> createMemberReport(
            @Authorization Member reporter,
            @RequestBody @Valid MemberReportCreateRequest request
    ) {
        reportService.reportMember(reporter, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/by-study")
    public ResponseEntity<Void> createStudyReport(
            @Authorization Member reporter,
            @RequestBody @Valid StudyReportCreateRequest request
    ) {
        reportService.reportStudy(reporter, request);
        return ResponseEntity.ok().build();
    }
}
