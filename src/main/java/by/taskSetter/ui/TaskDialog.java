package by.taskSetter.ui;

import by.taskSetter.entity.Task;
import by.taskSetter.entity.TaskStatus;
import by.taskSetter.exception.TaskValidationException;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

/**
 * Класс TaskDialog представляет собой диалоговое окно для добавления или редактирования задачи.
 * Он позволяет пользователю ввести или изменить информацию о задаче, такую как название, описание,
 * срок выполнения и статус. Диалог обеспечивает валидацию введенных данных и логирование важных событий.
 */

public class TaskDialog extends JDialog {
    private JTextField nameField;
    private JTextArea descriptionField;
    private JTextField dateField;
    private JButton saveButton;
    private JButton cancelButton;
    private JComboBox<TaskStatus> statusComboBox;
    private boolean isSaved = false;
    private Task task;

    private static final Logger LOGGER = Logger.getLogger(TaskDialog.class.getName());

    public TaskDialog(JFrame parent, Task task) {
        super(parent, true);
        setTitle(task == null ? "Добавить задачу" : "Редактировать задачу");
        this.task = task;
        initComponents();
        layoutComponents();
        initListeners();
        if (task != null) {
            populateFields(task);
        }
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        nameField = new JTextField(20);
        descriptionField = new JTextArea(5, 20);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        dateField = new JTextField(10);
        dateField.setToolTipText("Формат даты: ГГГГ-ММ-ДД");
        statusComboBox = new JComboBox<>(TaskStatus.values());
        saveButton = new JButton("Сохранить");
        cancelButton = new JButton("Отмена");

    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);

        // Название

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Название:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(nameField, gbc);

        // Описание

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        mainPanel.add(new JLabel("Описание:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JScrollPane descScrollPane = new JScrollPane(descriptionField);
        descScrollPane.setPreferredSize(new Dimension(200, 80));
        mainPanel.add(descScrollPane, gbc);

        // Срок выполнения

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Срок выполнения (ГГГГ-ММ-ДД):"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(dateField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Статус:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(statusComboBox, gbc);

        // Панель кнопок

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.add(buttonPanel);
        getContentPane().add(southPanel, BorderLayout.SOUTH);
    }

    private void initListeners() {
        saveButton.addActionListener(e -> onSave());
        cancelButton.addActionListener(e -> {
            isSaved = false;
            dispose();
        });
    }

    private void populateFields(Task task) {
        nameField.setText(task.getName());
        descriptionField.setText(task.getDescription());
        if (task.getDate() != null) {
            dateField.setText(task.getDate().toString());
        } else {
            dateField.setText("");
        }
        statusComboBox.setSelectedItem(task.getStatus());
    }

    /**
     * Выполняет валидацию введенных данных и возвращает объект Task.
     *
     * @throws TaskValidationException Если данные некорректны или не прошли проверку.
     */

    public Task getValidatedTask() throws TaskValidationException {
        String name = nameField.getText().trim();
        String description = descriptionField.getText().trim();
        String dateStr = dateField.getText().trim();
        TaskStatus status = (TaskStatus) statusComboBox.getSelectedItem();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите название задачи");
            throw new TaskValidationException("Название не может быть пустым");
        }

        LocalDate date = null;
        if (!dateStr.isEmpty()) {
            try {
                date = LocalDate.parse(dateStr);
                LOGGER.info("Парсинг даты: " + date);
            } catch (DateTimeParseException e) {
                LOGGER.warning("Некорректный формат даты: " + dateStr);
                JOptionPane.showMessageDialog(this, "Некорректный формат даты");
                throw new TaskValidationException("Некорректный формат даты");
            }
        }

        if (task == null) {
            LOGGER.info("Создается новая задача: " + name);
            return new Task(name, description, date, status);
        } else {

            String oldName = task.getName();
            LocalDate oldDate = task.getDate();
            String oldDescription = task.getDescription();
            TaskStatus oldStatus = task.getStatus();

            task.setName(name);
            task.setDescription(description);
            task.setDate(date);
            task.setStatus(status);

            LOGGER.info("Название до изменения: " + oldName + " Описание до изменения: " +
                    oldDescription + " Дата до изменения: " + oldDate + " Статус до изменения: " + oldStatus);
            LOGGER.info("После изменения: Название: " + task.getName() + " Описание: " +
                    task.getDescription() + " Дата: " + task.getDate() + " Статус: " + task.getStatus());

            return task;
        }
    }

    private void onSave() {
        try {
            task = getValidatedTask();
            isSaved = true;
            dispose();
        } catch (TaskValidationException e) {
            LOGGER.warning("Ошибка при сохранении задачи: " + e.getMessage());
        }
    }

    public boolean isSaved() {
        return isSaved;
    }

    public Task getTask() throws TaskValidationException {
        return getValidatedTask();
    }

    public void setNameFieldText(String text) {
        nameField.setText(text);
    }

    public void setDescriptionFieldText(String text) {
        descriptionField.setText(text);
    }

    public void setDateFieldText(String text) {
        dateField.setText(text);
    }

    public void setStatus(TaskStatus status) {
        statusComboBox.setSelectedItem(status);
    }
}

