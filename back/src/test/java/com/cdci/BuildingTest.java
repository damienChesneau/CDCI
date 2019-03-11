package com.cdci;

import com.cdci.building.Build;
import com.cdci.building.Building;
import com.cdci.database.PersistenceService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BuildingTest {

//    @Test
    public void doBuild() throws InterruptedException {
//        new Thread(() -> {
//            Building b = new Building(Paths.get("/root/CDCI/back/Dockerfile"));
//            b.build();
//        }).start();
        new Messaging().sendMessage("trigger-build", "12", "do it");
        System.out.println("hi");
        new Messaging().consumeTopicMessage("current-builds", "12", (s, s2) -> {
            System.out.println(s2);
            if (s2.contains("end")) {
                return;
            }
        });

    }

//    @Test
    public void testDB() {
        Build build = new Build(12, "monbeauprojet", true, "Output", new Date().getTime());
        Build persist = PersistenceService.newInstance().persist(build);
        System.out.println(persist.getId());

        List<Build> allBuilds = PersistenceService.newInstance().getAllBuilds();
        System.out.println(allBuilds);

        Build buildById = PersistenceService.newInstance().getBuildById(43);
        System.out.println(buildById);
    }

    /**
     * @throws IOException
     * @throws GitAPIException
     * @throws InterruptedException
     */
//    @Test
    public void test() throws IOException, GitAPIException, InterruptedException {
        Path test = Files.createTempDirectory("test");
        Git remote = Git.init().setDirectory(test.toFile()).call();
        Path dockerfile1 = Files.createFile(Path.of(test.toAbsolutePath().toString(), "Dockerfile"));
        Path dockerfile2 = Paths.get("Dockerfile");
        Files.writeString(dockerfile1.toAbsolutePath(), Files.lines(dockerfile2).collect(Collectors.joining("\n")), StandardOpenOption.WRITE);
        remote.add().addFilepattern("Dockerfile").call();
        remote.commit().setAll(true).setCommitter("Damien Chesneau", "mon@mail.com").setMessage("Fisrt commit.").call();
        System.out.println(test.toAbsolutePath().toString());
        String remoteUri = remote.getRepository().getDirectory().getCanonicalPath();
        System.out.println("remote:" + remoteUri);
        File localDir = Files.createTempDirectory("local").toFile();
        Git call = Git.cloneRepository().setURI(remoteUri).setDirectory(localDir).call();
        RevCommit next = call.log().call().iterator().next();
        Path dockerfile = Paths.get(localDir.toPath().toAbsolutePath().toString(), "Dockerfile");
        Files.lines(dockerfile).forEach(System.out::println);

        if (true) {
            new Building(localDir.toPath());
        }
    }

    /**
     * @throws IOException
     * @throws GitAPIException
     * @throws InterruptedException
     */
    @Test
    public void addNewCommit() throws IOException, GitAPIException, InterruptedException {
        var remoteUrl = "/tmp/test2495206229014264434/";
        File localDir = Files.createTempDirectory("local").toFile();
        Git repo = Git.cloneRepository().setURI(remoteUrl).setDirectory(localDir).call();
        Path fileTest = Files.createFile(Path.of(localDir.toPath().toAbsolutePath().toString(), "test" + new SecureRandom().nextInt()));
        repo.add().addFilepattern(fileTest.getFileName().toString()).call();
        repo.commit().setAll(true).setCommitter("Damien Chesneau", "mon@mail.com").setMessage("Fisrt commit.").call();
        repo.push().call();
    }

    @Test
    public void doCoucou() {
        System.out.println("coucou");
    }

}
