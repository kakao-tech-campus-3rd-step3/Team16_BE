package com.kakaotechcampus.team16be.report;

import com.kakaotechcampus.team16be.report.domain.Report;
import com.kakaotechcampus.team16be.report.domain.TargetType;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByTargetTypeAndTargetIdAndCreatedAtBetween(TargetType targetType, Long targetId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
