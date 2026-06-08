package com.reqapp.repository;

import com.reqapp.domain.Actor;
import com.reqapp.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    List<Actor> findByProject(Project project);
}
