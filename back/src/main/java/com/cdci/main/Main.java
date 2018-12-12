package com.cdci.main;

import com.cdci.Messaging;
import com.cdci.building.Building;
import com.cdci.server.Server;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        PropertyConfigurator.configure(Files.newInputStream(Paths.get("./back/src/main/resources/log4j.properties")));

        Building b = new Building(Paths.get("/root/CDCI/back/Dockerfile"));

        Thread a = new Thread(() -> {
            while (!Thread.interrupted()) {
                var mess = new Messaging();
                mess.consumeTopicMessage("trigger-build", "12", (s, s2) -> b.build());
            }
        });
        a.start();

        new Server().run();
        System.out.println("Trying to build...");
    }
}
