package com.kakaotechcampus.team16be.planParticipant.service;

import com.kakaotechcampus.team16be.planParticipant.dto.PlanParticipantResponseDto;
import java.util.List;

public interface PlanParticipantService {

  Long attendPlan(Long userId, Long planId);
  List<PlanParticipantResponseDto> getAllParticipants(Long planId);
  void withdrawAttendance(Long userId, Long planId);
}
