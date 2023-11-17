package com.diatoz.springBatch.config;

import com.diatoz.springBatch.dao.CustomerDao;
import com.diatoz.springBatch.entity.CustomerEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class Job2Config {
    @Autowired
    private CustomerDao customerDao;
    @Bean(name = "job2")
    public Job job2(JobRepository jobRepository, @Qualifier(value = "step2") Step step)
    {
        return new JobBuilder("job2",jobRepository)
                .start(step)
                .build();
    }



    @Bean(name = "step2")
    public Step step2(JobRepository jobRepository,
                     PlatformTransactionManager transactionManager,
                     @Qualifier("reader2")
                     ItemReader<CustomerEntity> itemReader,
                     @Qualifier("process2")
                     ItemProcessor<CustomerEntity,CustomerEntity> itemProcessor,
                     @Qualifier("writer2")
                     ItemWriter<CustomerEntity> itemWriter)
    {

        return new StepBuilder("step2",jobRepository)
                .<CustomerEntity,CustomerEntity>chunk(25,transactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }


    @Bean(name = "reader2")
    public ItemReader<CustomerEntity> flatFileItemReader2()
    {
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);
        return new RepositoryItemReaderBuilder<CustomerEntity>().name("reader2").sorts(sorts).repository(customerDao).methodName("findAll").build();


    }

    @Bean(name = "process2")
    public ItemProcessor<CustomerEntity,CustomerEntity> itemProcessor2()
    {
        return new CustomItemProcess2();
    }

    @Bean(name = "writer2")
    public ItemWriter<CustomerEntity> itemWriter2(CustomerDao customerDao)
    {

        return new FlatFileItemWriterBuilder<CustomerEntity>()
                .name("writer2")
                .resource(new FileSystemResource("C:\\Users\\prath\\OneDrive\\Desktop\\Project\\springBatch\\src\\main\\resources\\database.csv"))
                .delimited().names("id","firstName","lastName","email","gender","contactNo","country","dob","age")
                .headerCallback(writer -> writer.write("id,firstName,lastName,email,gender,contactNo,country,dob,age"))
                .build();




    }




}


