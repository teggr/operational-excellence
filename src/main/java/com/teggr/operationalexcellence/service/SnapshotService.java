package com.teggr.operationalexcellence.service;

import com.teggr.operationalexcellence.domain.portfolio.Portfolio;
import com.teggr.operationalexcellence.domain.snapshot.Snapshot;
import com.teggr.operationalexcellence.repository.PortfolioRepository;
import com.teggr.operationalexcellence.repository.SnapshotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class SnapshotService {

    private final SnapshotRepository snapshotRepository;
    private final PortfolioRepository portfolioRepository;
    private final Random random = new Random();

    public SnapshotService(SnapshotRepository snapshotRepository, PortfolioRepository portfolioRepository) {
        this.snapshotRepository = snapshotRepository;
        this.portfolioRepository = portfolioRepository;
    }

    @Transactional
    public Snapshot createSnapshot(UUID portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found"));
        
        Snapshot snapshot = new Snapshot(portfolio);
        
        // Generate dummy commit hashes for each repository in the portfolio
        for (String repo : portfolio.getRepositories()) {
            String commitHash = generateDummyCommitHash();
            snapshot.getCommitMap().put(repo, commitHash);
        }
        
        snapshot.setEnvironment("production");
        
        return snapshotRepository.save(snapshot);
    }

    public List<Snapshot> listSnapshots() {
        return snapshotRepository.findAll();
    }

    public Optional<Snapshot> getSnapshot(UUID id) {
        return snapshotRepository.findById(id);
    }

    public List<Snapshot> listSnapshotsByPortfolio(UUID portfolioId) {
        return snapshotRepository.findByPortfolioId(portfolioId);
    }

    @Transactional
    public void deleteSnapshot(UUID id) {
        snapshotRepository.deleteById(id);
    }

    private String generateDummyCommitHash() {
        StringBuilder hash = new StringBuilder();
        String chars = "0123456789abcdef";
        for (int i = 0; i < 40; i++) {
            hash.append(chars.charAt(random.nextInt(chars.length())));
        }
        return hash.toString();
    }
}
