package com.teggr.operationalexcellence.domain.hotspot;

import com.teggr.operationalexcellence.domain.exploration.Exploration;
import com.teggr.operationalexcellence.domain.signal.Signal;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "hotspots")
@Data
@NoArgsConstructor
public class Hotspot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "exploration_id", nullable = false)
    private Exploration exploration;

    @Column(nullable = false)
    private String name;

    private String identifier;

    @Column(length = 1000)
    private String description;

    @ManyToMany
    @JoinTable(
        name = "hotspot_signals",
        joinColumns = @JoinColumn(name = "hotspot_id"),
        inverseJoinColumns = @JoinColumn(name = "signal_id")
    )
    private List<Signal> contributingSignals = new ArrayList<>();

    public Hotspot(Exploration exploration, String name, String description) {
        this.exploration = exploration;
        this.name = name;
        this.description = description;
    }
}
