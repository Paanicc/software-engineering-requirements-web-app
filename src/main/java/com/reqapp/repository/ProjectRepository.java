package com.reqapp.repository;

import com.reqapp.domain.Project;
import com.reqapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT p FROM Project p LEFT JOIN p.teammates t WHERE p.user = :user OR t = :user")
    List<Project> findByUserOrTeammate(@org.springframework.data.repository.query.Param("user") User user);
}
