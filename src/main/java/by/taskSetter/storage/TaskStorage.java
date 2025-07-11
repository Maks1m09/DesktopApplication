package by.taskSetter.storage;


import by.taskSetter.entity.Task;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TaskStorage {

    private static final String FILE_NAME = System.getProperty("task.storage.file", "tasks.txt");
    private static final Logger LOGGER = Logger.getLogger(TaskStorage.class.getName());


    public static void save(List<Task> tasks) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            objectOutputStream.writeObject(tasks);
            LOGGER.info("Задачи успешно сохранены в файл");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Ошибка при сохранении данных: " + e.getMessage());
            LOGGER.warning("Ошибка при сохранении данных: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Task> load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            List<Task> tasks = (List<Task>) objectInputStream.readObject();
            LOGGER.info("Задачи успешно загружены из файла");
            return tasks;
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Ошибка при загрузке данных: " + e.getMessage());
            LOGGER.warning("Ошибка при загрузки задач или файл отсутствует" + e.getMessage());
            return new ArrayList<>();
        }
    }
}
