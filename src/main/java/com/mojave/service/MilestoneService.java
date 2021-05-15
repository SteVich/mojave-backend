package com.mojave.service;

import com.mojave.mapper.MilestoneMapper;
import com.mojave.payload.response.MilestoneResponse;
import com.mojave.repository.MilestoneRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MilestoneService {

    MilestoneRepository milestoneRepository;
    MilestoneMapper mapper;

    @Transactional(readOnly = true)
    public List<MilestoneResponse> getAllMilestonesForProject(Long projectId) {
        return mapper.toResponseList(milestoneRepository.findAllByProjectId(projectId));
    }
}
