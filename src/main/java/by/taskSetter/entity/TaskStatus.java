package by.taskSetter.entity;

/**
 * Enum TaskStatus представляет возможные статусы выполнения задач: выполнена или не выполнена.
 */

public enum TaskStatus {

    NOT_COMPLETED("Не выполнена"),
    COMPLETED("Выполнена");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
