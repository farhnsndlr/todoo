package com.pbo6.todoo;

public class Task {
    private int id;
    private String taskName;
    private boolean isCompleted;

    // Constructor for when task is being created (with ID)
    public Task(int id, String taskName, boolean isCompleted) {
        this.id = id;
        this.taskName = taskName;
        this.isCompleted = isCompleted;
    }

    // Constructor for creating a new task (without an ID)
    public Task(String taskName, boolean isCompleted) {
        this.taskName = taskName;
        this.isCompleted = isCompleted;
    }

    // Getters and setters for all fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    // Override toString() for easier debugging (optional)
    @Override
    public String toString() {
        return "Task{id=" + id + ", taskName='" + taskName + "', isCompleted=" + isCompleted + '}';
    }
}
