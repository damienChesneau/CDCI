package com.cdci.server;

import com.cdci.Messaging;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class BuildsRoutes {
    private final JsonArray builds = new JsonArray();

    public List<ServerRouter.ServerRoute> createRoutes() {
        List<ServerRouter.ServerRoute> routes = new ArrayList<>();
        routes.add(new ServerRouter.ServerRoute("/builds", event -> {
            event.response().putHeader("content-type", "application/json");
            JsonObject az = new JsonObject();
            az.put("builds", this.builds);
            event.response().end(az.encode());
        }));
        routes.add(new ServerRouter.ServerRoute("/build/:id", event -> {
            String id = event.pathParam("id");
            for (int i = 0; i < this.builds.size(); i++) {
                JsonObject jsonObject = this.builds.getJsonObject(i);
                int id1 = jsonObject.getInteger("id");
                if ((id1 + "").equals(id)) {
                    event.response().end(jsonObject.encode());
                    break;
                }
            }
            event.response().end("{error: \"NOT-FOUND\"}");
        }));
        new Thread(() -> {
            Messaging messaging = new Messaging();
            messaging.consumeTopicMessage("builds", "build", (s, s2) -> {
                builds.add(new JsonObject(s2));
                JsonObject az = new JsonObject();
                az.put("builds", builds);
                Messaging messaging2 = new Messaging();
                messaging2.sendMessage("event-web", "main-builds", az.encode());
            });
        }).start();

        System.out.println("End");
        return routes;
    }
//
//    public void sendMessage() {
//        JsonObject az = new JsonObject();
//        az.put("builds", builds);
//
//        eb.publish("main-builds", az.encode());
//    }

    public void run() {

    }
}
