package com.asu.agile.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sprint represents a time-boxed iteration in Agile.
 * Each Sprint has an ID, start/end dates, a team of users, and associated work items.
 */
public class Sprint {

    private final String id;
    private final LocalDate startDate;
    private final LocalDate endDate;

    private final List<WorkItem> workItems = new ArrayList<>();
    private final List<User> team = new ArrayList<>();

    /**
     * Constructor to create a new Sprint
     *
     * @param id        Sprint ID
     * @param startDate Start date of the Sprint
     * @param endDate   End date of the Sprint
     */
    public Sprint(String id, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /** Get Sprint ID */
    public String getId() {
        return id;
    }

    /** Get Sprint start date */
    public LocalDate getStartDate() {
        return startDate;
    }

    /** Get Sprint end date */
    public LocalDate getEndDate() {
        return endDate;
    }

    /** Add a work item to this Sprint */
    public void addWorkItem(WorkItem item) {
        workItems.add(item);
    }

    /** Add a team member to this Sprint */
    public void addTeamMember(User user) {
        team.add(user);
    }

    /** Get all work items (read-only copy) */
    public List<WorkItem> getWorkItems() {
        return Collections.unmodifiableList(workItems);
    }

    /** Get all team members (read-only copy) */
    public List<User> getTeam() {
        return Collections.unmodifiableList(team);
    }

    /** Returns a string summary of the Sprint for debugging/display purposes */
    @Override
    public String toString() {
        return "Sprint{" +
                "id='" + id + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", team=" + team.size() + " members" +
                ", workItems=" + workItems.size() + " items" +
                '}';
    }
}
