package by.taskSetter.exception;

/**
 * Исключение TaskValidationException используется для обработки ошибок валидации задач
 */

public class TaskValidationException extends Exception {
    public TaskValidationException(String message) {
        super(message);
    }
}

