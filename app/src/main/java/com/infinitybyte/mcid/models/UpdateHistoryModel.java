package com.infinitybyte.mcid.models;

public class UpdateHistoryModel {
    String version, changelog;

    public UpdateHistoryModel (){}

    public UpdateHistoryModel(String version, String changelog) {
        this.version = version;
        this.changelog = changelog;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }
}
