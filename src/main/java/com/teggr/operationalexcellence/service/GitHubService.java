package com.teggr.operationalexcellence.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class GitHubService {

    private final RestTemplate restTemplate;
    private final String githubApiBaseUrl;

    public GitHubService(@Value("${github.api.base-url}") String githubApiBaseUrl) {
        this.restTemplate = new RestTemplate();
        this.githubApiBaseUrl = githubApiBaseUrl;
    }

    public List<Map<String, Object>> getUserRepositories(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
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
            return Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
