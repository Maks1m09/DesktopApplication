package by.taskSetter.storage;

import by.taskSetter.entity.Task;
import by.taskSetter.entity.TaskStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TaskStorageTest {

    private static final String TEST_FILE = "tasks_test.txt";

    @Before
    public void setUp() throws IOException {
        System.setProperty("task.storage.file", TEST_FILE);
        File file = new File(TEST_FILE);
        if (file.exists() && !file.delete()) {
            throw new IOException("Не удалось удалить " + TEST_FILE + " в setUp");
        }
    }

    @After
    public void cleanUpTestFile() throws IOException {
        File file = new File(TEST_FILE);
        if (file.exists() && !file.delete()) {
            throw new IOException("Не удалось удалить " + TEST_FILE + " в cleanUpTestFile");
        }
    }

    @Test
    public void testSaveAndLoadTasks() {
        List<Task> tasksToSave = Arrays.asList(
                new Task("Задача 1", "Описание 1", LocalDate.of(2023, 10, 1), TaskStatus.NOT_COMPLETED),
                new Task("Задача 2", "Описание 2", LocalDate.of(2023, 10, 2), TaskStatus.COMPLETED)
        );
        TaskStorage.save(tasksToSave);
        List<Task> loadedTasks = TaskStorage.load();
        assertNotNull(loadedTasks);
        assertEquals(2, loadedTasks.size());
        assertEquals("Задача 1", loadedTasks.get(0).getName());
        assertEquals("Описание 1", loadedTasks.get(0).getDescription());
        assertEquals(LocalDate.of(2023, 10, 1), loadedTasks.get(0).getDate());
        assertEquals(TaskStatus.NOT_COMPLETED, loadedTasks.get(0).getStatus());
        assertEquals("Задача 2", loadedTasks.get(1).getName());
        assertEquals("Описание 2", loadedTasks.get(1).getDescription());
        assertEquals(LocalDate.of(2023, 10, 2), loadedTasks.get(1).getDate());
        assertEquals(TaskStatus.COMPLETED, loadedTasks.get(1).getStatus());
    }

    @Test
    public void testLoadWhenFileDoesNotExist() {
        File file = new File(TEST_FILE);
        if (file.exists() && !file.delete()) {
            throw new RuntimeException("Не удалось удалить " + TEST_FILE);
        }
        assertFalse(file.exists());
        List<Task> loadedTasks = TaskStorage.load();
        assertNotNull(loadedTasks);
        assertTrue(loadedTasks.isEmpty());
    }
}





