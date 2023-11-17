package com.diatoz.springBatch.controller;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    @Autowired
    @Qualifier("job1")
    private Job job1;

    @Autowired
    @Qualifier("job2")
    private Job job2;

    @Autowired
    private JobLauncher jobLauncher;

    @GetMapping("/load")
    public ResponseEntity<String> uploadTheData() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameter = new JobParametersBuilder().addLong("startAt",System.currentTimeMillis())
                .addString("file","customers.csv").toJobParameters();


        jobLauncher.run(job1,jobParameter);
        return ResponseEntity.ok("Data up loaded successfully");
    }
    @GetMapping("/upload")
    public ResponseEntity<String> loadTheData() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameter = new JobParametersBuilder().addLong("startAt",System.currentTimeMillis())
                .addString("file","database.csv").toJobParameters();


        jobLauncher.run(job2,jobParameter);
        return ResponseEntity.ok("Data loaded successfully");
    }
}
