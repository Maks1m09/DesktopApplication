package by.taskSetter.entity;

public enum TaskStatus {

    NOT_COMPLETED("Невыполнена"),
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
