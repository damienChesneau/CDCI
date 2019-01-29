package com.cdci.database;

import com.cdci.building.Build;
import org.neo4j.driver.v1.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.neo4j.driver.v1.Values.parameters;

class PersistanceImplNeo4J implements PersistanceService {

    private final Driver driver;

    public PersistanceImplNeo4J(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() {
        driver.close();
    }

    @Override
    public Build persist(Build build) {
        try (Session session = driver.session()) {
            int buildId = session.writeTransaction((tx) -> {
                StatementResult result = tx.run("MERGE (p:Project{name: $project}) CREATE (b:Build)-[:of]->(p)  SET b.project=$project SET b.success=$success SET b.console=$console SET b.timestamp=$timestamp  RETURN id(b) as id ",
                        parameters("project", build.getProject(), "success", build.isSuccess(), "console", build.getConsole(), "timestamp", build.getTimestamp()));
                return result.single().get("id").asInt();
            });
            return build.setId(buildId);
        }
    }

    @Override
    public List<Build> getAllBuilds() {
        try (Session session = driver.session()) {
            return session.writeTransaction((tx) -> {
                StatementResult result = tx.run("Match (b:Build) return b.console as console, b.success as success, b.project as project, b.timestamp as timestamp, ID(b) as id ORDER BY b.timestamp ");
                return result.stream().map(record -> new Build(record.get("id").asInt(),
                        record.get("project").asString("null"),
                        record.get("success").asBoolean(false),
                        record.get("console").asString("null"), record.get("timestamp").asLong(0l))).collect(Collectors.toList());
            });
        }
    }

    @Override
    public Build getBuildById(int id) {
        try (Session session = driver.session()) {
            return session.writeTransaction((tx) -> {
                StatementResult result = tx.run("Match (b:Build) WHERE ID(b) = $id return b.console as console, b.success as success, b.project as project, ID(b) as id", parameters("id", id));
                Record record = result.single();
                return new Build(record.get("id").asInt(),
                        record.get("project").asString("null"),
                        record.get("success").asBoolean(false),
                        record.get("console").asString("null"), record.get("timestamp").asLong(0l));
            });
        }
    }
}