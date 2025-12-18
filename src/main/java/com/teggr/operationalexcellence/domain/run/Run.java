package com.teggr.operationalexcellence.domain.run;

import com.teggr.operationalexcellence.domain.exploration.Exploration;
import com.teggr.operationalexcellence.domain.exploration.ExplorationStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "runs")
@Data
@NoArgsConstructor
public class Run {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "exploration_id", nullable = false)
    private Exploration exploration;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExplorationStatus status;

    @Column(length = 5000)
    private String notes;

    @Column(length = 5000)
    private String logs;

    @PrePersist
    protected void onCreate() {
        if (startedAt == null) {
            startedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = ExplorationStatus.RUNNING;
        }
    }

    public Run(Exploration exploration) {
        this.exploration = exploration;
        this.status = ExplorationStatus.RUNNING;
    }
}
