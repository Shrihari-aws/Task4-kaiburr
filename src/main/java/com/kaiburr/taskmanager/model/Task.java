package com.kaiburr.taskmanager.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("tasks")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Task 
{

    @Id
    private String id;
    private String name;
    private String owner;
    private String command;

    /** keep the list non-null to avoid NPEs */
    @Builder.Default
    private List<TaskExecution> taskExecutions = new ArrayList<>();
}
