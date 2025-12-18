package com.teggr.operationalexcellence.controller;

import com.teggr.operationalexcellence.service.GitHubService;
import com.teggr.operationalexcellence.view.GitHubViews;
import org.springframework.http.MediaType;
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
    public String listRepositories() {
        String tokenStatus = gitHubService.getTokenStatus();
        List<Map<String, Object>> repositories = gitHubService.getUserRepositories();
        return GitHubViews.repositoriesView(repositories, tokenStatus);
    }

    @GetMapping(value = "/login")
    public String initiateLogin() {
        // Redirect to a page where user can manually enter their token
        return "redirect:/github/token-form";
    }

    @GetMapping(value = "/token-form", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String showTokenForm(@RequestParam(required = false) String error) {
        return GitHubViews.tokenFormView(error);
    }

    @PostMapping(value = "/token")
    public String saveToken(@RequestParam String token) {
        // Validate token by getting user info
        Map<String, Object> user = gitHubService.getAuthenticatedUser(token);
        
        if (!user.isEmpty() && user.containsKey("login")) {
            String username = (String) user.get("login");
            gitHubService.saveToken(username, token);
            return "redirect:/github/repositories";
        } else {
            return "redirect:/github/token-form?error=invalid";
        }
    }

    @PostMapping(value = "/logout")
    public String logout() {
        gitHubService.deleteToken();
        return "redirect:/github/repositories";
    }
}
