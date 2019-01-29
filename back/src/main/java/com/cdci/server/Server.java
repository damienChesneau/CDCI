package com.cdci.server;

import com.cdci.Messaging;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

import java.util.*;

public class Server {

    private final Vertx vertx = Vertx.vertx();
    private final EventBus eb = vertx.eventBus();

    private void serve() throws InterruptedException {
        ConfigureRouteMethods configureRouteMethods = new ConfigureRouteMethods().invoke();
        Set<String> allowedHeaders = configureRouteMethods.getAllowedHeaders();
        Set<HttpMethod> allowedMethods = configureRouteMethods.getAllowedMethods();

        SockJSHandler ebHandler = configureEventBus();
        BuildsRoutes buildsRoutes = new BuildsRoutes();
        Router router = setupRoutes(vertx, allowedHeaders, allowedMethods, ebHandler, buildsRoutes.createRoutes());
        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(router::accept).listen(8002);
    }

    private SockJSHandler configureEventBus() {
        BridgeOptions bridgeOptions = new BridgeOptions();
        List<String> filesNamesWithoutExtension = Arrays.asList(".js");
        List<PermittedOptions> po = new ArrayList<>();
        filesNamesWithoutExtension.forEach(s -> po.add(new PermittedOptions().setAddress(s)));
        bridgeOptions.setOutboundPermitted(po);

        BridgeOptions opts = new BridgeOptions().addOutboundPermitted(new PermittedOptions().setAddress("main-builds"));

        return SockJSHandler.create(vertx).bridge(opts);
    }

    private Router setupRoutes(Vertx vertx, Set<String> allowedHeaders, Set<HttpMethod> allowedMethods, SockJSHandler ebHandler, List<ServerRouter.ServerRoute> routes) {
        Router router = Router.router(vertx);
        router.route("/eventbus/*").handler(ebHandler);
        router.route().handler(CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethods(allowedMethods));

        routes.forEach(serverRoute -> router.get(serverRoute.getName()).handler(serverRoute.getRouteHandler()));
        return router;
    }

    private class ConfigureRouteMethods {
        private Set<String> allowedHeaders;
        private Set<HttpMethod> allowedMethods;

        public Set<String> getAllowedHeaders() {
            return allowedHeaders;
        }

        public Set<HttpMethod> getAllowedMethods() {
            return allowedMethods;
        }

        public ConfigureRouteMethods invoke() {
            allowedHeaders = new HashSet<>();
            allowedHeaders.add("x-requested-with");
            allowedHeaders.add("Access-Control-Allow-Origin");
            allowedHeaders.add("origin");
            allowedHeaders.add("Content-Type");
            allowedHeaders.add("accept");
            allowedHeaders.add("X-PINGARUNER");

            allowedMethods = new HashSet<>();
            allowedMethods.add(HttpMethod.GET);
            allowedMethods.add(HttpMethod.POST);
            allowedMethods.add(HttpMethod.OPTIONS);
            allowedMethods.add(HttpMethod.DELETE);
            allowedMethods.add(HttpMethod.PATCH);
            allowedMethods.add(HttpMethod.PUT);
            return this;
        }
    }

    public void run() throws InterruptedException {
        serve();
        System.out.println("Server started.");
        Messaging messaging = new Messaging();
        messaging.consumeTopicMessage("event-web", "main-builds", eb::publish);
    }
}
