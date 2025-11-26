package com.asu.agile.main;

import com.asu.agile.entity.*;
import com.asu.agile.database.Database;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Database.loadData();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Agile Task Coordinator ===");
            System.out.println("1. Create User");
            System.out.println("2. Create Work Item");
            System.out.println("3. Assign Work Item");
            System.out.println("4. Change Work Item Status");
            System.out.println("5. Create Sprint");
            System.out.println("6. Show Users");
            System.out.println("7. Show Work Items");
            System.out.println("8. Show Sprints");
            System.out.println("9. Remove User");
            System.out.println("10. Remove Work Item");
            System.out.println("11. Remove Sprint");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> createUser(scanner);
                case 2 -> createWorkItem(scanner);
                case 3 -> assignWorkItem(scanner);
                case 4 -> changeWorkItemStatus(scanner);
                case 5 -> createSprint(scanner);
                case 6 -> showUsers();
                case 7 -> showWorkItems();
                case 8 -> showSprints();
                case 9 -> removeUser(scanner);
                case 10 -> removeWorkItem(scanner);
                case 11 -> removeSprint(scanner);
                case 0 -> {
                    running = false;
                    Database.saveData();
                    System.out.println("Data saved. Exiting...");
                }
                default -> System.out.println("Invalid choice!");
            }
        }

        scanner.close();
    }

    private static void createUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        User user;
        while (true) {
            System.out.print("Enter role (Developer, QAEngineer, Stakeholder, ScrumMaster): ");
            String role = scanner.nextLine().trim().toLowerCase();
            switch (role) {
                case "developer" -> user = new Developer(username, password);
                case "qaengineer" -> user = new QAEngineer(username, password);
                case "stakeholder" -> user = new Stakeholder(username, password);
                case "scrummaster" -> user = new ScrumMaster(username, password);
                default -> {
                    System.out.println("Invalid role! Try again.");
                    continue;
                }
            }
            Database.addUser(user);
            Database.saveData();
            System.out.println("User " + username + " created successfully as " + user.getRole() + ".");
            break;
        }
    }

    private static void createWorkItem(Scanner scanner) {
        System.out.print("Enter work item type (Task, Bug, Story, Epic): ");
        String type = scanner.nextLine().trim().toLowerCase();
        System.out.print("Enter title: ");
        String title = scanner.nextLine().trim();
        WorkItem item;
        switch (type) {
            case "task" -> item = new Task("T" + (Database.getWorkItems().size() + 1), title);
            case "bug" -> item = new Bug("B" + (Database.getWorkItems().size() + 1), title);
            case "story" -> item = new Story("S" + (Database.getWorkItems().size() + 1), title);
            case "epic" -> item = new Epic("E" + (Database.getWorkItems().size() + 1), title);
            default -> {
                System.out.println("Invalid type!");
                return;
            }
        }
        Database.addWorkItem(item);
        Database.saveData();
        System.out.println(type.substring(0, 1).toUpperCase() + type.substring(1) + " \"" + title + "\" created.");
    }

    private static void assignWorkItem(Scanner scanner) {
        List<WorkItem> workItems = Database.getWorkItems();
        List<User> users = Database.getUsers();
        if (workItems.isEmpty() || users.isEmpty()) {
            System.out.println("No work items or users available.");
            return;
        }

        System.out.println("Select work item to assign:");
        for (int i = 0; i < workItems.size(); i++)
            System.out.println((i + 1) + ". " + workItems.get(i).getTitle() + " [" + workItems.get(i).getStatus() + "]");
        int wIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (wIndex < 0 || wIndex >= workItems.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        System.out.println("Select user to assign:");
        for (int i = 0; i < users.size(); i++)
            System.out.println((i + 1) + ". " + users.get(i).getUsername() + " (" + users.get(i).getRole() + ")");
        int uIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (uIndex < 0 || uIndex >= users.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        workItems.get(wIndex).assignTo(users.get(uIndex));
        Database.saveData();
        System.out.println("Work item assigned successfully.");
    }

    private static void changeWorkItemStatus(Scanner scanner) {
        List<WorkItem> workItems = Database.getWorkItems();
        if (workItems.isEmpty()) {
            System.out.println("No work items.");
            return;
        }

        System.out.println("Select work item to change status:");
        for (int i = 0; i < workItems.size(); i++)
            System.out.println((i + 1) + ". " + workItems.get(i).getTitle() + " [" + workItems.get(i).getStatus() + "]");
        int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (index < 0 || index >= workItems.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        System.out.print("Enter new status (Open, In Progress, Done): ");
        String status = scanner.nextLine().trim();
        workItems.get(index).changeStatus(status);
        Database.saveData();
        System.out.println("Status updated.");
    }

    private static void createSprint(Scanner scanner) {
        System.out.print("Enter sprint ID: ");
        String id = scanner.nextLine().trim();
        Sprint sprint = new Sprint(id, LocalDate.now(), LocalDate.now().plusDays(14));

        System.out.println("Add users to sprint team:");
        for (User u : Database.getUsers()) {
            System.out.print("Add " + u.getUsername() + "? (y/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) sprint.addTeamMember(u);
        }

        System.out.println("Add work items to sprint:");
        for (WorkItem w : Database.getWorkItems()) {
            System.out.print("Add " + w.getTitle() + "? (y/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) sprint.addWorkItem(w);
        }

        Database.addSprint(sprint);
        Database.saveData();
        System.out.println("Sprint created successfully.");
    }

    private static void showUsers() {
        System.out.println("=== USERS ===");
        for (User u : Database.getUsers()) {
            System.out.println(u.getUsername() + " (" + u.getRole() + ")");
        }
    }

    private static void showWorkItems() {
        System.out.println("=== WORK ITEMS ===");
        for (WorkItem w : Database.getWorkItems()) {
            String assigned = (w.getAssignedTo() != null) ? w.getAssignedTo().getUsername() : "Unassigned";
            System.out.println(w.getTitle() + " [" + w.getStatus() + "] Assigned to: " + assigned);
        }
    }

    private static void showSprints() {
        System.out.println("=== SPRINTS ===");
        for (Sprint s : Database.getSprints()) {
            System.out.println("Sprint " + s.getId() + " (" + s.getStartDate() + " to " + s.getEndDate() + ")");
        }
    }

    private static void removeUser(Scanner scanner) {
        List<User> users = Database.getUsers();
        if (users.isEmpty()) {
            System.out.println("No users to remove.");
            return;
        }
        System.out.println("Select user to remove:");
        for (int i = 0; i < users.size(); i++) System.out.println((i + 1) + ". " + users.get(i).getUsername());
        int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (index < 0 || index >= users.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        Database.removeUser(users.get(index));
        Database.saveData();
        System.out.println("User removed.");
    }

    private static void removeWorkItem(Scanner scanner) {
        List<WorkItem> items = Database.getWorkItems();
        if (items.isEmpty()) {
            System.out.println("No work items to remove.");
            return;
        }
        System.out.println("Select work item to remove:");
        for (int i = 0; i < items.size(); i++) System.out.println((i + 1) + ". " + items.get(i).getTitle());
        int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (index < 0 || index >= items.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        Database.removeWorkItem(items.get(index));
        Database.saveData();
        System.out.println("Work item removed.");
    }

    private static void removeSprint(Scanner scanner) {
        List<Sprint> sprints = Database.getSprints();
        if (sprints.isEmpty()) {
            System.out.println("No sprints to remove.");
            return;
        }
        System.out.println("Select sprint to remove:");
        for (int i = 0; i < sprints.size(); i++) System.out.println((i + 1) + ". " + sprints.get(i).getId());
        int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (index < 0 || index >= sprints.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        Database.removeSprint(sprints.get(index));
        Database.saveData();
        System.out.println("Sprint removed.");
    }
}