package com.kakaotechcampus.team16be.report;

import com.kakaotechcampus.team16be.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    void deleteAllByTargetId(Long targetId);

    void deleteAllByReporterId(Long reporterId);
}
