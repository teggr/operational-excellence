# operational-excellence
Application to help deliver operation excellence over multiple code repositories

## Dependency Management

This project uses the [Versions Maven Plugin](https://www.mojohaus.org/versions/versions-maven-plugin/) to check for outdated dependencies and generate reports.

### Checking for Dependency Updates

To check for all available dependency updates:
```bash
./mvnw versions:display-dependency-updates
```

To check for only patch/incremental updates (excludes minor and major version updates):
```bash
./mvnw versions:display-dependency-updates -DallowMajorUpdates=false -DallowMinorUpdates=false
```

### Generating Dependency Reports

To generate HTML and XML reports of available dependency updates:
```bash
./mvnw versions:dependency-updates-report
```

The reports will be generated in:
- **HTML Report**: `target/dependency-updates-report.html` - Human-readable report with full details
- **XML Report**: `target/dependency-updates.xml` - Machine-readable report for automation

The reports include:
- Current dependency versions
- Available patch (incremental) updates
- Available minor version updates
- Available major version updates
- Excludes pre-release versions (alpha, beta, RC, milestones)

### Applying Dependency Updates

To update to the latest patch version of a dependency, modify the version in `pom.xml` and run the full build to ensure compatibility:
```bash
./mvnw clean verify
```

This will download the updated dependencies, compile the code, run all tests, and verify the build is successful.
