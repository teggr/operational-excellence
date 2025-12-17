package com.teggr.operationalexcellence.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "git_repositories")
public class GitRepository {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String localPath;

    private LocalDateTime lastCloned;

    private LocalDateTime lastUpdated;

    @Column(length = 1000)
    private String lastError;

    public GitRepository() {
    }

    public GitRepository(String name, String url, String localPath) {
        this.name = name;
        this.url = url;
        this.localPath = localPath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public LocalDateTime getLastCloned() {
        return lastCloned;
    }

    public void setLastCloned(LocalDateTime lastCloned) {
        this.lastCloned = lastCloned;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }
}
