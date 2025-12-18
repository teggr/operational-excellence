package com.teggr.operationalexcellence.controller;

import com.teggr.operationalexcellence.domain.snapshot.Snapshot;
import com.teggr.operationalexcellence.service.SnapshotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Tag(name = "Snapshot", description = "Snapshot management endpoints")
public class SnapshotController {

    private final SnapshotService snapshotService;

    public SnapshotController(SnapshotService snapshotService) {
        this.snapshotService = snapshotService;
    }

    @PostMapping("/portfolios/{portfolioId}/snapshots")
    @Operation(summary = "Create a snapshot for a portfolio")
    public ResponseEntity<Snapshot> createSnapshot(@PathVariable UUID portfolioId) {
        Snapshot snapshot = snapshotService.createSnapshot(portfolioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(snapshot);
    }

    @GetMapping("/snapshots")
    @Operation(summary = "List all snapshots")
    public ResponseEntity<List<Snapshot>> listSnapshots() {
        return ResponseEntity.ok(snapshotService.listSnapshots());
    }

    @GetMapping("/snapshots/{id}")
    @Operation(summary = "Get snapshot by ID")
    public ResponseEntity<Snapshot> getSnapshot(@PathVariable UUID id) {
        return snapshotService.getSnapshot(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/snapshots/{id}")
    @Operation(summary = "Delete snapshot")
    public ResponseEntity<Void> deleteSnapshot(@PathVariable UUID id) {
        snapshotService.deleteSnapshot(id);
        return ResponseEntity.noContent().build();
    }
}
