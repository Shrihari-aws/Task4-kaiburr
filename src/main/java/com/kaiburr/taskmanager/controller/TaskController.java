package com.kaiburr.taskmanager.controller;

import com.kaiburr.taskmanager.model.Task;
import com.kaiburr.taskmanager.model.TaskExecution;
import com.kaiburr.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService svc;

    @GetMapping
    public ResponseEntity<?> listOrGet(@RequestParam(required = false) String id) 
    {
        return id == null
                ? ResponseEntity.ok(svc.getAll())
                : ResponseEntity.ok(svc.getById(id));
    }

    
    
    @GetMapping("/search")
    public List<Task> search(@RequestParam String name) 
    { 
        return svc.findByName(name); 
    }

    @PutMapping
    public Task createOrUpdate(@RequestBody Task task)  
    { 
        return svc.save(task); 
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id)         
    { 
        svc.delete(id); 
    }

    @PutMapping("/{id}/execute")
    public TaskExecution run(@PathVariable String id) throws Exception 
    { 
        return svc.execute(id); 
    }
}
