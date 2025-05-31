package rhjava.erpnext.demo.model;

import lombok.Data;

@Data
public class SalarySlip {
    String name;
    String status;
    String payroll_frequency;
    String net_pay;
}
