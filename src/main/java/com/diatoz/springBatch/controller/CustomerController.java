package com.diatoz.springBatch.controller;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    @Autowired
    private Job job;

    @Autowired
    private JobLauncher jobLauncher;

    @GetMapping("/load")
    public ResponseEntity<String> loadTheData() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameter = new JobParametersBuilder().addLong("startAt",System.currentTimeMillis())
                .addString("file","customers.csv").toJobParameters();


        jobLauncher.run(job,jobParameter);
        return ResponseEntity.ok("Data loaded successfully");
    }
}
