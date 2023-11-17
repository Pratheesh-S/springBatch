package com.diatoz.springBatch.config;

import com.diatoz.springBatch.dao.CustomerDao;
import com.diatoz.springBatch.entity.CustomerEntity;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Objects;

@Configuration
public class JobConfig
{



    @Bean
    public Job job(JobRepository jobRepository,JobExecutionListener jobExecutionListener, Step step)
    {
        return new JobBuilder("job1",jobRepository)
                .listener(jobExecutionListener)
                .start(step)
                .build();
    }

    @Bean
    public JobExecutionListener jobExecutionListener()
    {
        return new ConfigExecutionBuilder();
    }

    @Bean
    public Step step(JobRepository jobRepository,
                     PlatformTransactionManager transactionManager,
                     TaskExecutor taskExecutor,
                     ItemReader<CustomerEntity> itemReader,
                     ItemProcessor<CustomerEntity,CustomerEntity> itemProcessor,
                     ItemWriter<CustomerEntity> itemWriter)
    {

        return new StepBuilder("step1",jobRepository)
                .<CustomerEntity,CustomerEntity>chunk(25,transactionManager)
                .taskExecutor(taskExecutor)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor()
    {
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor= new SimpleAsyncTaskExecutor();
        simpleAsyncTaskExecutor.setConcurrencyLimit(10);
        return simpleAsyncTaskExecutor;
    }
    @Bean
    public ItemReader<CustomerEntity> flatFileItemReader()
  {
      return new FlatFileItemReaderBuilder<CustomerEntity>()
              .name("reader1")
              .resource(new ClassPathResource("customers.csv"))
              .linesToSkip(1)
              .delimited()
              .names("id","firstName","lastName","email","gender","contactNo","country","dob")
              .targetType(CustomerEntity.class)
              .build();

  }

  @Bean
  public ItemProcessor<CustomerEntity,CustomerEntity> itemProcessor()
  {
      return new CustomItemProcess();
  }

  @Bean
  public ItemWriter<CustomerEntity> itemWriter(CustomerDao customerDao)
  {
      return new RepositoryItemWriterBuilder<CustomerEntity>()
              .repository(customerDao)
              .methodName("save").build();

  }




}
