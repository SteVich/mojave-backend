package com.mojave.dto;

import com.mojave.dictionary.Role;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {

    Long id;
    String name;
    String username;
    String email;
    Set<Role> roles;
}
