package com.mojave.service;

import com.mojave.mapper.TaskMapper;
import com.mojave.model.Task;
import com.mojave.payload.request.TaskUpdateRequest;
import com.mojave.repository.TaskRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TaskService {

    TaskRepository taskRepository;
    TaskMapper taskMapper;

    @Transactional
    public void create(Long columnId, TaskUpdateRequest request) {
        Task task = taskMapper.toTask(request);

        task.setBoardColumn(null);
    }
}
