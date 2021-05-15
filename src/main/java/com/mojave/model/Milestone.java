package com.mojave.model;

import com.mojave.model.base.AbstractVersional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "milestone")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Milestone extends AbstractVersional {

    @Column
    String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    Project project;
}
