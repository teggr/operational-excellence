package com.teggr.operationalexcellence.controller;

import com.teggr.operationalexcellence.domain.portfolio.Portfolio;
import com.teggr.operationalexcellence.service.PortfolioService;
import com.teggr.operationalexcellence.view.PortfolioViews;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String listPortfolios() {
        return PortfolioViews.listView(portfolioService.listPortfolios());
    }

    @GetMapping(value = "/new", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String newPortfolioForm() {
        return PortfolioViews.formView(null);
    }

    @GetMapping(value = "/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String viewPortfolio(@PathVariable UUID id) {
        Portfolio portfolio = portfolioService.getPortfolio(id)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found"));
        return PortfolioViews.detailView(portfolio);
    }

    @PostMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String savePortfolio(
            @RequestParam(required = false) String id,
            @RequestParam String name,
            @RequestParam(required = false) String description) {
        
        if (id != null && !id.isEmpty()) {
            // Edit existing portfolio - not implemented yet
            throw new UnsupportedOperationException("Edit not yet supported");
        } else {
            portfolioService.createPortfolio(name, description);
        }
        
        return "redirect:/portfolios";
    }

    @PostMapping(value = "/{id}/delete", produces = MediaType.TEXT_HTML_VALUE)
    public String deletePortfolio(@PathVariable UUID id) {
        portfolioService.deletePortfolio(id);
        return "redirect:/portfolios";
    }

    @GetMapping(value = "/{id}/repositories/new", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String newRepositoryForm(@PathVariable UUID id) {
        Portfolio portfolio = portfolioService.getPortfolio(id)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found"));
        return PortfolioViews.repositoryFormView(portfolio);
    }

    @PostMapping(value = "/{id}/repositories", produces = MediaType.TEXT_HTML_VALUE)
    public String addRepository(
            @PathVariable UUID id,
            @RequestParam String url) {
        portfolioService.addRepository(id, url);
        return "redirect:/portfolios/" + id;
    }

    @PostMapping(value = "/{id}/repositories/remove", produces = MediaType.TEXT_HTML_VALUE)
    public String removeRepository(
            @PathVariable UUID id,
            @RequestParam String url) {
        Portfolio portfolio = portfolioService.getPortfolio(id)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found"));
        portfolio.getRepositories().remove(url);
        portfolioService.updatePortfolio(portfolio);
        return "redirect:/portfolios/" + id;
    }
}
