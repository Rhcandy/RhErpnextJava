package rhjava.erpnext.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalarySlip {
    String name;
    String status;
    String payroll_frequency;
    double net_pay;

    String start_date;
    String end_date;
    String salary_structure;
    String mode_of_payment;
    int total_working_days;
    double gross_pay;
    double total_deduction;
    String employee_name;
    String currency;

    // misy annee
    public String getMois() {
        try {
            LocalDate date = LocalDate.parse(start_date, DateTimeFormatter.ISO_DATE);
            String mois = date.getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH);
            int annee = date.getYear();
            return mois.substring(0, 1).toUpperCase() + mois.substring(1) + " " + annee;
        } catch (Exception e) {
            return "";
        }
    }

    // mois ihany
    public String getMoisOnly() {
        try {
            LocalDate date = LocalDate.parse(start_date, DateTimeFormatter.ISO_DATE);
            String mois = date.getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH);
            return mois.substring(0, 1).toUpperCase() + mois.substring(1);
        } catch (Exception e) {
            return "";
        }
    }
}
