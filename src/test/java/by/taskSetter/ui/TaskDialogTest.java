package by.taskSetter.ui;

import by.taskSetter.entity.Task;
import by.taskSetter.entity.TaskStatus;
import by.taskSetter.exception.TaskValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TaskDialogTest {
    private TaskDialog taskDialog;

    @BeforeEach
    public void setUp() {
        JFrame parent = null;
        taskDialog = new TaskDialog(parent, null);
    }

    @Test
    public void testGetValidatedTaskValidData() throws Exception {
        taskDialog.setNameFieldText("Тестовая задача");
        taskDialog.setDescriptionFieldText("Описание");
        taskDialog.setDateFieldText("2023-12-31");
        taskDialog.setStatus(TaskStatus.COMPLETED);
        Task task = taskDialog.getValidatedTask();
        assertNotNull(task);
        assertEquals("Тестовая задача", task.getName());
        assertEquals("Описание", task.getDescription());
        assertEquals(LocalDate.of(2023, 12, 31), task.getDate());
        assertEquals(TaskStatus.COMPLETED, task.getStatus());
    }

    @Test
    public void testGetValidatedTaskEmptyNameShouldThrow() {
        taskDialog.setNameFieldText("");
        taskDialog.setDescriptionFieldText("Описание");
        taskDialog.setDateFieldText("2023-12-31");
        taskDialog.setStatus(TaskStatus.NOT_COMPLETED);
        Exception exception = assertThrows(TaskValidationException.class, () -> {
            taskDialog.getValidatedTask();
        });
        assertEquals("Название не может быть пустым", exception.getMessage());
    }

    @Test
    public void testGetValidatedTaskInvalidDateFormatShouldThrow() {
        taskDialog.setNameFieldText("Тестовая задача");
        taskDialog.setDescriptionFieldText("Описание");
        taskDialog.setDateFieldText("2023/12/31");
        taskDialog.setStatus(TaskStatus.NOT_COMPLETED);
        Exception exception = assertThrows(TaskValidationException.class, () -> {
            taskDialog.getValidatedTask();
        });
        assertEquals("Некорректный формат даты", exception.getMessage());
    }

    @Test
    public void testGetValidatedTaskEmptyDateShouldWork() throws Exception {
        taskDialog.setNameFieldText("Тестовая задача");
        taskDialog.setDescriptionFieldText("Описание");
        taskDialog.setDateFieldText("");
        taskDialog.setStatus(TaskStatus.COMPLETED);
        Task result = taskDialog.getValidatedTask();
        assertNotNull(result);
        assertNull(result.getDate());
    }
}
