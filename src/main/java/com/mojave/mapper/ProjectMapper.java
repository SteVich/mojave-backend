package com.mojave.mapper;

import com.mojave.model.Project;
import com.mojave.payload.request.ProjectCreateRequest;
import com.mojave.payload.response.ProjectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectResponse toResponse(Project project);

    Project toEntity(ProjectCreateRequest request);
}
