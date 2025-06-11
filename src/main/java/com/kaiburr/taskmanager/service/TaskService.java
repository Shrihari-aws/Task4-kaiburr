package com.kaiburr.taskmanager.service;

import com.kaiburr.taskmanager.model.Task;
import com.kaiburr.taskmanager.model.TaskExecution;
import com.kaiburr.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService 
{

    private final TaskRepository repo;

    public List<Task> getAll()                         
    { 
        return repo.findAll(); 
    }
    public Task       getById(String id)               
    { return repo.findById(id)
                                                               .orElseThrow(() -> new NotFound(id)); 
    }
    public List<Task> findByName(String pattern)       
    { 
        List<Task> list = repo.findByNameContainingIgnoreCase(pattern);
        if (list.isEmpty()) throw new NotFound("name like \"" + pattern + '"');
        return list;
    }
    public Task save(Task task)
    { 
        validateCommand(task.getCommand()); 
        return repo.save(task); 
    }
    public void delete(String id)
    { 
        if (!repo.existsById(id)) throw new NotFound(id); 
        repo.deleteById(id); 
    }

 
   public TaskExecution execute(String id) throws Exception 
   {
        Task task = getById(id);
        validateCommand(task.getCommand());
        Date start = new Date();
        String os = System.getProperty("os.name").toLowerCase();
        ProcessBuilder pb;

        if (os.contains("win")) 
        {
            pb = new ProcessBuilder("cmd", "/c", task.getCommand());
        } 
        else 
        {
            pb = new ProcessBuilder("sh", "-c", task.getCommand());
        }

        pb.redirectErrorStream(true);
        Process proc = pb.start();

        String output;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()))) 
        {
            output = br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
        proc.waitFor();
        Date end = new Date();

        TaskExecution exec = TaskExecution.builder()
                                      .startTime(start)
                                      .endTime(end)
                                      .output(output)
                                      .build();

        task.getTaskExecutions().add(exec);
        repo.save(task);
        return exec;
    }

    private static final Pattern SAFE = Pattern.compile("^(echo|ls|cat)\\b.*", Pattern.CASE_INSENSITIVE);

    private void validateCommand(String cmd) 
    {
        if (!SAFE.matcher(cmd).matches())
            throw new IllegalArgumentException("Unsafe command rejected");
    }

    public static class NotFound extends RuntimeException 
    {
        public NotFound(String what) { super("Task not found: " + what); }
    }
}
