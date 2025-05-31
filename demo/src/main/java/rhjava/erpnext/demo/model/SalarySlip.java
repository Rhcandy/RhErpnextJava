package rhjava.erpnext.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalarySlip {
    String name;
    String status;
    String payroll_frequency;
    String net_pay;
}
