package com.cdci.building;

import com.cdci.Messaging;
import com.cdci.database.PersistenceService;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.BuildImageResultCallback;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Building {
    private final Path path;

    public Building(Path path) {
        this.path = path;
    }

    public void build() {
        DefaultDockerClientConfig dockerBuild = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerClient docker = DockerClientBuilder.getInstance(dockerBuild).build();

        BuildImageCmd tag = docker.buildImageCmd(Paths.get(path.toAbsolutePath().toString(), ConfigRules.initialFileToBuild()).toFile());//.withNoCache(false);
        var consoleOutput = new ArrayList<String>();

        BuildImageResultCallback callback = new BuildImageResultCallback() {
            @Override
            public void onNext(BuildResponseItem item) {
                var stream = item.getStream();
                consoleOutput.add(stream);
                var messages = new Messaging();
                messages.sendMessage("current-builds", "12", stream == null ? "end" : stream);
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

        Build build = PersistenceService.newInstance().persist(new Build(0, path.toString(), true, String.join("\n", consoleOutput), new Date().getTime()));
        var messages = new Messaging();
        messages.sendMessage("builds", "build", build.toJson().encode());
    }

}
