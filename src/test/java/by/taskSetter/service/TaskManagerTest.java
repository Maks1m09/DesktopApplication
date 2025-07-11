package by.taskSetter.service;

import by.taskSetter.entity.Task;
import by.taskSetter.entity.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new TaskManager();
    }

    @Test
    public void testInitialTasksIsEmpty() {
        List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks);
        assertTrue(tasks.isEmpty(), "Первоначальный список задач должен быть пустым.");
    }

    @Test
    public void testAddTask() {
        Task task = new Task("Задача", "Описание", LocalDate.of(2023, 10, 1), TaskStatus.NOT_COMPLETED);
        taskManager.addTask(task);
        List<Task> tasks = taskManager.getTasks();
        assertEquals(1, tasks.size());
        assertEquals("Задача", tasks.get(0).getName());
    }

    @Test
    public void testRemoveTask() {
        Task task1 = new Task("Задача 1", "Описание 1", LocalDate.of(2023, 10, 2), TaskStatus.NOT_COMPLETED);
        Task task2 = new Task("Задача 2", "Описание 2", LocalDate.of(2023, 10, 3), TaskStatus.COMPLETED);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.removeTask(task1);
        List<Task> tasks = taskManager.getTasks();
        assertEquals(1, tasks.size());
        assertEquals("Задача 2", tasks.get(0).getName());
    }

    @Test
    public void testUpdateTask() {
        Task originalTask = new Task("Задача1", "Описание 1", LocalDate.of(2023, 10, 4), TaskStatus.NOT_COMPLETED);
        taskManager.addTask(originalTask);
        Task updatedTask = new Task("Задача обновленная", "Описание обновленное", LocalDate.of(2023, 11, 1), TaskStatus.COMPLETED);
        taskManager.updateTask(0, updatedTask);
        List<Task> tasks = taskManager.getTasks();
        assertEquals(1, tasks.size());
        assertEquals("Задача обновленная", tasks.get(0).getName());
        assertEquals(TaskStatus.COMPLETED, tasks.get(0).getStatus());
    }

    @Test
    public void testUpdateTaskInvalidIndex() {
        taskManager.addTask(new Task("Задача1", "Описание", LocalDate.now(), TaskStatus.NOT_COMPLETED));
        assertDoesNotThrow(() -> taskManager.updateTask(-1, new Task("Задача", "Описание", LocalDate.now(), TaskStatus.NOT_COMPLETED)));
        assertDoesNotThrow(() -> taskManager.updateTask(10, new Task("Задача", "Описание", LocalDate.now(), TaskStatus.NOT_COMPLETED)));
        List<Task> tasks = taskManager.getTasks();
        assertEquals(1, tasks.size());
    }
}

