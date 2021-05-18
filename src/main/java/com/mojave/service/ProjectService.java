package com.mojave.service;

import com.mojave.exception.ResourceNotFoundException;
import com.mojave.mapper.ProjectMapper;
import com.mojave.model.Project;
import com.mojave.payload.response.ProjectResponse;
import com.mojave.repository.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectService {

    ProjectRepository projectRepository;
    ProjectMapper projectMapper;

    @Transactional(readOnly = true)
    public ProjectResponse getResponseById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "project id", projectId));

        return projectMapper.toResponse(project);
    }

    @Transactional
    public void updateName(Long projectId, String name) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "project id", projectId));
        project.setName(name);
        projectRepository.save(project);
    }

    @Transactional
    public void updateDescription(Long projectId, String description) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "project id", projectId));
        project.setDescription(description);
        projectRepository.save(project);
    }

    @Transactional
    public void updateImageUrl(Long projectId, String imageUrl) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "project id", projectId));
        project.setImageUrl(imageUrl);
        projectRepository.save(project);
    }

}
