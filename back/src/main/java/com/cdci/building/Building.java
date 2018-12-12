package com.cdci.building;

import com.cdci.Messaging;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Building {
    private final Path path;

    public Building(Path path) {
        this.path = path;
    }

    public void build() {
        DefaultDockerClientConfig build = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerClient docker = DockerClientBuilder.getInstance(build).build();

        BuildImageCmd tag = docker.buildImageCmd(path.toFile());//.withNoCache(false);
        var consoleOutput = new ArrayList<String>();

        BuildImageResultCallback callback = new BuildImageResultCallback() {
            @Override
            public void onNext(BuildResponseItem item) {
                var stream = item.getStream();
                consoleOutput.add(stream);
                var messsages = new Messaging();
                messsages.sendMessage("current-builds", "12", stream == null ? "end" : stream);
                super.onNext(item);
            }
        };

        String s = tag.exec(callback).awaitImageId(20, TimeUnit.SECONDS);
        docker.removeImageCmd(s).exec();
        tag.close();

        try {
            callback.close();
            docker.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        JsonObject jsonReturn = new JsonObject();
        var i = new SecureRandom().nextInt();
        jsonReturn.put("id", i);
        jsonReturn.put("name", "Build #" + i);
        jsonReturn.put("working", "images/Cherry.png");
        jsonReturn.put("color", new SecureRandom().nextBoolean() ? "green" : "red");
        jsonReturn.put("description", String.join("", consoleOutput));
        var messsages = new Messaging();
        messsages.sendMessage("builds", "build", jsonReturn.encode());
    }

}
