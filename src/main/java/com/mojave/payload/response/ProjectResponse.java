package com.mojave.payload.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectResponse {

    Long id;
    String name;
    String description;
    String imageUrl;
    List<MilestoneResponse> milestones = new ArrayList<>();
    List<TagResponse> tags = new ArrayList<>();
}
