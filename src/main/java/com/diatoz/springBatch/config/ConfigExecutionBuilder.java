package com.diatoz.springBatch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class ConfigExecutionBuilder implements JobExecutionListener {

    private final Logger logger = LoggerFactory.getLogger(ConfigExecutionBuilder.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.info("job started  the parameter {}",jobExecution.getJobParameters());
        JobExecutionListener.super.beforeJob(jobExecution);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        logger.info("job is completed  the parameter {}",jobExecution.getJobParameters());
        JobExecutionListener.super.afterJob(jobExecution);
    }
}
