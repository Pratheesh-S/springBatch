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



    @Bean(name = "job1")
    public Job job1(JobRepository jobRepository,@Qualifier("listener1") JobExecutionListener jobExecutionListener, @Qualifier(value = "step1") Step step)
    {
        return new JobBuilder("job1",jobRepository)
                .listener(jobExecutionListener)
                .start(step)
                .build();
    }

    @Bean(name = "listener1")
    public JobExecutionListener jobExecutionListener()
    {
        return new ConfigExecutionBuilder();
    }

    @Bean(name = "step1")
    public Step step1(JobRepository jobRepository,
                     PlatformTransactionManager transactionManager,
                     TaskExecutor taskExecutor,
                     @Qualifier("reader1")
                     ItemReader<CustomerEntity> itemReader,
                     @Qualifier("process1")
                         ItemProcessor<CustomerEntity,CustomerEntity> itemProcessor,
                     @Qualifier("writer1")
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
    public TaskExecutor taskExecutor1()
    {
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor= new SimpleAsyncTaskExecutor();
        simpleAsyncTaskExecutor.setConcurrencyLimit(10);
        return simpleAsyncTaskExecutor;
    }
    @Bean(name = "reader1")
    public ItemReader<CustomerEntity> flatFileItemReader1()
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

  @Bean(name = "process1")
  public ItemProcessor<CustomerEntity,CustomerEntity> itemProcessor1()
  {
      return new CustomItemProcess();
  }

  @Bean(name = "writer1")
  public ItemWriter<CustomerEntity> itemWriter1(CustomerDao customerDao)
  {
      return new RepositoryItemWriterBuilder<CustomerEntity>()
              .repository(customerDao)
              .methodName("save").build();

  }




}
