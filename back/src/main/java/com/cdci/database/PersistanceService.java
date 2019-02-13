package com.cdci.database;

import com.cdci.building.Build;

import java.util.List;

public interface PersistanceService extends AutoCloseable {

    Build persist(Build build);

    List<Build> getAllBuilds();

    Build getBuildById(int id);

    Build updateProjectNameByProjectPath(String path, String name);

    static PersistanceService newInstance() {
        return new PersistanceImplNeo4J("bolt://localhost:7687", "neo4j", "password");
    }
}
