package com.mojave.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ProjectCreateRequest {

    @NotBlank
    String name;

    String description;

    String imageUrl;

    @NotNull
    List<MilestoneCreateRequest> milestones;

    @NotNull
    List<TagCreateRequest> tags;
}
