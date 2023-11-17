package com.diatoz.springBatch.config;

import com.diatoz.springBatch.entity.CustomerEntity;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomItemProcess implements ItemProcessor<CustomerEntity, CustomerEntity> {
    @Override
    public CustomerEntity process(CustomerEntity item) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dob = LocalDate.parse(item.getDob(), formatter);

        String age = String.valueOf(calculateAge(dob));
        item.setAge(age);

        return item;
    }
    public static int calculateAge(LocalDate dob) {
        LocalDate currentDate = LocalDate.now();

        int age = currentDate.getYear() - dob.getYear();

        if (currentDate.getDayOfYear() < dob.getDayOfYear()) {
            age--;
        }
        return age;
    }
}
