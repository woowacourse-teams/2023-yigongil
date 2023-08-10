package com.yigongil.backend.domain.report;

import org.springframework.data.repository.Repository;

public interface ReportRepository extends Repository<Report, Long> {

    Report save(Report report);
}
