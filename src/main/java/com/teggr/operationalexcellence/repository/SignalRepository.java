package com.teggr.operationalexcellence.repository;

import com.teggr.operationalexcellence.domain.signal.Signal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SignalRepository extends JpaRepository<Signal, UUID> {
    List<Signal> findByExplorationId(UUID explorationId);
}
