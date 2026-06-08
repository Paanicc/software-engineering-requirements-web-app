package com.reqapp.repository;

import com.reqapp.domain.CrcCardComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrcCardCommentRepository extends JpaRepository<CrcCardComment, Long> {
}
