package com.cdci.building;

import com.cdci.Messaging;
import com.cdci.database.PersistanceService;
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


//        var jsonReturn = new JsonObject();
//        var i = new SecureRandom().nextInt();
//        jsonReturn.put("id", i);
//        jsonReturn.put("name", "Build #" + i);
//        jsonReturn.put("working", "images/Cherry.png");
//        jsonReturn.put("color", new SecureRandom().nextBoolean() ? "green" : "red");
//        jsonReturn.put("description", String.join("", consoleOutput));

        Build build = PersistanceService.newInstance().persist(new Build(0, path.toString(), true, String.join("\n", consoleOutput), new Date().getTime()));
        var messsages = new Messaging();
        messsages.sendMessage("builds", "build", build.toJson().encode());
    }

}
