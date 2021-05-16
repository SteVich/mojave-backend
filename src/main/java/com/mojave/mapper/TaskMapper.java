package com.mojave.mapper;

import com.mojave.model.Task;
import com.mojave.payload.request.TaskUpdateRequest;
import com.mojave.payload.response.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(TaskUpdateRequest request);

    TaskResponse toResponse(Task task);

    @Mapping(target = "positionInColumn", ignore = true)
    void updateEntityFromRequest(TaskUpdateRequest request, @MappingTarget Task task);
}
