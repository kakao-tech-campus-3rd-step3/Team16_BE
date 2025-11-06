package com.kakaotechcampus.team16be.admin.service;

import com.kakaotechcampus.team16be.admin.dto.AdminReportResponseDto;
import com.kakaotechcampus.team16be.admin.dto.AdminUserVerificationView;
import com.kakaotechcampus.team16be.aws.service.S3UploadPresignedUrlService;
import com.kakaotechcampus.team16be.report.ReportRepository;
import com.kakaotechcampus.team16be.report.domain.Report;
import com.kakaotechcampus.team16be.report.exception.ReportErrorCode;
import com.kakaotechcampus.team16be.report.exception.ReportException;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.domain.VerificationStatus;
import com.kakaotechcampus.team16be.user.exception.UserErrorCode;
import com.kakaotechcampus.team16be.user.exception.UserException;
import com.kakaotechcampus.team16be.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminPageService {

    private final UserRepository userRepository;
    private final S3UploadPresignedUrlService s3Service;
    private final ReportRepository reportRepository;

    @Transactional(readOnly = true)
    public List<AdminUserVerificationView> getAllVerificationRequests() {
        return userRepository.findAll().stream()
                .map(user -> new AdminUserVerificationView(
                        user.getId(),
                        user.getNickname(),
                        s3Service.getPublicUrl(user.getStudentIdImageUrl()),
                        user.getVerificationStatus().name()
                ))
                .toList();
    }

    @Transactional
    public void updateVerificationStatus(Long userId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        user.updateVerificationStatus(VerificationStatus.valueOf(status));
    }

    @Transactional(readOnly = true)
    public List<AdminReportResponseDto> getAllReportsForAdmin() {
        List<Report> reports = reportRepository.findAll();
        return reports.stream()
                .map(AdminReportResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateReportStatus(Long reportId, String status) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportException(ReportErrorCode.REPORT_NOT_FOUND));
        report.updateStatusAsAdmin(status);
    }

}
