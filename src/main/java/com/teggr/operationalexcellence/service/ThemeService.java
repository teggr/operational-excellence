package com.teggr.operationalexcellence.service;

import com.teggr.operationalexcellence.domain.theme.Theme;
import com.teggr.operationalexcellence.repository.ThemeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> listThemes() {
        return themeRepository.findAll();
    }

    public Optional<Theme> getTheme(UUID id) {
        return themeRepository.findById(id);
    }

    public List<Theme> listThemesByExploration(UUID explorationId) {
        return themeRepository.findByExplorationId(explorationId);
    }
}
