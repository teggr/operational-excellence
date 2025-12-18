package com.teggr.operationalexcellence.view;

import com.teggr.operationalexcellence.domain.exploration.Exploration;
import com.teggr.operationalexcellence.domain.exploration.ExplorationType;
import com.teggr.operationalexcellence.domain.hotspot.Hotspot;
import com.teggr.operationalexcellence.domain.signal.Signal;
import com.teggr.operationalexcellence.domain.snapshot.Snapshot;
import com.teggr.operationalexcellence.domain.theme.Theme;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static j2html.TagCreator.*;

public class ExplorationViews {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String listView(List<Exploration> explorations) {
        return html(
                head(
                        title("Explorations"),
                        style(PortfolioViews.getCommonStyles())
                ),
                body(
                        div(
                                h1("Explorations"),
                                div(
                                        a("Back to Portfolios").withClass("btn btn-secondary").withHref("/portfolios")
                                ).withClass("header-actions"),
                                explorations.isEmpty()
                                        ? p("No explorations available.")
                                        : table(
                                                thead(
                                                        tr(
                                                                th("Type"),
                                                                th("Portfolio"),
                                                                th("Status"),
                                                                th("Created"),
                                                                th("Actions")
                                                        )
                                                ),
                                                tbody(
                                                        each(explorations, exploration -> tr(
                                                                td(exploration.getType().toString()),
                                                                td(exploration.getSnapshot().getPortfolio().getName()),
                                                                td(span(exploration.getStatus().toString()).withClass("status status-" + exploration.getStatus().toString().toLowerCase())),
                                                                td(exploration.getCreatedAt().format(formatter)),
                                                                td(
                                                                        a("View").withClass("btn btn-primary").withHref("/explorations/" + exploration.getId())
                                                                )
                                                        ))
                                                )
                                        )
                        ).withClass("container")
                )
        ).render();
    }

    public static String createFormView(Snapshot snapshot) {
        return html(
                head(
                        title("Create Exploration"),
                        style(PortfolioViews.getCommonStyles())
                ),
                body(
                        div(
                                h1("Create Exploration"),
                                form(
                                        div(
                                                p(strong("Snapshot: "), text(snapshot.getPortfolio().getName() + " - " + snapshot.getCreatedAt().format(formatter)))
                                        ),
                                        div(
                                                label("Exploration Type:").attr("for", "type"),
                                                select(
                                                        each(java.util.Arrays.asList(ExplorationType.values()), type -> 
                                                                option(type.toString()).withValue(type.toString())
                                                        )
                                                ).withName("type").withId("type").isRequired()
                                        ).withClass("form-group"),
                                        div(
                                                button("Create").withClass("btn btn-primary").withType("submit"),
                                                a("Cancel").withClass("btn btn-secondary").withHref("/snapshots/" + snapshot.getId())
                                        ).withClass("form-actions")
                                ).withMethod("post").withAction("/snapshots/" + snapshot.getId() + "/explorations")
                        ).withClass("container")
                )
        ).render();
    }

    public static String detailView(Exploration exploration, List<Signal> signals, List<Hotspot> hotspots, List<Theme> themes) {
        return html(
                head(
                        title("Exploration Details"),
                        style(PortfolioViews.getCommonStyles() + getExplorationStyles())
                ),
                body(
                        div(
                                h1("Exploration: " + exploration.getType()),
                                div(
                                        a("Back to Snapshot").withClass("btn btn-secondary").withHref("/snapshots/" + exploration.getSnapshot().getId())
                                ).withClass("header-actions"),
                                div(
                                        p(strong("Type: "), text(exploration.getType().toString())),
                                        p(strong("Status: "), span(exploration.getStatus().toString()).withClass("status status-" + exploration.getStatus().toString().toLowerCase())),
                                        p(strong("Portfolio: "), text(exploration.getSnapshot().getPortfolio().getName())),
                                        p(strong("Created: "), text(exploration.getCreatedAt().format(formatter)))
                                ).withClass("detail-section"),
                                
                                h2("Signals (" + signals.size() + ")"),
                                signals.isEmpty()
                                        ? p("No signals found.")
                                        : table(
                                                thead(
                                                        tr(
                                                                th("Type"),
                                                                th("Description"),
                                                                th("Reference"),
                                                                th("Severity"),
                                                                th("Confidence")
                                                        )
                                                ),
                                                tbody(
                                                        each(signals, signal -> tr(
                                                                td(signal.getType()),
                                                                td(signal.getDescription()),
                                                                td(code(signal.getReference())),
                                                                td(signal.getSeverity() != null ? String.format("%.1f", signal.getSeverity()) : "N/A"),
                                                                td(signal.getConfidence() != null ? String.format("%.2f", signal.getConfidence()) : "N/A")
                                                        ))
                                                )
                                        ),
                                
                                h2("Hotspots (" + hotspots.size() + ")"),
                                hotspots.isEmpty()
                                        ? p("No hotspots identified.")
                                        : div(
                                                each(hotspots, hotspot -> 
                                                        div(
                                                                h3(hotspot.getName() + " (" + hotspot.getIdentifier() + ")"),
                                                                p(hotspot.getDescription()),
                                                                p(strong("Contributing Signals: "), text(String.valueOf(hotspot.getContributingSignals().size())))
                                                        ).withClass("hotspot-card")
                                                )
                                        ),
                                
                                h2("Themes (" + themes.size() + ")"),
                                themes.isEmpty()
                                        ? p("No themes identified.")
                                        : div(
                                                each(themes, theme -> 
                                                        div(
                                                                h3(theme.getTitle()),
                                                                p(theme.getDescription()),
                                                                p(strong("Associated Hotspots: "), text(String.valueOf(theme.getAssociatedHotspots().size())))
                                                        ).withClass("theme-card")
                                                )
                                        )
                        ).withClass("container")
                )
        ).render();
    }

    private static String getExplorationStyles() {
        return """
                .status {
                    padding: 4px 8px;
                    border-radius: 4px;
                    font-size: 12px;
                    font-weight: bold;
                }
                .status-completed {
                    background-color: #d4edda;
                    color: #155724;
                }
                .status-pending {
                    background-color: #fff3cd;
                    color: #856404;
                }
                .status-running {
                    background-color: #cce5ff;
                    color: #004085;
                }
                .status-failed {
                    background-color: #f8d7da;
                    color: #721c24;
                }
                .hotspot-card, .theme-card {
                    background: #f8f9fa;
                    border-left: 4px solid #007bff;
                    padding: 15px;
                    margin: 15px 0;
                    border-radius: 4px;
                }
                .hotspot-card h3, .theme-card h3 {
                    margin-top: 0;
                    color: #007bff;
                }
                code {
                    background: #f4f4f4;
                    padding: 2px 6px;
                    border-radius: 3px;
                    font-family: 'Courier New', monospace;
                    font-size: 13px;
                }
                select {
                    width: 100%;
                    padding: 8px;
                    border: 1px solid #ddd;
                    border-radius: 4px;
                    box-sizing: border-box;
                    font-size: 14px;
                }
                """;
    }
}
