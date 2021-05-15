package com.mojave.model;

import com.mojave.dictionary.Priority;
import com.mojave.model.base.AbstractVersional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "task")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task extends AbstractVersional {

    @Column(nullable = false)
    String title;

    @Column
    String description;

    @Column(nullable = false)
    Integer number;

    @Column(precision = 4, scale = 2)
    Float estimate;

    @Column(nullable = false)
    LocalDateTime dueDate;

    @Column
    @Enumerated(EnumType.STRING)
    Priority priority;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    User assignee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_id")
    Tag tag;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "milestone_id")
    Milestone milestone;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "task_histories_relations",
            joinColumns = {@JoinColumn(name = "task_id")},
            inverseJoinColumns = {@JoinColumn(name = "history_id")}
    )
    Set<History> histories = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_column_id")
    BoardColumn boardColumn;
}
