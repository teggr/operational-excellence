package com.teggr.operationalexcellence.view;

import com.teggr.operationalexcellence.domain.portfolio.Portfolio;
import j2html.tags.specialized.DivTag;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static j2html.TagCreator.*;

public class PortfolioViews {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String listView(List<Portfolio> portfolios) {
        DivTag bodyContent;
        
        if (portfolios.isEmpty()) {
            bodyContent = div(
                    h1("Portfolio Management"),
                    div(
                            a("Add New Portfolio").withClass("btn btn-primary").withHref("/portfolios/new")
                    ).withClass("header-actions"),
                    p("No portfolios configured. Click 'Add New Portfolio' to get started.")
            ).withClass("container");
        } else {
            bodyContent = div(
                    h1("Portfolio Management"),
                    div(
                            a("Add New Portfolio").withClass("btn btn-primary").withHref("/portfolios/new")
                    ).withClass("header-actions"),
                    table(
                            thead(
                                    tr(
                                            th("Name"),
                                            th("Description"),
                                            th("Repositories"),
                                            th("Created"),
                                            th("Actions")
                                    )
                            ),
                            tbody(
                                    each(portfolios, portfolio -> tr(
                                            td(portfolio.getName()),
                                            td(portfolio.getDescription() != null ? portfolio.getDescription() : ""),
                                            td(String.valueOf(portfolio.getRepositories().size())),
                                            td(portfolio.getCreatedAt() != null 
                                                    ? portfolio.getCreatedAt().format(formatter) 
                                                    : "-"),
                                            td(
                                                    div(
                                                            a("View").withClass("btn btn-primary").withHref("/portfolios/" + portfolio.getId()),
                                                            form(
                                                                    button("Delete").withClass("btn btn-danger").withType("submit")
                                                                            .attr("onclick", "return confirm('Are you sure you want to delete this portfolio?')")
                                                            ).withMethod("post").withAction("/portfolios/" + portfolio.getId() + "/delete")
                                                    ).withClass("actions")
                                            )
                                    ))
                            )
                    )
            ).withClass("container");
        }
        
        return html(
                head(
                        title("Operational Excellence - Portfolio Management"),
                        style(getCommonStyles())
                ),
                body(bodyContent)
        ).render();
    }

    public static String formView(Portfolio portfolio) {
        boolean isEdit = portfolio != null;
        
        return html(
                head(
                        title(isEdit ? "Edit Portfolio" : "Add New Portfolio"),
                        style(getCommonStyles())
                ),
                body(
                        div(
                                h1(isEdit ? "Edit Portfolio" : "Add New Portfolio"),
                                form(
                                        isEdit ? input().withType("hidden").withName("id").withValue(portfolio.getId().toString()) : text(""),
                                        div(
                                                label("Name:").attr("for", "name"),
                                                input().withType("text")
                                                        .withName("name")
                                                        .withId("name")
                                                        .withValue(isEdit ? portfolio.getName() : "")
                                                        .isRequired()
                                        ).withClass("form-group"),
                                        div(
                                                label("Description:").attr("for", "description"),
                                                textarea()
                                                        .withName("description")
                                                        .withId("description")
                                                        .withText(isEdit && portfolio.getDescription() != null ? portfolio.getDescription() : "")
                                        ).withClass("form-group"),
                                        div(
                                                button("Save").withClass("btn btn-primary").withType("submit"),
                                                a("Cancel").withClass("btn btn-secondary").withHref("/portfolios")
                                        ).withClass("form-actions")
                                ).withMethod("post").withAction("/portfolios")
                        ).withClass("container")
                )
        ).render();
    }

    public static String detailView(Portfolio portfolio) {
        return html(
                head(
                        title("Portfolio: " + portfolio.getName()),
                        style(getCommonStyles())
                ),
                body(
                        div(
                                h1("Portfolio: " + portfolio.getName()),
                                div(
                                        a("Back to Portfolios").withClass("btn btn-secondary").withHref("/portfolios"),
                                        a("Add Repository").withClass("btn btn-primary").withHref("/portfolios/" + portfolio.getId() + "/repositories/new"),
                                        a("Create Snapshot").withClass("btn btn-success").withHref("/portfolios/" + portfolio.getId() + "/snapshots/new")
                                ).withClass("header-actions"),
                                div(
                                        p(strong("Description: "), text(portfolio.getDescription() != null ? portfolio.getDescription() : "None")),
                                        p(strong("Created: "), text(portfolio.getCreatedAt().format(formatter)))
                                ).withClass("detail-section"),
                                h2("Repositories"),
                                portfolio.getRepositories().isEmpty() 
                                        ? p("No repositories added yet.")
                                        : ul(
                                                each(portfolio.getRepositories(), repo -> li(
                                                        text(repo),
                                                        form(
                                                                button("Remove").withClass("btn btn-sm btn-danger").withType("submit")
                                                        ).withMethod("post").withAction("/portfolios/" + portfolio.getId() + "/repositories/remove").withStyle("display:inline;margin-left:10px;")
                                                        .with(input().withType("hidden").withName("url").withValue(repo))
                                                ))
                                        )
                        ).withClass("container")
                )
        ).render();
    }

