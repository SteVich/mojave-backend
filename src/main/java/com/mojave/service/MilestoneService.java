package com.mojave.service;

import com.mojave.exception.ResourceNotFoundException;
import com.mojave.mapper.MilestoneMapper;
import com.mojave.model.Milestone;
import com.mojave.model.Project;
import com.mojave.payload.response.MilestoneResponse;
import com.mojave.repository.MilestoneRepository;
import com.mojave.repository.ProjectRepository;
import com.mojave.repository.TaskRepository;
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
    ProjectRepository projectRepository;
    TaskRepository taskRepository;
    MilestoneMapper mapper;

    @Transactional(readOnly = true)
    public List<MilestoneResponse> getAllMilestonesForProject(Long projectId) {
        return mapper.toResponseList(milestoneRepository.findAllByProjectId(projectId));
    }

    @Transactional
    public Long createMilestone(Long projectId, String name) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "project id", projectId));

        Milestone milestone = new Milestone();
        milestone.setProject(project);
        milestone.setName(name);

        return milestoneRepository.save(milestone).getId();
    }

    @Transactional
    public boolean deleteMilestone(Long projectId, Long milestoneId) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "project id", milestoneId));

        boolean result = taskRepository.findAllByMilestone(milestone).isEmpty();
        if (result) {
            milestoneRepository.delete(milestone);
        }
        return result;
    }
}
