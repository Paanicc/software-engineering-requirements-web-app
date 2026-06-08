package com.reqapp.repository;

import com.reqapp.domain.UseCaseComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UseCaseCommentRepository extends JpaRepository<UseCaseComment, Long> {
}
