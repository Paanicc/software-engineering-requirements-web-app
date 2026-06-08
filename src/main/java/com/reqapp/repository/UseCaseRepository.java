package com.reqapp.repository;

import com.reqapp.domain.Project;
import com.reqapp.domain.UseCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UseCaseRepository extends JpaRepository<UseCase, Long> {
    List<UseCase> findByProject(Project project);
}
