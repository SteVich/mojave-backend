package com.mojave.service;

import com.mojave.exception.ResourceNotFoundException;
import com.mojave.mapper.TaskMapper;
import com.mojave.model.BoardColumn;
import com.mojave.model.History;
import com.mojave.model.Milestone;
import com.mojave.model.Project;
import com.mojave.model.Tag;
import com.mojave.model.Task;
import com.mojave.model.User;
import com.mojave.payload.request.TaskUpdateRequest;
import com.mojave.payload.response.TaskResponse;
import com.mojave.repository.BoardColumnRepository;
import com.mojave.repository.MilestoneRepository;
import com.mojave.repository.ProjectRepository;
import com.mojave.repository.TagRepository;
import com.mojave.repository.TaskRepository;
import com.mojave.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TaskService {

    TaskRepository taskRepository;
    BoardColumnRepository boardColumnRepository;
    TagRepository tagRepository;
    MilestoneRepository milestoneRepository;
    UserRepository userRepository;
    ProjectRepository projectRepository;

    UserService userService;
    TaskMapper taskMapper;

    @Transactional
    public TaskResponse create(Long projectId, Long columnId, TaskUpdateRequest request) {
        Task task = initCrateTask(projectId, columnId, request);
        Task createdTask = taskRepository.save(task);

        return taskMapper.toResponse(createdTask);
    }

    @Transactional
    public TaskResponse update(Long projectId, Long columnId, Long taskId, TaskUpdateRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        initUpdateTask(projectId, columnId, task, request);
        Task createdTask = taskRepository.save(task);

        return taskMapper.toResponse(createdTask);
    }

    private Task initCrateTask(Long projectId, Long columnId, TaskUpdateRequest request) {
        Task task = taskMapper.toEntity(request);

        BoardColumn boardColumn = boardColumnRepository.findById(columnId)
                .orElseThrow(() -> new ResourceNotFoundException("Column", "id", columnId));
        task.setBoardColumn(boardColumn);

        User assignee = userRepository.findById(request.getAssigneeId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getAssigneeId()));
        task.setAssignee(assignee);

        Tag tag = tagRepository.findById(request.getTagId())
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", request.getTagId()));
        task.setTag(tag);

        Milestone milestone = milestoneRepository.findById(request.getMilestoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Milestone", "id", request.getMilestoneId()));
        task.setMilestone(milestone);

        History history = getTaskHistory(projectId, "created the task");
        task.setHistories(Collections.singleton(history));

        return task;
    }

    private void initUpdateTask(Long projectId, Long columnId, Task task, TaskUpdateRequest request) {
        taskMapper.updateEntityFromRequest(request, task);

        if (!task.getAssignee().getId().equals(request.getAssigneeId())) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getAssigneeId()));
            task.setAssignee(assignee);

            History history = getTaskHistory(projectId, "reassigned the task on " + assignee.getUsername());
            task.getHistories().add(history);
        }

        if (!task.getTag().getId().equals(request.getTagId())) {
            Tag tag = tagRepository.findById(request.getTagId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", request.getTagId()));
            task.setTag(tag);

            History history = getTaskHistory(projectId, "changed the task tag on " + tag.getName());
            task.getHistories().add(history);
        }

        if (!task.getMilestone().getId().equals(request.getMilestoneId())) {
            Milestone milestone = milestoneRepository.findById(request.getMilestoneId())
                    .orElseThrow(() -> new ResourceNotFoundException("Milestone", "id", request.getMilestoneId()));
            task.setMilestone(milestone);

            History history = getTaskHistory(projectId, "changed milestone for the task on " + milestone.getName());
            task.getHistories().add(history);
        }
    }

    private History getTaskHistory(Long projectId, String description) {
        History history = new History();
        history.setDescription(description);
        history.setDate(LocalDateTime.now());

        User currentUser = userRepository.findById(userService.getCurrentUserDto().getId()).orElse(null);
        history.setUser(currentUser);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
        history.setProject(project);

        return history;
    }

    @Transactional
    public void changeColumn(Long projectId, Long columnId, Long taskId, Integer taskPosition) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        BoardColumn boardColumn = boardColumnRepository.findById(columnId)
                .orElseThrow(() -> new ResourceNotFoundException("Column", "id", columnId));

        History history = getTaskHistory(projectId, String.format("moved task from %s to %s", task.getBoardColumn().getName(), boardColumn.getName()));
        task.getHistories().add(history);

        task.setBoardColumn(boardColumn);
        task.setPositionInColumn(taskPosition);

        taskRepository.save(task);
    }

    @Transactional
    public void changeTasksPositionsInColumns(Long projectId, Long columnId, List<Long> taskIdsToSetPositions) {
        List<Task> tasks = taskRepository.findAllByIdIn(taskIdsToSetPositions);

        taskIdsToSetPositions.forEach(id ->
                tasks.stream()
                        .filter(task -> task.getId().equals(id))
                        .forEach(task -> task.setPositionInColumn(taskIdsToSetPositions.indexOf(id)))
        );

        taskRepository.saveAll(tasks);
    }

    @Transactional
    public void delete(Long columnId, Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
