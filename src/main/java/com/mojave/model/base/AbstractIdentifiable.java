package com.mojave.model.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/** Base class for all application entities which have unique identifier. */
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
}
