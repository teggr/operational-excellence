package com.teggr.operationalexcellence.view;

import j2html.tags.specialized.DivTag;

import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;

public class GitHubViews {

    public static String repositoriesView(List<Map<String, Object>> repositories, String username) {
        DivTag bodyContent;
        
        if (repositories.isEmpty()) {
            bodyContent = div(
                    h1("GitHub Repositories"),
                    div(
                            p("Logged in as: " + username),
                            a("Back to Local Repositories").withClass("btn btn-secondary").withHref("/"),
                            form(
                                    button("Logout").withClass("btn btn-danger").withType("submit")
                            ).withMethod("post").withAction("/logout")
                    ).withClass("header-actions"),
                    div(
                            p("No repositories found.")
                    ).withClass("message-box")
            ).withClass("container");
        } else {
            bodyContent = div(
                    h1("GitHub Repositories"),
                    div(
                            p("Logged in as: " + username),
                            a("Back to Local Repositories").withClass("btn btn-secondary").withHref("/"),
                            form(
                                    button("Logout").withClass("btn btn-danger").withType("submit")
                            ).withMethod("post").withAction("/logout")
                    ).withClass("header-actions"),
                    table(
                            thead(
                                    tr(
                                            th("Name"),
                                            th("Description"),
                                            th("URL"),
                                            th("Private"),
                                            th("Language"),
                                            th("Updated")
                                    )
                            ),
                            tbody(
                                    each(repositories, repo -> {
                                        String name = (String) repo.getOrDefault("name", "N/A");
                                        String description = (String) repo.getOrDefault("description", "");
                                        String htmlUrl = (String) repo.getOrDefault("html_url", "#");
                                        Boolean isPrivate = (Boolean) repo.getOrDefault("private", false);
                                        String language = (String) repo.getOrDefault("language", "N/A");
                                        String updatedAt = (String) repo.getOrDefault("updated_at", "N/A");
                                        
                                        return tr(
                                                td(name),
                                                td(description != null && !description.isEmpty() ? description : "-"),
                                                td(a(htmlUrl).withHref(htmlUrl).withTarget("_blank")),
                                                td(isPrivate ? 
                                                    span("Private").withClass("status status-private") : 
                                                    span("Public").withClass("status status-public")
                                                ),
                                                td(language != null ? language : "-"),
                                                td(updatedAt)
                                        );
                                    })
                            )
                    )
            ).withClass("container");
        }
        
        return html(
                head(
                        title("GitHub Repositories - Operational Excellence"),
                        style("""
                                body {
                                    font-family: Arial, sans-serif;
                                    margin: 20px;
                                    background-color: #f5f5f5;
                                }
                                .container {
                                    max-width: 1400px;
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
                                .btn-primary {
                                    background-color: #007bff;
                                    color: white;
                                }
                                .btn-primary:hover {
                                    background-color: #0056b3;
                                }
                                .btn-secondary {
                                    background-color: #6c757d;
                                    color: white;
                                }
                                .btn-secondary:hover {
                                    background-color: #5a6268;
                                }
                                .btn-danger {
                                    background-color: #dc3545;
                                    color: white;
                                }
                                .btn-danger:hover {
                                    background-color: #c82333;
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
                                .status {
                                    padding: 4px 8px;
                                    border-radius: 4px;
                                    font-size: 12px;
                                    font-weight: bold;
                                }
                                .status-private {
                                    background-color: #f8d7da;
                                    color: #721c24;
                                }
                                .status-public {
                                    background-color: #d4edda;
                                    color: #155724;
                                }
                                .header-actions {
                                    margin-bottom: 20px;
                                }
                                .message-box {
                                    padding: 20px;
                                    margin: 20px 0;
                                    background-color: #f8f9fa;
                                    border: 1px solid #dee2e6;
                                    border-radius: 4px;
                                }
                                .message-box p {
                                    margin-bottom: 15px;
                                    color: #495057;
                                }
                                a {
                                    color: #007bff;
                                    text-decoration: none;
                                }
                                a:hover {
                                    text-decoration: underline;
                                }
                                """
                        )
                ),
                body(bodyContent)
        ).render();
    }
}
