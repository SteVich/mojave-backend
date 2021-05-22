package com.mojave.model;

import com.mojave.dictionary.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProjectRole {

    @EmbeddedId
    UserProjectRoleId id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    Role role;
}
