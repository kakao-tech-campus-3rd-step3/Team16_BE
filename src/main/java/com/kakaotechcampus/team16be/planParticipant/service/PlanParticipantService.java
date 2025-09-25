package com.kakaotechcampus.team16be.planParticipant.service;

import com.kakaotechcampus.team16be.planParticipant.dto.PlanParticipantResponseDto;
import java.util.List;

public interface PlanParticipantService {

  void attendPlan(Long userId, Long planId);
  List<PlanParticipantResponseDto> getAllParticipants(Long planId);
  void cancelAttendance(Long userId, Long planId);
}
