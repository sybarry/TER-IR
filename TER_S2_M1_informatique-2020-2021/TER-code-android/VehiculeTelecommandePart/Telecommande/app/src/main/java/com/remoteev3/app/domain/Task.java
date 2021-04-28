package com.remoteev3.app.domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String timestamp;
    private String effectiveTimestamp;
    private int status;

    public Task(String title, String timestamp, String effectiveTimestamp, int status) {
        this.title = title;
        this.timestamp = timestamp;
        this.effectiveTimestamp = effectiveTimestamp;
        this.status = status;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getEffectiveTimestamp() {
        return effectiveTimestamp;
    }

    public int getStatus() {
        return status;
    }

}