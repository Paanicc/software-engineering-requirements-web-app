package com.reqapp.repository;

import com.reqapp.domain.CrcCard;
import com.reqapp.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrcCardRepository extends JpaRepository<CrcCard, Long> {
    List<CrcCard> findByProject(Project project);
}
