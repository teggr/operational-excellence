package com.teggr.operationalexcellence.service;

import com.teggr.operationalexcellence.domain.signal.Signal;
import com.teggr.operationalexcellence.repository.SignalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SignalService {

    private final SignalRepository signalRepository;

    public SignalService(SignalRepository signalRepository) {
        this.signalRepository = signalRepository;
    }

    public List<Signal> listSignals() {
        return signalRepository.findAll();
    }

    public Optional<Signal> getSignal(UUID id) {
        return signalRepository.findById(id);
    }

    public List<Signal> listSignalsByExploration(UUID explorationId) {
        return signalRepository.findByExplorationId(explorationId);
    }
}
