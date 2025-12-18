package com.teggr.operationalexcellence.controller;

import com.teggr.operationalexcellence.domain.exploration.Exploration;
import com.teggr.operationalexcellence.domain.exploration.ExplorationType;
import com.teggr.operationalexcellence.domain.hotspot.Hotspot;
import com.teggr.operationalexcellence.domain.signal.Signal;
import com.teggr.operationalexcellence.domain.theme.Theme;
import com.teggr.operationalexcellence.service.ExplorationService;
import com.teggr.operationalexcellence.service.HotspotService;
import com.teggr.operationalexcellence.service.SignalService;
import com.teggr.operationalexcellence.service.ThemeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Tag(name = "Exploration", description = "Exploration management endpoints")
public class ExplorationController {

    private final ExplorationService explorationService;
    private final SignalService signalService;
    private final HotspotService hotspotService;
    private final ThemeService themeService;

    public ExplorationController(
            ExplorationService explorationService,
            SignalService signalService,
            HotspotService hotspotService,
            ThemeService themeService) {
        this.explorationService = explorationService;
        this.signalService = signalService;
        this.hotspotService = hotspotService;
        this.themeService = themeService;
    }

    @PostMapping("/snapshots/{snapshotId}/explorations")
    @Operation(summary = "Create an exploration for a snapshot")
    public ResponseEntity<Exploration> createExploration(
            @PathVariable UUID snapshotId,
            @RequestBody Map<String, String> request) {
        String typeStr = request.get("type");
        
        if (typeStr == null || typeStr.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            ExplorationType type = ExplorationType.valueOf(typeStr);
            Exploration exploration = explorationService.createExploration(snapshotId, type);
            return ResponseEntity.status(HttpStatus.CREATED).body(exploration);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/explorations")
    @Operation(summary = "List all explorations")
    public ResponseEntity<List<Exploration>> listExplorations() {
        return ResponseEntity.ok(explorationService.listExplorations());
    }

    @GetMapping("/explorations/{id}")
    @Operation(summary = "Get exploration by ID")
    public ResponseEntity<Exploration> getExploration(@PathVariable UUID id) {
        return explorationService.getExploration(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/explorations/{id}/signals")
    @Operation(summary = "List signals for an exploration")
    public ResponseEntity<List<Signal>> listSignals(@PathVariable UUID id) {
        return ResponseEntity.ok(signalService.listSignalsByExploration(id));
    }

    @GetMapping("/explorations/{id}/hotspots")
    @Operation(summary = "List hotspots for an exploration")
    public ResponseEntity<List<Hotspot>> listHotspots(@PathVariable UUID id) {
        return ResponseEntity.ok(hotspotService.listHotspotsByExploration(id));
    }

    @GetMapping("/explorations/{id}/themes")
    @Operation(summary = "List themes for an exploration")
    public ResponseEntity<List<Theme>> listThemes(@PathVariable UUID id) {
        return ResponseEntity.ok(themeService.listThemesByExploration(id));
    }

    @DeleteMapping("/explorations/{id}")
    @Operation(summary = "Delete exploration")
    public ResponseEntity<Void> deleteExploration(@PathVariable UUID id) {
        explorationService.deleteExploration(id);
        return ResponseEntity.noContent().build();
    }
}
