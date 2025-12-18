package com.teggr.operationalexcellence.repository;

import com.teggr.operationalexcellence.domain.snapshot.Snapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SnapshotRepository extends JpaRepository<Snapshot, UUID> {
    List<Snapshot> findByPortfolioId(UUID portfolioId);
}
