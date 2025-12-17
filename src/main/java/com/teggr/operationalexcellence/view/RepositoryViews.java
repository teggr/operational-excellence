package com.teggr.operationalexcellence.view;

import com.teggr.operationalexcellence.model.GitRepository;
import com.teggr.operationalexcellence.service.GitRepositoryService;
import j2html.tags.specialized.DivTag;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static j2html.TagCreator.*;

public class RepositoryViews {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String listView(List<GitRepository> repositories, GitRepositoryService service) {
        DivTag bodyContent;
        
        if (repositories.isEmpty()) {
            bodyContent = div(
                    h1("Git Repository Management"),
                    div(
                            a("Add New Repository").withClass("btn btn-primary").withHref("/new")
                    ).withClass("header-actions"),
                    p("No repositories configured. Click 'Add New Repository' to get started.")
            ).withClass("container");
        } else {
            bodyContent = div(
                    h1("Git Repository Management"),
                    div(
                            a("Add New Repository").withClass("btn btn-primary").withHref("/new")
                    ).withClass("header-actions"),
                    table(
                            thead(
                                    tr(
                                            th("Name"),
                                            th("URL"),
                                            th("Local Path"),
                                            th("Status"),
                                            th("Last Updated"),
                                            th("Actions")
                                    )
                            ),
                            tbody(
                                    each(repositories, repo -> {
                                        boolean isCloned = service.isCloned(repo.getId());
                                        return tr(
                                                td(repo.getName()),
                                                td(repo.getUrl()),
                                                td(repo.getLocalPath()),
                                                td(
                                                        isCloned
                                                                ? span("Cloned").withClass("status status-cloned")
                                                                : span("Not Cloned").withClass("status status-not-cloned"),
                                                        iff(repo.getLastError() != null,
                                                                div(repo.getLastError()).withClass("error")
                                                        )
                                                ),
                                                td(repo.getLastUpdated() != null
                                                        ? repo.getLastUpdated().format(formatter)
                                                        : (repo.getLastCloned() != null
                                                        ? repo.getLastCloned().format(formatter)
                                                        : "-")),
                                                td(
                                                        div(
                                                                iff(!isCloned,
                                                                        form(
                                                                                button("Clone").withClass("btn btn-success").withType("submit")
                                                                        ).withMethod("post").withAction("/" + repo.getId() + "/clone")
                                                                ),
                                                                iff(isCloned,
                                                                        form(
                                                                                button("Update").withClass("btn btn-primary").withType("submit")
                                                                        ).withMethod("post").withAction("/" + repo.getId() + "/update")
                                                                ),
                                                                iff(isCloned,
                                                                        form(
                                                                                button("Clean").withClass("btn btn-warning").withType("submit")
                                                                        ).withMethod("post").withAction("/" + repo.getId() + "/clean")
                                                                ),
                                                                a("Edit").withClass("btn btn-secondary").withHref("/" + repo.getId() + "/edit"),
                                                                form(
                                                                        button("Delete").withClass("btn btn-danger").withType("submit")
                                                                                .attr("onclick", "return confirm('Are you sure you want to delete this repository?')")
                                                                ).withMethod("post").withAction("/" + repo.getId() + "/delete")
                                                        ).withClass("actions")
                                                )
                                        );
                                    })
                            )
                    )
            ).withClass("container");
        }
        
        return html(
                head(
                        title("Operational Excellence - Repository Management"),
                        style("""
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
                                .btn-success {
                                    background-color: #28a745;
                                    color: white;
                                }
                                .btn-success:hover {
                                    background-color: #218838;
                                }
                                .btn-warning {
                                    background-color: #ffc107;
                                    color: black;
                                }
                                .btn-warning:hover {
                                    background-color: #e0a800;
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
                                .status {
                                    padding: 4px 8px;
                                    border-radius: 4px;
                                    font-size: 12px;
                                    font-weight: bold;
                                }
                                .status-cloned {
                                    background-color: #d4edda;
                                    color: #155724;
                                }
                                .status-not-cloned {
                                    background-color: #f8d7da;
                                    color: #721c24;
                                }
                                .error {
                                    color: #dc3545;
                                    font-size: 12px;
                                    margin-top: 4px;
                                }
                                .actions {
                                    white-space: nowrap;
                                }
                                .header-actions {
                                    margin-bottom: 20px;
                                }
                                """
                        )
                ),
                body(bodyContent)
        ).render();
    }

    public static String formView(GitRepository repository) {
        boolean isEdit = repository != null;
        
        return html(
                head(
                        title(isEdit ? "Edit Repository" : "Add New Repository"),
                        style("""
                                body {
                                    font-family: Arial, sans-serif;
                                    margin: 20px;
                                    background-color: #f5f5f5;
                                }
                                .container {
                                    max-width: 600px;
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
                                .form-group {
                                    margin-bottom: 20px;
                                }
                                label {
                                    display: block;
                                    margin-bottom: 5px;
                                    font-weight: bold;
                                    color: #333;
                                }
                                input[type="text"] {
                                    width: 100%;
                                    padding: 8px;
                                    border: 1px solid #ddd;
                                    border-radius: 4px;
                                    box-sizing: border-box;
                                    font-size: 14px;
                                }
                                input[type="text"]:focus {
                                    outline: none;
                                    border-color: #007bff;
                                }
                                .btn {
                                    padding: 10px 20px;
                                    margin-right: 10px;
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
                                .form-actions {
                                    margin-top: 20px;
                                }
                                """
                        )
                ),
                body(
                        div(
                                h1(isEdit ? "Edit Repository" : "Add New Repository"),
                                form(
                                        isEdit ? input().withType("hidden").withName("id").withValue(String.valueOf(repository.getId())) : text(""),
                                        div(
                                                label("Name:").attr("for", "name"),
                                                input().withType("text")
                                                        .withName("name")
                                                        .withId("name")
                                                        .withValue(isEdit ? repository.getName() : "")
                                                        .isRequired()
                                        ).withClass("form-group"),
                                        div(
                                                label("Git URL:").attr("for", "url"),
                                                input().withType("text")
                                                        .withName("url")
                                                        .withId("url")
                                                        .withValue(isEdit ? repository.getUrl() : "")
                                                        .withPlaceholder("https://github.com/user/repo.git")
                                                        .isRequired()
                                        ).withClass("form-group"),
                                        div(
                                                button("Save").withClass("btn btn-primary").withType("submit"),
                                                a("Cancel").withClass("btn btn-secondary").withHref("/")
                                        ).withClass("form-actions")
                                ).withMethod("post").withAction("/")
                        ).withClass("container")
                )
        ).render();
    }
}
