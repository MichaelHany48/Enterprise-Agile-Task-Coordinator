package com.asu.agile.database;

import com.asu.agile.entity.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private static final Logger logger = Logger.getLogger(Database.class.getName());

    private static List<User> users = new ArrayList<>();
    private static List<WorkItem> workItems = new ArrayList<>();
    private static List<Sprint> sprints = new ArrayList<>();

    // Gson instance with LocalDate support
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, _, _) ->
                    LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE))
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, _, _) ->
                    new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE)))
            .setPrettyPrinting()
            .create();

    // Add a user
    public static void addUser(User user) {
        users.add(user);
        saveData(); // save after change
    }

    public static List<User> getUsers() {
        return users;
    }

    public static void removeUser(User user) {
        users.remove(user);
        // Unassign work items assigned to this user
        for (WorkItem w : workItems) {
            if (user.equals(w.getAssignedTo())) {
                w.assignTo(null);
            }
        }
        saveData();
    }

    // Add a work item
    public static void addWorkItem(WorkItem workItem) {
        workItems.add(workItem);
        saveData();
    }

    public static List<WorkItem> getWorkItems() {
        return workItems;
    }

    public static void removeWorkItem(WorkItem workItem) {
        workItems.remove(workItem);
        // Remove from any sprint
        for (Sprint s : sprints) {
            s.getWorkItems().remove(workItem);
        }
        saveData();
    }

    // Add a sprint
    public static void addSprint(Sprint sprint) {
        sprints.add(sprint);
        saveData();
    }

    public static List<Sprint> getSprints() {
        return sprints;
    }

    public static void removeSprint(Sprint sprint) {
        sprints.remove(sprint);
        saveData();
    }

    // Save data to JSON files
    public static void saveData() {
        saveList("users.json", users);
        saveList("workitems.json", workItems);
        saveList("sprints.json", sprints);
    }

    private static <T> void saveList(String filename, List<T> list) {
        try (Writer writer = new FileWriter(filename)) {
            gson.toJson(list, writer);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save " + filename, e);
        }
    }

    // Load data from JSON files
    public static void loadData() {
        users = loadList("users.json", new TypeToken<ArrayList<User>>(){}.getType());
        workItems = loadList("workitems.json", new TypeToken<ArrayList<WorkItem>>(){}.getType());
        sprints = loadList("sprints.json", new TypeToken<ArrayList<Sprint>>(){}.getType());
    }

    private static <T> List<T> loadList(String filename, Type type) {
        try (Reader reader = new FileReader(filename)) {
            List<T> list = gson.fromJson(reader, type);
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to load " + filename + ", starting with empty list", e);
            return new ArrayList<>();
        }
    }
}
