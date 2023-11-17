package com.diatoz.springBatch.config;

import com.diatoz.springBatch.entity.CustomerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcess2 implements ItemProcessor<CustomerEntity, CustomerEntity> {

    Logger logger = LoggerFactory.getLogger(CustomItemProcess2.class);
    @Override
    public CustomerEntity process(CustomerEntity item) throws Exception {
        logger.info("inside the oprocess");
        return item;
    }
}
