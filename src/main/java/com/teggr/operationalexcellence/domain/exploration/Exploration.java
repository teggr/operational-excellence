package com.teggr.operationalexcellence.domain.exploration;

import com.teggr.operationalexcellence.domain.snapshot.Snapshot;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "explorations")
@Data
@NoArgsConstructor
public class Exploration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "snapshot_id", nullable = false)
    private Snapshot snapshot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExplorationType type;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExplorationStatus status;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = ExplorationStatus.PENDING;
        }
    }

    public Exploration(Snapshot snapshot, ExplorationType type) {
        this.snapshot = snapshot;
        this.type = type;
        this.status = ExplorationStatus.PENDING;
    }
}
