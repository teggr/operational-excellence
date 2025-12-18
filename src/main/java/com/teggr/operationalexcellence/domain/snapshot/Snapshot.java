package com.teggr.operationalexcellence.domain.snapshot;

import com.teggr.operationalexcellence.domain.portfolio.Portfolio;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "snapshots")
@Data
@NoArgsConstructor
public class Snapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ElementCollection
    @CollectionTable(name = "snapshot_commits", joinColumns = @JoinColumn(name = "snapshot_id"))
    @MapKeyColumn(name = "repository")
    @Column(name = "commit_hash")
    private Map<String, String> commitMap = new HashMap<>();

    @Column(length = 2000)
    private String environment;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Snapshot(Portfolio portfolio) {
        this.portfolio = portfolio;
    }
}
