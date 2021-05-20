package com.mojave.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TagCreateRequest {

    @NotBlank
    String name;

    @NotBlank
    String color;
}
