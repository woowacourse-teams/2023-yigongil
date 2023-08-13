package com.yigongil.backend.ui;

import com.yigongil.backend.application.ReportService;
import com.yigongil.backend.config.auth.Authorization;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.request.ReportCreateRequest;
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

    @PostMapping
    public ResponseEntity<Void> createReport(
            @Authorization Member reporter,
            @RequestBody @Valid ReportCreateRequest request
    ) {
        reportService.report(reporter, request);
        return ResponseEntity.ok().build();
    }
}
