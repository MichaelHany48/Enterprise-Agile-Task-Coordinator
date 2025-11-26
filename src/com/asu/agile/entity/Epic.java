package com.asu.agile.entity;

import java.util.ArrayList;
import java.util.List;

public class Epic extends WorkItem {
    private final List<Story> stories = new ArrayList<>();

    public Epic(String id, String title) {
        super(id, title);
    }

    public void addStory(Story story) { stories.add(story); }
    public List<Story> getStories() { return stories; }
}
