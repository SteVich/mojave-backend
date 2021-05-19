package com.mojave.repository;

import com.mojave.model.Milestone;
import com.mojave.model.Tag;
import com.mojave.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByIdIn(List<Long> ids);

    List<Task> findAllByMilestone(Milestone milestone);

    List<Task> findAllByTag(Tag tag);
}
