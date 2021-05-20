package com.mojave.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MilestoneCreateRequest {

    @NotBlank
    String name;
}
