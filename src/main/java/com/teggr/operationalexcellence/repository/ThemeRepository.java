package com.teggr.operationalexcellence.repository;

import com.teggr.operationalexcellence.domain.theme.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, UUID> {
    List<Theme> findByExplorationId(UUID explorationId);
}
