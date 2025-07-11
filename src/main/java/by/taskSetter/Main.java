package by.taskSetter;

import by.taskSetter.ui.TaskApp;
import by.taskSetter.utils.LoggerConfigurator;

import javax.swing.*;
import java.util.logging.Logger;

/**
 * Основной класс приложения, содержащий точку входа.
 * В методе main происходит настройка логгирования с помощью LoggerConfigurator,
 * а также запуск графического интерфейса пользователя (GUI) в потоках Swing.
 */

public class Main {
    public static void main(String[] args) {

        LoggerConfigurator.setup();

        SwingUtilities.invokeLater(() -> {
            Logger.getLogger(Main.class.getName()).info("Запуск приложения");
            new TaskApp().setVisible(true);
        });
    }
}
