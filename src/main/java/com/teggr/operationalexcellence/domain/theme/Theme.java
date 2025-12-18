package com.teggr.operationalexcellence.domain.theme;

import com.teggr.operationalexcellence.domain.exploration.Exploration;
import com.teggr.operationalexcellence.domain.hotspot.Hotspot;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "themes")
@Data
@NoArgsConstructor
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "exploration_id", nullable = false)
    private Exploration exploration;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @ManyToMany
    @JoinTable(
        name = "theme_hotspots",
        joinColumns = @JoinColumn(name = "theme_id"),
        inverseJoinColumns = @JoinColumn(name = "hotspot_id")
    )
    private List<Hotspot> associatedHotspots = new ArrayList<>();

    public Theme(Exploration exploration, String title, String description) {
        this.exploration = exploration;
        this.title = title;
        this.description = description;
    }
}
