package com.teggr.operationalexcellence.controller;

import com.teggr.operationalexcellence.service.GitHubService;
import com.teggr.operationalexcellence.view.GitHubViews;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/github")
public class GitHubController {

    private final GitHubService gitHubService;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping(value = "/repositories", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String listRepositories(
            @RegisteredOAuth2AuthorizedClient("github") OAuth2AuthorizedClient authorizedClient,
            @AuthenticationPrincipal OAuth2User oauth2User) {
        
        String accessToken = authorizedClient.getAccessToken().getTokenValue();
        String username = oauth2User.getAttribute("login");
        
        List<Map<String, Object>> repositories = gitHubService.getUserRepositories(accessToken);
        return GitHubViews.repositoriesView(repositories, username);
    }

    @GetMapping(value = "/login")
    public String initiateLogin() {
        // Spring Security will handle the OAuth2 login flow
        return "redirect:/oauth2/authorization/github";
    }
}
