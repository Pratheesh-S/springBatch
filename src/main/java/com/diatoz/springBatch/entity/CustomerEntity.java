package com.diatoz.springBatch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table( name= "customers")
public class CustomerEntity {

    @Id
    private String id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")

    private String lastName;
    private String email;
    private String gender;
    @Column(name = "contact_no")
    private String contactNo;
    private String country;
    private String dob;
    private String age;


}
