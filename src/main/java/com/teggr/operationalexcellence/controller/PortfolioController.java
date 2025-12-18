package com.teggr.operationalexcellence.controller;

import com.teggr.operationalexcellence.domain.portfolio.Portfolio;
import com.teggr.operationalexcellence.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/portfolios")
@Tag(name = "Portfolio", description = "Portfolio management endpoints")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @PostMapping
    @Operation(summary = "Create a new portfolio")
    public ResponseEntity<Portfolio> createPortfolio(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String description = request.get("description");
        Portfolio portfolio = portfolioService.createPortfolio(name, description);
        return ResponseEntity.status(HttpStatus.CREATED).body(portfolio);
    }

    @GetMapping
    @Operation(summary = "List all portfolios")
    public ResponseEntity<List<Portfolio>> listPortfolios() {
        return ResponseEntity.ok(portfolioService.listPortfolios());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get portfolio by ID")
    public ResponseEntity<Portfolio> getPortfolio(@PathVariable UUID id) {
        return portfolioService.getPortfolio(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/repositories")
    @Operation(summary = "Add repository to portfolio")
    public ResponseEntity<Portfolio> addRepository(
            @PathVariable UUID id,
            @RequestBody Map<String, String> request) {
        String repositoryUrl = request.get("url");
        Portfolio portfolio = portfolioService.addRepository(id, repositoryUrl);
        return ResponseEntity.ok(portfolio);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete portfolio")
    public ResponseEntity<Void> deletePortfolio(@PathVariable UUID id) {
        portfolioService.deletePortfolio(id);
        return ResponseEntity.noContent().build();
    }
}
