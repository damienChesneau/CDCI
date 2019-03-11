package com.cdci;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class GitTrigger {

    private final String remoteUri;
    private final Git localRepo;
    private final Thread thread = new Thread(this::watchRun);

    private String lastCommit;

    public static GitTrigger newInstance(String remoteUri) throws IOException, GitAPIException {
        File localDir = Files.createTempDirectory("local").toFile();
        Git localRepo = Git.cloneRepository().setURI(remoteUri).setDirectory(localDir).call();
        return new GitTrigger(remoteUri, localRepo);
    }

    private GitTrigger(String remoteUri, Git localRepo) {
        this.remoteUri = remoteUri;
        this.localRepo = localRepo;
    }

    private void init() throws GitAPIException {
        this.lastCommit = getLastCommitId();
    }

    private String getLastCommitId() throws GitAPIException {
        RevCommit firstCommit = localRepo.log().call().iterator().next();
        return firstCommit.getId().toString();
    }

    private void watchRun() {
        try {
            while (!Thread.interrupted()) {
                localRepo.pull().call();
                String lastCommit = getLastCommitId();
                if (!lastCommit.equals(this.lastCommit)) {
                    Messaging messaging = new Messaging();
                    messaging.sendMessage("git-trigger-v", "project-name", localRepo.getRepository().getDirectory().toPath().toAbsolutePath().getParent().toString());
                    this.lastCommit = lastCommit;
                }
                Thread.sleep(1000);
            }
        } catch (GitAPIException e) {
            e.printStackTrace();
            System.err.println();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void watch() {
        thread.start();
    }

}
