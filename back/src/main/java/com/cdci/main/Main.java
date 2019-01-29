package com.cdci.main;

import com.cdci.GitTrigger;
import com.cdci.building.BuildTrigger;
import com.cdci.server.Server;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        PropertyConfigurator.configure(Files.newInputStream(Paths.get("./back/src/main/resources/log4j.properties")));
        try {
            GitTrigger gitTrigger = GitTrigger.newInstance("/tmp/test2495206229014264434/");
            gitTrigger.watch();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        BuildTrigger.startListening();

        new Server().run();
    }
}
