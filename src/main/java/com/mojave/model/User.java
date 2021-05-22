package com.mojave.model;

import com.mojave.model.base.AbstractVersional;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username", "email"
        })})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends AbstractVersional {

    @Column(name = "name", length = 100)
    String name;

    @Column(name = "username", nullable = false, length = 40)
    String username;

    @Column(name = "email", nullable = false, length = 50)
    String email;

    @Column(name = "password")
    String password;

    @OrderBy("role DESC")
    @OneToMany(mappedBy = "id.userId", cascade = CascadeType.ALL)
    Set<UserProjectRole> projectRoles = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "user_project",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "project_id")}
    )
    Set<Project> projects = new HashSet<>();
}


