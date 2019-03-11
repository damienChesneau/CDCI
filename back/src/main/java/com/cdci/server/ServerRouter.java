package com.cdci.server;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

class ServerRouter {
    public static class ServerRoute {
        private final String name;
        private final Handler<RoutingContext> routeHandler;

        public ServerRoute(String name, Handler<RoutingContext> routeHandler) {
            this.name = name;
            this.routeHandler = routeHandler;
        }

        public Handler<RoutingContext> getRouteHandler() {
            return routeHandler;
        }

        public String getName() {
            return name;
        }
    }

    static {
    }

}
