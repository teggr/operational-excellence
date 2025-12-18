package com.teggr.operationalexcellence.domain.signal;

import com.teggr.operationalexcellence.domain.exploration.Exploration;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "signals")
@Data
@NoArgsConstructor
public class Signal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "exploration_id", nullable = false)
    private Exploration exploration;

    @Column(nullable = false)
    private String type;

    @Column(length = 1000)
    private String description;

    @Column(length = 500)
    private String reference;

    private Double severity;

    private Double confidence;

    public Signal(Exploration exploration, String type, String description, String reference) {
        this.exploration = exploration;
        this.type = type;
        this.description = description;
        this.reference = reference;
    }
}
