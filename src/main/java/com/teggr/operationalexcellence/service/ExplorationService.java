package com.teggr.operationalexcellence.service;

import com.teggr.operationalexcellence.domain.exploration.Exploration;
import com.teggr.operationalexcellence.domain.exploration.ExplorationStatus;
import com.teggr.operationalexcellence.domain.exploration.ExplorationType;
import com.teggr.operationalexcellence.domain.hotspot.Hotspot;
import com.teggr.operationalexcellence.domain.signal.Signal;
import com.teggr.operationalexcellence.domain.snapshot.Snapshot;
import com.teggr.operationalexcellence.domain.theme.Theme;
import com.teggr.operationalexcellence.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExplorationService {

    private final ExplorationRepository explorationRepository;
    private final SnapshotRepository snapshotRepository;
    private final SignalRepository signalRepository;
    private final HotspotRepository hotspotRepository;
    private final ThemeRepository themeRepository;

    public ExplorationService(
            ExplorationRepository explorationRepository,
            SnapshotRepository snapshotRepository,
            SignalRepository signalRepository,
            HotspotRepository hotspotRepository,
            ThemeRepository themeRepository) {
        this.explorationRepository = explorationRepository;
        this.snapshotRepository = snapshotRepository;
        this.signalRepository = signalRepository;
        this.hotspotRepository = hotspotRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Exploration createExploration(UUID snapshotId, ExplorationType type) {
        Snapshot snapshot = snapshotRepository.findById(snapshotId)
                .orElseThrow(() -> new IllegalArgumentException("Snapshot not found"));
        
        Exploration exploration = new Exploration(snapshot, type);
        exploration = explorationRepository.save(exploration);
        
        // Generate dummy data for the exploration
        generateDummySignals(exploration);
        generateDummyHotspots(exploration);
        generateDummyThemes(exploration);
        
        // Mark exploration as completed
        exploration.setStatus(ExplorationStatus.COMPLETED);
        return explorationRepository.save(exploration);
    }

    public List<Exploration> listExplorations() {
        return explorationRepository.findAll();
    }

    public Optional<Exploration> getExploration(UUID id) {
        return explorationRepository.findById(id);
    }

    public List<Exploration> listExplorationsBySnapshot(UUID snapshotId) {
        return explorationRepository.findBySnapshotId(snapshotId);
    }

    @Transactional
    public void deleteExploration(UUID id) {
        explorationRepository.deleteById(id);
    }

    private void generateDummySignals(Exploration exploration) {
        String[] signalTypes = {"Code Smell", "Complexity", "Duplication", "Security", "Performance"};
        String[] references = {
            "com.example.service.UserService:123",
            "com.example.controller.OrderController:45",
            "com.example.repository.ProductRepository:89",
            "com.example.util.StringHelper:234"
        };

        for (int i = 0; i < 5; i++) {
            Signal signal = new Signal(
                exploration,
                signalTypes[i % signalTypes.length],
                "Dummy signal description for " + signalTypes[i % signalTypes.length],
                references[i % references.length]
            );
            signal.setSeverity((double) (1 + i % 5));
            signal.setConfidence(0.7 + (i * 0.05));
            signalRepository.save(signal);
        }
    }

    private void generateDummyHotspots(Exploration exploration) {
        List<Signal> signals = signalRepository.findByExplorationId(exploration.getId());
        
        String[] hotspotNames = {"Authentication Module", "Database Layer", "API Gateway"};
        
        for (int i = 0; i < 3; i++) {
            Hotspot hotspot = new Hotspot(
                exploration,
                hotspotNames[i % hotspotNames.length],
                "Dummy hotspot description for " + hotspotNames[i % hotspotNames.length]
            );
            hotspot.setIdentifier("HS-" + (i + 1));
            
            // Associate some signals with this hotspot
            if (!signals.isEmpty() && signals.size() > 0) {
                hotspot.getContributingSignals().add(signals.get(i % signals.size()));
            }
            
            hotspotRepository.save(hotspot);
        }
    }

    private void generateDummyThemes(Exploration exploration) {
        List<Hotspot> hotspots = hotspotRepository.findByExplorationId(exploration.getId());
        
        String[] themeNames = {"Technical Debt", "Security Concerns", "Performance Issues"};
        
        for (int i = 0; i < 2; i++) {
            Theme theme = new Theme(
                exploration,
                themeNames[i % themeNames.length],
                "Dummy theme description for " + themeNames[i % themeNames.length]
            );
            
            // Associate some hotspots with this theme
            if (!hotspots.isEmpty() && hotspots.size() > 0) {
                theme.getAssociatedHotspots().add(hotspots.get(i % hotspots.size()));
            }
            
            themeRepository.save(theme);
        }
    }
}
