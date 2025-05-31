package rhjava.erpnext.demo.model;


import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee {
    String name;
    String employee_name;
    String date_of_joining;
    String status;
    String department;
    String designation;
    String gender;
    String date_of_birth;
    String company;
    String branch;
}
