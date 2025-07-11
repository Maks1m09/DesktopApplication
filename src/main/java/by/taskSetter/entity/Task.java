package by.taskSetter.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Task implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    private LocalDate date;
    private TaskStatus status;


    public Task(String name, String description, LocalDate Date, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.date = Date;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(date, task.date) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, date, status);
    }

    @Override
    public String toString() {
        return name + " (" + (status == TaskStatus.COMPLETED ? "Выполнена" : "Невыполнена") + ")";
    }
}
