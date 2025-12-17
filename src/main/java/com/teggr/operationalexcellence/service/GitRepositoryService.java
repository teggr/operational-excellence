package com.teggr.operationalexcellence.service;

import com.teggr.operationalexcellence.model.GitRepository;
import com.teggr.operationalexcellence.repository.GitRepositoryRepository;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class GitRepositoryService {

    private final GitRepositoryRepository repository;
    private final String localBasePath;

    public GitRepositoryService(
            GitRepositoryRepository repository,
            @Value("${app.repositories.local-path}") String localBasePath) {
        this.repository = repository;
        this.localBasePath = localBasePath;
    }

    public List<GitRepository> findAll() {
        return repository.findAll();
    }

    public Optional<GitRepository> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public GitRepository save(GitRepository gitRepository) {
        if (gitRepository.getId() == null) {
            // New repository - set local path with proper sanitization
            String sanitizedName = gitRepository.getName().replaceAll("[^a-zA-Z0-9-_]", "_");
            // Prevent path traversal by ensuring the path stays within the base directory
            Path basePath = Paths.get(localBasePath).toAbsolutePath().normalize();
            Path targetPath = basePath.resolve(sanitizedName).normalize();
            
            // Verify the resolved path is still within the base directory
            if (!targetPath.startsWith(basePath)) {
                throw new IllegalArgumentException("Invalid repository name: path traversal detected");
            }
            
            gitRepository.setLocalPath(targetPath.toString());
        }
        return repository.save(gitRepository);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void cloneRepository(Long id) throws GitAPIException, IOException {
        GitRepository gitRepo = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Repository not found"));

        // Validate URL format
        String url = gitRepo.getUrl();
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("Repository URL is required");
        }
        
        // Basic URL validation - ensure it's a valid Git URL
        if (!url.startsWith("http://") && !url.startsWith("https://") && 
            !url.startsWith("git://") && !url.startsWith("ssh://") &&
            !url.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+:.*")) {
            throw new IllegalArgumentException("Invalid Git repository URL format");
        }

        File localPathFile = new File(gitRepo.getLocalPath());
        
        try {
            // Clone the repository
            Git.cloneRepository()
                    .setURI(gitRepo.getUrl())
                    .setDirectory(localPathFile)
                    .call()
                    .close();

            gitRepo.setLastCloned(LocalDateTime.now());
            gitRepo.setLastError(null);
            repository.save(gitRepo);
        } catch (Exception e) {
            gitRepo.setLastError("Clone failed: " + e.getMessage());
            repository.save(gitRepo);
            throw e;
        }
    }

    @Transactional
    public void updateRepository(Long id) throws GitAPIException, IOException {
        GitRepository gitRepo = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Repository not found"));

        File localPathFile = new File(gitRepo.getLocalPath());
        
        if (!localPathFile.exists()) {
            throw new IllegalStateException("Repository not cloned yet. Please clone first.");
        }

        try {
            // Pull latest changes
            try (Git git = Git.open(localPathFile)) {
                git.pull().call();
            }

            gitRepo.setLastUpdated(LocalDateTime.now());
            gitRepo.setLastError(null);
            repository.save(gitRepo);
        } catch (Exception e) {
            gitRepo.setLastError("Update failed: " + e.getMessage());
            repository.save(gitRepo);
            throw e;
        }
    }

    @Transactional
    public void cleanRepository(Long id) throws IOException {
        GitRepository gitRepo = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Repository not found"));

        File localPathFile = new File(gitRepo.getLocalPath());
        
        if (localPathFile.exists()) {
            try {
                deleteDirectory(localPathFile.toPath());
                gitRepo.setLastCloned(null);
                gitRepo.setLastUpdated(null);
                gitRepo.setLastError(null);
                repository.save(gitRepo);
            } catch (Exception e) {
                gitRepo.setLastError("Clean failed: " + e.getMessage());
                repository.save(gitRepo);
                throw e;
            }
        }
    }

    private void deleteDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            try (var stream = Files.walk(path)) {
                stream.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        }
    }

    public boolean isCloned(Long id) {
        return repository.findById(id)
                .map(gitRepo -> new File(gitRepo.getLocalPath()).exists())
                .orElse(false);
    }
}
