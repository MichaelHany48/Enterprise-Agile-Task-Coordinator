package com.asu.agile.entity;

import java.util.ArrayList;
import java.util.List;

public class Story extends WorkItem {
    private final List<Task> tasks = new ArrayList<>();

    public Story(String id, String title) {
        super(id, title);
    }

    public void addTask(Task task) { tasks.add(task); }
    public List<Task> getTasks() { return tasks; }
}
