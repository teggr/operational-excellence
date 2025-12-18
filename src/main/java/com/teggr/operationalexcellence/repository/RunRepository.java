package com.teggr.operationalexcellence.repository;

import com.teggr.operationalexcellence.domain.run.Run;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RunRepository extends JpaRepository<Run, UUID> {
    List<Run> findByExplorationId(UUID explorationId);
}
