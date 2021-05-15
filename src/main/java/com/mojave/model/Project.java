package com.mojave.model;

import com.mojave.model.base.AbstractVersional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "project")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Project extends AbstractVersional {

    @Column
    String name;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> board = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Team> team = new ArrayList<>();

    @ManyToMany(mappedBy = "projects")
    private Set<User> users = new HashSet<>();
}
