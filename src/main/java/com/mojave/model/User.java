package com.mojave.model;

import com.mojave.dictionary.Role;
import com.mojave.model.base.AbstractVersional;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@TypeDef(
        name = "JSON",
        typeClass = JsonStringType.class
)
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

    @Type(type = "JSON")
    @Column(columnDefinition = "JSON")
    Set<Role> roles = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "user_project",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "project_id")}
    )
    Set<Project> projects = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "user_team",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "team_id")}
    )
    Set<Team> teams = new HashSet<>();
}