    public static String repositoryFormView(Portfolio portfolio) {
        return html(
                head(
                        title("Add Repository to " + portfolio.getName()),
                        style(getCommonStyles())
                ),
                body(
                        div(
                                h1("Add Repository"),
                                form(
                                        div(
                                                label("Repository URL:").attr("for", "url"),
                                                input().withType("text")
                                                        .withName("url")
                                                        .withId("url")
                                                        .withPlaceholder("https://github.com/user/repo.git")
                                                        .isRequired()
                                        ).withClass("form-group"),
                                        div(
                                                button("Add").withClass("btn btn-primary").withType("submit"),
                                                a("Cancel").withClass("btn btn-secondary").withHref("/portfolios/" + portfolio.getId())
                                        ).withClass("form-actions")
                                ).withMethod("post").withAction("/portfolios/" + portfolio.getId() + "/repositories")
                        ).withClass("container")
                )
        ).render();
    }

    public static String getCommonStyles() {
        return """
                body {
                    font-family: Arial, sans-serif;
                    margin: 20px;
                    background-color: #f5f5f5;
                }
                .container {
                    max-width: 1200px;
                    margin: 0 auto;
                    background-color: white;
                    padding: 20px;
                    border-radius: 8px;
                    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                }
                h1 {
                    color: #333;
                    border-bottom: 2px solid #007bff;
                    padding-bottom: 10px;
                }
                h2 {
                    color: #555;
                    margin-top: 30px;
                }
                .btn {
                    padding: 8px 16px;
                    margin: 4px 2px;
                    border: none;
                    border-radius: 4px;
                    cursor: pointer;
                    text-decoration: none;
                    display: inline-block;
                    font-size: 14px;
                }
                .btn-sm {
                    padding: 4px 8px;
                    font-size: 12px;
                }
                .btn-primary {
                    background-color: #007bff;
                    color: white;
                }
                .btn-primary:hover {
                    background-color: #0056b3;
                }
                .btn-success {
                    background-color: #28a745;
                    color: white;
                }
                .btn-success:hover {
                    background-color: #218838;
                }
                .btn-danger {
                    background-color: #dc3545;
                    color: white;
                }
                .btn-danger:hover {
                    background-color: #c82333;
                }
                .btn-secondary {
                    background-color: #6c757d;
                    color: white;
                }
                .btn-secondary:hover {
                    background-color: #5a6268;
                }
                table {
                    width: 100%;
                    border-collapse: collapse;
                    margin-top: 20px;
                }
                th, td {
                    padding: 12px;
                    text-align: left;
                    border-bottom: 1px solid #ddd;
                }
                th {
                    background-color: #007bff;
                    color: white;
                }
                tr:hover {
                    background-color: #f5f5f5;
                }
                .actions {
                    white-space: nowrap;
                }
                .header-actions {
                    margin-bottom: 20px;
                }
                .form-group {
                    margin-bottom: 20px;
                }
                label {
                    display: block;
                    margin-bottom: 5px;
                    font-weight: bold;
                    color: #333;
                }
                input[type="text"], textarea {
                    width: 100%;
                    padding: 8px;
                    border: 1px solid #ddd;
                    border-radius: 4px;
                    box-sizing: border-box;
                    font-size: 14px;
                }
                textarea {
                    min-height: 100px;
                    font-family: Arial, sans-serif;
                }
                input[type="text"]:focus, textarea:focus {
                    outline: none;
                    border-color: #007bff;
                }
                .form-actions {
                    margin-top: 20px;
                }
                .detail-section {
                    margin: 20px 0;
                }
                ul {
                    list-style: none;
                    padding: 0;
                }
                li {
                    padding: 10px;
                    border-bottom: 1px solid #eee;
                }
                """;
    }
}
