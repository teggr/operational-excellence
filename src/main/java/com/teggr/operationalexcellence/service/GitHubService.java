package com.teggr.operationalexcellence.service;

import com.teggr.operationalexcellence.model.GitHubToken;
import com.teggr.operationalexcellence.repository.GitHubTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GitHubService {

    private final GitHubTokenRepository tokenRepository;
    private final RestTemplate restTemplate;
    private final String githubApiBaseUrl;

    public GitHubService(
            GitHubTokenRepository tokenRepository,
            @Value("${github.api.base-url}") String githubApiBaseUrl) {
        this.tokenRepository = tokenRepository;
        this.restTemplate = new RestTemplate();
        this.githubApiBaseUrl = githubApiBaseUrl;
    }

    public Optional<GitHubToken> getCurrentToken() {
        // For simplicity, we'll use a single token (first one in the database)
        // In a real application, you'd associate tokens with authenticated users
        return tokenRepository.findAll().stream().findFirst();
    }

    public void saveToken(String username, String accessToken) {
        Optional<GitHubToken> existingToken = tokenRepository.findByUsername(username);
        
        GitHubToken token;
        if (existingToken.isPresent()) {
            token = existingToken.get();
            token.setAccessToken(accessToken);
        } else {
            token = new GitHubToken(username, accessToken);
        }
        
        tokenRepository.save(token);
    }

    public void deleteToken() {
        tokenRepository.deleteAll();
    }

    public List<Map<String, Object>> getUserRepositories() {
        Optional<GitHubToken> tokenOpt = getCurrentToken();
        
        if (tokenOpt.isEmpty()) {
            return Collections.emptyList();
        }

        GitHubToken token = tokenOpt.get();
        
        if (token.isExpired()) {
            return Collections.emptyList();
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token.getAccessToken());
            headers.set("Accept", "application/vnd.github.v3+json");
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<List> response = restTemplate.exchange(
                githubApiBaseUrl + "/user/repos?sort=updated&per_page=100",
                HttpMethod.GET,
                entity,
                List.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (List<Map<String, Object>>) response.getBody();
            }
            
            return Collections.emptyList();
        } catch (HttpClientErrorException e) {
            // Token might be invalid or expired
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                // Token is invalid - could delete it here
            }
            return Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public Map<String, Object> getAuthenticatedUser(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.set("Accept", "application/vnd.github.v3+json");
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                githubApiBaseUrl + "/user",
                HttpMethod.GET,
                entity,
                Map.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (Map<String, Object>) response.getBody();
            }
            
            return Collections.emptyMap();
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    public boolean hasValidToken() {
        Optional<GitHubToken> tokenOpt = getCurrentToken();
        return tokenOpt.isPresent() && !tokenOpt.get().isExpired();
    }

    public String getTokenStatus() {
        Optional<GitHubToken> tokenOpt = getCurrentToken();
        
        if (tokenOpt.isEmpty()) {
            return "NO_TOKEN";
        }
        
        GitHubToken token = tokenOpt.get();
        if (token.isExpired()) {
            return "EXPIRED";
        }
        
        return "VALID";
    }
}
