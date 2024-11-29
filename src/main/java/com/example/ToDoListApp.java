
package com.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;

import java.sql.*;
import java.util.Scanner;

public class ToDoListApp {

    private static final String DB_URL = "jdbc:mysql://database-1.c5yccaueggp2.eu-north-1.rds.amazonaws.com:3306/todolistdb";
    private static final String USER = "admin";
    private static final String PASSWORD = "Abdulaziz123";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- To-Do List ---");
            System.out.println("1. View Tasks");
            System.out.println("2. Add Task");
            System.out.println("3. Mark Task as Complete");
            System.out.println("4. Remove Task");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: viewTasks(); break;
                case 2: addTask(scanner); break;
                case 3: markTaskComplete(scanner); break;
                case 4: removeTask(scanner); break;
                case 5: 
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void viewTasks() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM tasks";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int taskId = rs.getInt("task_id");
                String description = rs.getString("description");
                boolean isComplete = rs.getBoolean("is_complete");
                System.out.printf("%d. [%s] %s%n", taskId, isComplete ? "X" : " ", description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addTask(Scanner scanner) {
        System.out.print("Enter task description: ");
        String description = scanner.nextLine();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "INSERT INTO tasks (description) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, description);
            stmt.executeUpdate();
            System.out.println("Task added.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void markTaskComplete(Scanner scanner) {
        viewTasks();
        System.out.print("Enter task ID to mark as complete: ");
        int taskId = scanner.nextInt();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "UPDATE tasks SET is_complete = TRUE WHERE task_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, taskId);
            stmt.executeUpdate();
            System.out.println("Task marked as complete.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void removeTask(Scanner scanner) {
        viewTasks();
        System.out.print("Enter task ID to remove: ");
        int taskId = scanner.nextInt();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "DELETE FROM tasks WHERE task_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, taskId);
            stmt.executeUpdate();
            System.out.println("Task removed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

