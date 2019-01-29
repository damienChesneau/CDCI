package com.cdci.building;

import com.cdci.Messaging;

import java.nio.file.Paths;

public class BuildTrigger {

    public static void startListening() throws InterruptedException {
        Messaging messaging = new Messaging();
        messaging.consumeTopicMessage("git-trigger-v", "project-name", (s, s2) -> {
            Building building = new Building(Paths.get(s2));
            building.build();
        });
        System.out.println("Build trigger started.");
    }
}
