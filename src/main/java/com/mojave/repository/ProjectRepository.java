package com.mojave.repository;

import com.mojave.model.Project;
import com.mojave.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> getAllByUsersIn(Collection<User> users);
}
