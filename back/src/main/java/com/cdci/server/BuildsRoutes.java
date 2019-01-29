package com.cdci.server;

import com.cdci.Messaging;
import com.cdci.building.Build;
import com.cdci.database.PersistanceService;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class BuildsRoutes {

    public List<ServerRouter.ServerRoute> createRoutes() throws InterruptedException {
        List<ServerRouter.ServerRoute> routes = new ArrayList<>();
        routes.add(new ServerRouter.ServerRoute("/builds", event -> {
            JsonArray builds = new JsonArray();
            List<Build> allBuilds = PersistanceService.newInstance().getAllBuilds();
            for (Build b: allBuilds){
                builds.add(b.toJson());
            }
            event.response().putHeader("content-type", "application/json");
            JsonObject az = new JsonObject();
            az.put("builds", builds);
            event.response().end(az.encode());
        }));
        routes.add(new ServerRouter.ServerRoute("/build/:id", event -> {
            String id = event.pathParam("id");
//            for (int i = 0; i < builds.size(); i++) {
//                JsonObject jsonObject = this.builds.getJsonObject(i);
//                int id1 = jsonObject.getInteger("id");
//                if ((id1 + "").equals(id)) {
//                    event.response().end(jsonObject.encode());
//                    break;
//                }
//            }
            event.response().end("{error: \"NOT-FOUND\"}");
        }));
        Messaging messaging = new Messaging();
        messaging.consumeTopicMessage("builds", "build", (s, s2) -> {
            Messaging messaging2 = new Messaging();
            messaging2.sendMessage("event-web", "main-builds", Build.fromJson(s2).toJson().encode());
        });
        return routes;
    }
}
