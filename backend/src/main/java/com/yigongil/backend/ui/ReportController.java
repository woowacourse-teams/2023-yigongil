package com.yigongil.backend.ui;

import com.yigongil.backend.application.ReportService;
import com.yigongil.backend.config.auth.Authorization;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.request.ReportCreateRequest;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/reports")
@RestController
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/{reportedMemberId}")
    public ResponseEntity<Void> createReport(
            @Authorization Member reporter,
            @PathVariable Long reportedMemberId,
            @RequestBody @Valid ReportCreateRequest request
    ) {
        reportService.report(reporter, reportedMemberId, request);
        return ResponseEntity.ok().build();
    }
}
