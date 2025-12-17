package com.teggr.operationalexcellence.repository;

import com.teggr.operationalexcellence.model.GitRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GitRepositoryRepository extends JpaRepository<GitRepository, Long> {
}
