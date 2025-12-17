package com.teggr.operationalexcellence.controller;

import com.teggr.operationalexcellence.model.GitRepository;
import com.teggr.operationalexcellence.service.GitRepositoryService;
import com.teggr.operationalexcellence.view.RepositoryViews;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class RepositoryController {

    private final GitRepositoryService gitRepositoryService;

    public RepositoryController(GitRepositoryService gitRepositoryService) {
        this.gitRepositoryService = gitRepositoryService;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String listRepositories() {
        return RepositoryViews.listView(gitRepositoryService.findAll(), gitRepositoryService);
    }

    @GetMapping(value = "/new", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String newRepositoryForm() {
        return RepositoryViews.formView(null);
    }

    @GetMapping(value = "/{id}/edit", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String editRepositoryForm(@PathVariable Long id) {
        GitRepository repository = gitRepositoryService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Repository not found"));
        return RepositoryViews.formView(repository);
    }

    @PostMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String saveRepository(
            @RequestParam(required = false) Long id,
            @RequestParam String name,
            @RequestParam String url) {
        
        GitRepository repository;
        if (id != null) {
            repository = gitRepositoryService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Repository not found"));
            repository.setName(name);
            repository.setUrl(url);
        } else {
            repository = new GitRepository(name, url, "");
        }
        
        gitRepositoryService.save(repository);
        return "redirect:/";
    }

    @PostMapping(value = "/{id}/delete", produces = MediaType.TEXT_HTML_VALUE)
    public String deleteRepository(@PathVariable Long id) {
        try {
            gitRepositoryService.cleanRepository(id);
        } catch (Exception e) {
            // Log error but continue with deletion
        }
        gitRepositoryService.deleteById(id);
        return "redirect:/";
    }

    @PostMapping(value = "/{id}/clone", produces = MediaType.TEXT_HTML_VALUE)
    public String cloneRepository(@PathVariable Long id) {
        try {
            gitRepositoryService.cloneRepository(id);
        } catch (Exception e) {
            // Error is saved in the repository entity
        }
        return "redirect:/";
    }

    @PostMapping(value = "/{id}/update", produces = MediaType.TEXT_HTML_VALUE)
    public String updateRepository(@PathVariable Long id) {
        try {
            gitRepositoryService.updateRepository(id);
        } catch (Exception e) {
            // Error is saved in the repository entity
        }
        return "redirect:/";
    }

    @PostMapping(value = "/{id}/clean", produces = MediaType.TEXT_HTML_VALUE)
    public String cleanRepository(@PathVariable Long id) {
        try {
            gitRepositoryService.cleanRepository(id);
        } catch (Exception e) {
            // Error is saved in the repository entity
        }
        return "redirect:/";
    }
}
