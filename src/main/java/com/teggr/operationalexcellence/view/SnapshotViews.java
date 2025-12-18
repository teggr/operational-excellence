package com.teggr.operationalexcellence.view;

import com.teggr.operationalexcellence.domain.snapshot.Snapshot;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static j2html.TagCreator.*;

public class SnapshotViews {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String listView(List<Snapshot> snapshots) {
        return html(
                head(
                        title("Snapshots"),
                        style(PortfolioViews.getCommonStyles())
                ),
                body(
                        div(
                                h1("Snapshots"),
                                div(
                                        a("Back to Portfolios").withClass("btn btn-secondary").withHref("/portfolios")
                                ).withClass("header-actions"),
                                snapshots.isEmpty()
                                        ? p("No snapshots available.")
                                        : table(
                                                thead(
                                                        tr(
                                                                th("Portfolio"),
                                                                th("Created"),
                                                                th("Repositories"),
                                                                th("Actions")
                                                        )
                                                ),
                                                tbody(
                                                        each(snapshots, snapshot -> tr(
                                                                td(snapshot.getPortfolio().getName()),
                                                                td(snapshot.getCreatedAt().format(formatter)),
                                                                td(String.valueOf(snapshot.getCommitMap().size())),
                                                                td(
                                                                        div(
                                                                                a("View").withClass("btn btn-primary").withHref("/snapshots/" + snapshot.getId()),
                                                                                a("Create Exploration").withClass("btn btn-success").withHref("/snapshots/" + snapshot.getId() + "/explorations/new")
                                                                        ).withClass("actions")
                                                                )
                                                        ))
                                                )
                                        )
                        ).withClass("container")
                )
        ).render();
    }

    public static String detailView(Snapshot snapshot) {
        return html(
                head(
                        title("Snapshot Details"),
                        style(PortfolioViews.getCommonStyles())
                ),
                body(
                        div(
                                h1("Snapshot Details"),
                                div(
                                        a("Back to Portfolio").withClass("btn btn-secondary").withHref("/portfolios/" + snapshot.getPortfolio().getId()),
                                        a("Create Exploration").withClass("btn btn-success").withHref("/snapshots/" + snapshot.getId() + "/explorations/new")
                                ).withClass("header-actions"),
                                div(
                                        p(strong("Portfolio: "), text(snapshot.getPortfolio().getName())),
                                        p(strong("Created: "), text(snapshot.getCreatedAt().format(formatter))),
                                        p(strong("Environment: "), text(snapshot.getEnvironment() != null ? snapshot.getEnvironment() : "N/A"))
                                ).withClass("detail-section"),
                                h2("Repository Commits"),
                                snapshot.getCommitMap().isEmpty()
                                        ? p("No repository commits recorded.")
                                        : table(
                                                thead(
                                                        tr(
                                                                th("Repository"),
                                                                th("Commit Hash")
                                                        )
                                                ),
                                                tbody(
                                                        each(snapshot.getCommitMap().entrySet(), entry -> tr(
                                                                td(entry.getKey()),
                                                                td(code(entry.getValue()))
                                                        ))
                                                )
                                        )
                        ).withClass("container")
                )
        ).render();
    }
}
