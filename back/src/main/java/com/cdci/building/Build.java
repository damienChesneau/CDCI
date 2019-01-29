package com.cdci.building;

import io.vertx.core.json.JsonObject;

public class Build {

    private final int id;
    private final String project;
    private final boolean success;
    private final String console;
    private final long timestamp;

    public Build(int id, String project, boolean success, String console, long timestamp) {
        this.id = id;
        this.project = project;
        this.success = success;
        this.console = console;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getConsole() {
        return console;
    }

    public String getProject() {
        return project;
    }

    public boolean isSuccess() {
        return success;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Build setId(int id) {
        return new Build(id, this.project, this.success, this.console, timestamp);
    }

    @Override
    public String toString() {
        return "Id:" + id + " project:" + project + " success:" + success + " console:" + console;
    }

    public JsonObject toJson() {
        JsonObject jsonReturn = new JsonObject();
        jsonReturn.put("id", id);
        jsonReturn.put("success", success);
        jsonReturn.put("console", console);
        jsonReturn.put("project", project);
        jsonReturn.put("timestamp", timestamp);
        return jsonReturn;
    }

    public static Build fromJson(String jsonAsString) {
        var jsonObject = new JsonObject(jsonAsString);
        return new Build(jsonObject.getInteger("id"), jsonObject.getString("project"), jsonObject.getBoolean("success"), jsonObject.getString("console"), jsonObject.getLong("timestamp", 0l));
    }
}
