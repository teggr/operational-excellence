package com.teggr.operationalexcellence.service;

import com.teggr.operationalexcellence.domain.hotspot.Hotspot;
import com.teggr.operationalexcellence.repository.HotspotRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HotspotService {

    private final HotspotRepository hotspotRepository;

    public HotspotService(HotspotRepository hotspotRepository) {
        this.hotspotRepository = hotspotRepository;
    }

    public List<Hotspot> listHotspots() {
        return hotspotRepository.findAll();
    }

    public Optional<Hotspot> getHotspot(UUID id) {
        return hotspotRepository.findById(id);
    }

    public List<Hotspot> listHotspotsByExploration(UUID explorationId) {
        return hotspotRepository.findByExplorationId(explorationId);
    }
}
