package com.example.taskmanager.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task task) {
        if (taskRepository.existsById(id)) {
            return taskRepository.save(task);
        }
        return null;
    }

    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Task getTaskByTitle(String title) {
        return taskRepository.findByTitle(title);
    }

    public Task updateTaskPartially(Long id, Map<String, Object> updates) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            if (updates.containsKey("title")) {
                task.setTitle((String) updates.get("title"));
            }
            if (updates.containsKey("description")) {
                task.setDescription((String) updates.get("description"));
            }
            if (updates.containsKey("date")) {
                String dateStr = (String) updates.get("date");
                task.setDate(java.time.LocalDateTime.parse(dateStr));
            }
            return taskRepository.save(task);
        }
        return null;
    }
} 