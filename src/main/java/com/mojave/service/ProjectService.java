package com.mojave.service;

import com.mojave.exception.ResourceNotFoundException;
import com.mojave.mapper.ProjectMapper;
import com.mojave.model.Project;
import com.mojave.model.User;
import com.mojave.payload.request.ProjectCreateRequest;
import com.mojave.payload.response.ProjectResponse;
import com.mojave.repository.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectService {

    ProjectRepository projectRepository;
    MilestoneService milestoneService;
    TagService tagService;
    UserService userService;
    BoardService boardService;
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

    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjectsForUser() {
        User currentUser = userService.getCurrentUser();
        return projectRepository.getAllByUsersIn(Collections.singleton(currentUser)).stream()
                .map(projectMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectResponse create(ProjectCreateRequest request) {
        Project project = projectMapper.toEntity(request);
        project.setMilestones(null);
        project.setTags(null);

        Project savedProject = projectRepository.save(project);

        request.getMilestones().forEach(milestoneRequest -> {
            milestoneService.createMilestone(savedProject.getId(), milestoneRequest.getName());
        });

        request.getTags().forEach(tagCreateRequest -> {
            tagService.createTag(savedProject.getId(), tagCreateRequest.getName(), tagCreateRequest.getColor());
        });

        boardService.create(project);

        userService.addProjectToUser(project);

        return projectMapper.toResponse(savedProject);
    }
}
