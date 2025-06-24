package rhjava.erpnext.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SalaryService {

    public String buildSalarySlipFilters(String name, String employeeName,String department, String designation,
                                      String startDate, String endDate) throws Exception {
        List<List<Object>> filters = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            filters.add(Arrays.asList("name", "like", "%" + name + "%"));
        }
        if (name != null && !name.isEmpty()) {
            filters.add(Arrays.asList("employee_name", "like", "%" + name + "%"));
        }

        if (department != null && !department.isEmpty()) {
            filters.add(Arrays.asList("department", "=", department));
        }

        if (designation != null && !designation.isEmpty()) {
            filters.add(Arrays.asList("designation", "=",  designation ));
        }

        if (startDate != null) {
            filters.add(Arrays.asList("date_of_joining", ">=", startDate.toString()));
        }

        if (endDate != null) {
            filters.add(Arrays.asList("date_of_joining", "<=", endDate.toString()));
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(filters); // ← Important pour le format attendu par Frappe
    }

}
