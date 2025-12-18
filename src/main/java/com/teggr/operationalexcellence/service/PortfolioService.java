package com.teggr.operationalexcellence.service;

import com.teggr.operationalexcellence.domain.portfolio.Portfolio;
import com.teggr.operationalexcellence.repository.PortfolioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    @Transactional
    public Portfolio createPortfolio(String name, String description) {
        Portfolio portfolio = new Portfolio(name, description);
        return portfolioRepository.save(portfolio);
    }

    @Transactional
    public Portfolio addRepository(UUID portfolioId, String repositoryUrl) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found"));
        portfolio.getRepositories().add(repositoryUrl);
        return portfolioRepository.save(portfolio);
    }

    public List<Portfolio> listPortfolios() {
        return portfolioRepository.findAll();
    }

    public Optional<Portfolio> getPortfolio(UUID id) {
        return portfolioRepository.findById(id);
    }

    @Transactional
    public void deletePortfolio(UUID id) {
        portfolioRepository.deleteById(id);
    }

    @Transactional
    public Portfolio updatePortfolio(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }
}
