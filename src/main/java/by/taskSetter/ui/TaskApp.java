package by.taskSetter.ui;

import by.taskSetter.entity.Task;
import by.taskSetter.entity.TaskStatus;
import by.taskSetter.exception.TaskValidationException;
import by.taskSetter.service.TaskManager;
import by.taskSetter.storage.TaskStorage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;


public class TaskApp extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(TaskApp.class.getName());

    private JTable taskTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JComboBox<String> filterComboBox;
    private final TaskManager taskManager;
//    private final List<Task> allTasks;

    public TaskApp() {
        LOGGER.info("Инициализация приложения");
        setTitle("Управление задачами");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        taskManager = new TaskManager();
        taskManager.setTasks(TaskStorage.load());
//        allTasks = taskManager.getTasks();

        initComponents();
        layoutComponents();
        initListeners();

        refreshTaskList();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                LOGGER.info("Закрытие приложения. Сохранение задач.");
                int confirm = JOptionPane.showConfirmDialog(
                        TaskApp.this,
                        "Вы действительно хотите выйти?",
                        "Подтверждение выхода",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    LOGGER.info("Пользователь подтвердил выход. Сохраняем задачи");
                    TaskStorage.save(taskManager.getTasks());
                    dispose();
                }
                LOGGER.info("Выход отменен пользователем");
            }
        });
    }

    private void initComponents() {
        LOGGER.info("Инициализация компонентов интерфейса");
        String[] columnNames = {"Задача", "Статус"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        taskTable = new JTable(tableModel);
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addButton = new JButton("Добавить");
        editButton = new JButton("Редактировать");
        deleteButton = new JButton("Удалить");
        filterComboBox = new JComboBox<>(new String[]{"Все", "Выполненные", "Невыполненные"});
    }

    private void layoutComponents() {
        LOGGER.info("Размещение компонентов интерфейса");
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("Фильтр:"));
        filterPanel.add(filterComboBox);
        topPanel.add(filterPanel, BorderLayout.WEST);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(taskTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initListeners() {
        addButton.addActionListener(e -> {
            LOGGER.info("Кнопка 'Добавить' нажата");
            openTaskDialog(null);
            refreshTaskList();
            applyFilter();
            LOGGER.info("Обновлен список задач после добавления");
        });

        editButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow >= 0) {
                Task selectedTask = taskManager.getTasks().get(selectedRow);
                LOGGER.info("Редактирование задачи: " + selectedTask.getName());
                openTaskDialog(selectedTask);
                LOGGER.info("Диалог редактирования завершен");
                refreshTaskList();
                applyFilter();
            } else {
                LOGGER.warning("Попытка редактировать задачу без выбора");
                JOptionPane.showMessageDialog(this, "Выберите задачу для редактирования");
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow >= 0) {
                Task selectedTask = taskManager.getTasks().get(selectedRow);
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Удалить выбранную задачу?",
                        "Подтверждение",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    LOGGER.info("Удаление задачи: " + selectedTask.getName());
                    taskManager.removeTask(selectedTask);
                    refreshTaskList();
                    LOGGER.info("Задача удалена" + selectedTask.getName() + " и список обновлен");
                } else {
                    LOGGER.info("Удаление отменено пользователем");
                }
            } else {
                LOGGER.warning("Попытка удалить задачу без выбора");
                JOptionPane.showMessageDialog(this, "Выберите задачу для удаления");
            }
        });
        filterComboBox.addActionListener(e -> {
            String selectedFilter = (String) filterComboBox.getSelectedItem();
            LOGGER.info("Выбран фильтр: " + selectedFilter);
            applyFilter();
        });
        taskTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = taskTable.getSelectedRow();
                    if (row >= 0) {
                        Task selectedTask = taskManager.getTasks().get(row);
                        openTaskDialog(selectedTask);
                    }
                }
            }
        });
    }

    private void refreshTaskList() {
        LOGGER.fine("Обновление отображения списка задач");
        tableModel.setRowCount(0);
        for (Task task : taskManager.getTasks()) {
            String statusStr = task.getStatus().toString();
            tableModel.addRow(new Object[]{task.getName(), statusStr});
        }
    }

    private void applyFilter() {
        String selectedFilter = (String) filterComboBox.getSelectedItem();
        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : taskManager.getTasks()) {
            switch (Objects.requireNonNull(selectedFilter)) {
                case "Все":
                    filteredTasks.add(task);
                    break;
                case "Выполненные":
                    if (task.getStatus() == TaskStatus.COMPLETED) {
                        filteredTasks.add(task);
                    }
                    break;
                case "Невыполненные":
                    if (task.getStatus() == TaskStatus.NOT_COMPLETED) {
                        filteredTasks.add(task);
                    }
                    break;
            }
        }
        tableModel.setRowCount(0);
        for (Task task : filteredTasks) {
            String statusStr = task.getStatus().toString();
            tableModel.addRow(new Object[]{task.getName(), statusStr});
        }
    }

    private void openTaskDialog(Task task) {
        TaskDialog dialog = new TaskDialog(this, task);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            try {
                Task resultTask = dialog.getTask();
                if (task == null) {
                    taskManager.addTask(resultTask);
                    LOGGER.info("Добавлена задача: " + resultTask.getName());
                } else {
                    int index = taskManager.getTasks().indexOf(task);
                    if (index >= 0) {
                        taskManager.updateTask(index, resultTask);
                    }
                }
                refreshTaskList();
                applyFilter();
            } catch (TaskValidationException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Ошибка: ", JOptionPane.ERROR_MESSAGE);
                LOGGER.warning("Ошибка: " + e.getMessage());
            }
        }
    }
}


