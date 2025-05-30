package com.example.taskmanager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/test")
    public String test() {
        return "Task Manager API is working!";
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Task id must be a positive number");
        }
        Task task = taskService.getTaskById(id);
        if (task != null) {
            return ResponseEntity.ok(task);
        }
        throw new TaskNotFoundException(id);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Task> getTaskByTitle(@PathVariable String title) {
        Task task = taskService.getTaskByTitle(title);
        if (task != null) {
            return ResponseEntity.ok(task);
        }
        throw new TaskNotFoundException(-1L);
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Task id must be a positive number");
        }
        Task updatedTask = taskService.updateTask(id, task);
        if (updatedTask != null) {
            return ResponseEntity.ok(updatedTask);
        }
        throw new TaskNotFoundException(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Task id must be a positive number");
        }
        if (taskService.deleteTask(id)) {
            return ResponseEntity.ok().build();
        }
        throw new TaskNotFoundException(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Task> updateTaskPartially(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Task id must be a positive number");
        }
        Task updatedTask = taskService.updateTaskPartially(id, updates);
        if (updatedTask != null) {
            return ResponseEntity.ok(updatedTask);
        }
        throw new TaskNotFoundException(id);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class TaskNotFoundException extends RuntimeException {
        public TaskNotFoundException(Long id) {
            super("Task not found with id: " + id);
        }
    }
} 