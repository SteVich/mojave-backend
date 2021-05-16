package com.mojave.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mojave.dictionary.Priority;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskUpdateRequest {

    @NotNull
    String title;

    @NotNull
    LocalDateTime dueDate;

    @NotNull
    Priority priority;

    @NotNull
    @JsonProperty("assignee")
    Long assigneeId;

    @NotNull
    @JsonProperty("milestone")
    Long milestoneId;

    @NotNull
    @JsonProperty("tag")
    Long tagId;

    @NotNull
    Integer positionInColumn;

    Double estimate;

    String description;
}
