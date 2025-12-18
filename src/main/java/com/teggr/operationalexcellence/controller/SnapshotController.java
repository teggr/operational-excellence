package com.teggr.operationalexcellence.controller;

import com.teggr.operationalexcellence.domain.snapshot.Snapshot;
import com.teggr.operationalexcellence.service.SnapshotService;
import com.teggr.operationalexcellence.view.SnapshotViews;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class SnapshotController {

    private final SnapshotService snapshotService;

    public SnapshotController(SnapshotService snapshotService) {
        this.snapshotService = snapshotService;
    }

    @GetMapping(value = "/snapshots", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String listSnapshots() {
        return SnapshotViews.listView(snapshotService.listSnapshots());
    }

    @GetMapping(value = "/snapshots/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String viewSnapshot(@PathVariable UUID id) {
        Snapshot snapshot = snapshotService.getSnapshot(id)
                .orElseThrow(() -> new IllegalArgumentException("Snapshot not found"));
        return SnapshotViews.detailView(snapshot);
    }

    @GetMapping(value = "/portfolios/{portfolioId}/snapshots/new", produces = MediaType.TEXT_HTML_VALUE)
    public String createSnapshotRedirect(@PathVariable UUID portfolioId) {
        Snapshot snapshot = snapshotService.createSnapshot(portfolioId);
        return "redirect:/snapshots/" + snapshot.getId();
    }
}
