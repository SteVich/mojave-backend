package com.mojave.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@Setter
public class UserProjectRoleId implements Serializable {

    Long projectId;
    Long userId;
}
