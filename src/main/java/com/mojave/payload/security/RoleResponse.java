package com.mojave.payload.security;

import com.mojave.dictionary.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {

    Long projectId;
    Role role;
}
