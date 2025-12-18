package com.teggr.operationalexcellence.repository;

import com.teggr.operationalexcellence.domain.exploration.Exploration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExplorationRepository extends JpaRepository<Exploration, UUID> {
    List<Exploration> findBySnapshotId(UUID snapshotId);
}
