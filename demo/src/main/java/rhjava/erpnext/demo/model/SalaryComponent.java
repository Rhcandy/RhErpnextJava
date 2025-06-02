package rhjava.erpnext.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalaryComponent {
    String salary_component;
    String abbr;
    double default_amount;
}
