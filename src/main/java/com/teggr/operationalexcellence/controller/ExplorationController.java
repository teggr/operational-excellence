package com.teggr.operationalexcellence.controller;

import com.teggr.operationalexcellence.domain.exploration.Exploration;
import com.teggr.operationalexcellence.domain.exploration.ExplorationType;
import com.teggr.operationalexcellence.domain.hotspot.Hotspot;
import com.teggr.operationalexcellence.domain.signal.Signal;
import com.teggr.operationalexcellence.domain.snapshot.Snapshot;
import com.teggr.operationalexcellence.domain.theme.Theme;
import com.teggr.operationalexcellence.service.ExplorationService;
import com.teggr.operationalexcellence.service.HotspotService;
import com.teggr.operationalexcellence.service.SignalService;
import com.teggr.operationalexcellence.service.SnapshotService;
import com.teggr.operationalexcellence.service.ThemeService;
import com.teggr.operationalexcellence.view.ExplorationViews;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
public class ExplorationController {

    private final ExplorationService explorationService;
    private final SnapshotService snapshotService;
    private final SignalService signalService;
    private final HotspotService hotspotService;
    private final ThemeService themeService;

    public ExplorationController(
            ExplorationService explorationService,
            SnapshotService snapshotService,
            SignalService signalService,
            HotspotService hotspotService,
            ThemeService themeService) {
        this.explorationService = explorationService;
        this.snapshotService = snapshotService;
        this.signalService = signalService;
        this.hotspotService = hotspotService;
        this.themeService = themeService;
    }

    @GetMapping(value = "/explorations", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String listExplorations() {
        return ExplorationViews.listView(explorationService.listExplorations());
    }

    @GetMapping(value = "/explorations/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String viewExploration(@PathVariable UUID id) {
        Exploration exploration = explorationService.getExploration(id)
                .orElseThrow(() -> new IllegalArgumentException("Exploration not found"));
        
        List<Signal> signals = signalService.listSignalsByExploration(id);
        List<Hotspot> hotspots = hotspotService.listHotspotsByExploration(id);
        List<Theme> themes = themeService.listThemesByExploration(id);
        
        return ExplorationViews.detailView(exploration, signals, hotspots, themes);
    }

    @GetMapping(value = "/snapshots/{snapshotId}/explorations/new", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String newExplorationForm(@PathVariable UUID snapshotId) {
        Snapshot snapshot = snapshotService.getSnapshot(snapshotId)
                .orElseThrow(() -> new IllegalArgumentException("Snapshot not found"));
        return ExplorationViews.createFormView(snapshot);
    }

    @PostMapping(value = "/snapshots/{snapshotId}/explorations", produces = MediaType.TEXT_HTML_VALUE)
    public String createExploration(
            @PathVariable UUID snapshotId,
            @RequestParam String type) {
        
        ExplorationType explorationType = ExplorationType.valueOf(type);
        Exploration exploration = explorationService.createExploration(snapshotId, explorationType);
        
        return "redirect:/explorations/" + exploration.getId();
    }
}
