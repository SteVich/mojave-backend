package com.mojave.mapper;

import com.mojave.model.Task;
import com.mojave.payload.request.TaskUpdateRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toTask(TaskUpdateRequest request);
}
