package by.taskSetter.service;

import by.taskSetter.entity.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс TaskManager управляет списком задач: добавление, удаление и обновление задач.
 */

public class TaskManager {
    private List<Task> tasks;

    public TaskManager() {
        tasks = new ArrayList<>();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public void updateTask(int index, Task task) {
        if (index >= 0 && index < tasks.size()) {
            tasks.set(index, task);
        }
    }
}
