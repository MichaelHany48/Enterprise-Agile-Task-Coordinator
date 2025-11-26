package com.asu.agile.entity;

public abstract class WorkItem {
    protected String id;
    protected String title;
    protected String status = "Open";
    protected User assignedTo;

    public WorkItem(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getStatus() { return status; }
    public User getAssignedTo() { return assignedTo; }

    public void assignTo(User user) { this.assignedTo = user; }
    public void changeStatus(String status) { this.status = status; }
}
