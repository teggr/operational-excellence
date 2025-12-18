package com.teggr.operationalexcellence.repository;

import com.teggr.operationalexcellence.model.GitHubToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GitHubTokenRepository extends JpaRepository<GitHubToken, Long> {
    Optional<GitHubToken> findByUsername(String username);
}
