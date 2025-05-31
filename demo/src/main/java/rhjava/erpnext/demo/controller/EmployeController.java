package rhjava.erpnext.demo.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import rhjava.erpnext.demo.model.Base;

import jakarta.servlet.http.HttpSession;
import rhjava.erpnext.demo.model.Employee;
import rhjava.erpnext.demo.model.SalarySlip;
import rhjava.erpnext.demo.service.ERPNextService;

@Controller
@RequestMapping("/employee")
public class EmployeController {
    @Autowired
    private ERPNextService erpNextService;

    @GetMapping("/filter")
    public String filterEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String designation,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model,
            HttpSession session
            ) {

        List<String> fields = Arrays.asList("name", "employee_name", "date_of_joining", "status","designation","department","designation","gender","date_of_birth","company","branch");
        List<Employee> allEmployees=erpNextService.getListWithFields(session, "Employee", fields, Employee.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // adapte au format réel

        List<Employee> filtered = allEmployees.stream()
            .filter(e -> name == null || e.getName() != null && e.getName().toLowerCase().contains(name.toLowerCase()))
            .filter(e -> employeeName == null || e.getEmployee_name() != null && e.getEmployee_name().toLowerCase().contains(employeeName.toLowerCase()))
            .filter(e -> department == null || e.getDepartment() != null && e.getDepartment().toLowerCase().contains(department.toLowerCase()))
            .filter(e -> designation == null || e.getDesignation() != null && e.getDesignation().toLowerCase().contains(designation.toLowerCase()))
            .filter(e -> {
                if (startDate == null) return true;
                try {
                    LocalDate doj = LocalDate.parse(e.getDate_of_joining(), formatter);
                    return !doj.isBefore(startDate);
                } catch (Exception ex) {
                    return false;
                }
            })
            .filter(e -> {
                if (endDate == null) return true;
                try {
                    LocalDate doj = LocalDate.parse(e.getDate_of_joining(), formatter);
                    return !doj.isAfter(endDate);
                } catch (Exception ex) {
                    return false;
                }
            })
            .collect(Collectors.toList());
        List<Base> departements= erpNextService.getList(session, "Department", Base.class) ;
        List<Base> designations= erpNextService.getList(session, "Designation", Base.class);
        model.addAttribute("departement", departements);
        model.addAttribute("designation", designations);
        model.addAttribute("activePage", "employee");
        model.addAttribute("pageTitle", "Employee");
        model.addAttribute("list", filtered);
        return "emp/list"; 
    }


    @GetMapping
    public String ListEmployer(HttpSession session,Model model) {
        List<String> fields = Arrays.asList("name", "employee_name", "date_of_joining", "status","designation","department","designation","gender","date_of_birth","company","branch");
        List<Employee> employees=erpNextService.getListWithFields(session, "Employee", fields, Employee.class);
        List<Base> departements= erpNextService.getList(session, "Department", Base.class) ;
        List<Base> designations= erpNextService.getList(session, "Designation", Base.class);
        model.addAttribute("departement", departements);
        model.addAttribute("designation", designations);
        model.addAttribute("activePage", "employee");
        model.addAttribute("pageTitle", "Employee");
        model.addAttribute("list", employees);
        return "emp/list";
    }

    @GetMapping("/more")
    public String MoreDetailEmployee(@RequestParam("name") String name, Model model,HttpSession session) {
        String filters = "[[\"employee\", \"=\", \"" + name + "\"]]";
        List<String> fields = Arrays.asList("name", "status", "payroll_frequency", "net_pay", "start_date", "end_date");
        List<SalarySlip> emSlips=erpNextService.getListByFilterWithFields(session, "Salary Slip", filters, fields, SalarySlip.class);
        List<Base> departements= erpNextService.getList(session, "Department", Base.class);
        List<Base> designations= erpNextService.getList(session, "Designation", Base.class);

        model.addAttribute("pageTitle", "Employee");
        model.addAttribute("activePage", "employee");
        model.addAttribute("salarySlipsList", emSlips);
        model.addAttribute("moredetail", (Employee) erpNextService.getDetail(session, "Employee", name, Employee.class));
        model.addAttribute("departement", departements);
        model.addAttribute("designation", designations);
        return "emp/more"; 
    }

    @GetMapping("/more/salarySlip")
    public String MoreDetailSalarySlip(@RequestParam("name") String name, Model model,HttpSession session) {
        model.addAttribute("activePage", "employee");
        model.addAttribute("pageTitle", "Employee Salary Slip");
        model.addAttribute("salarySlip", (SalarySlip)erpNextService.getDetail(session,"Salary Slip", name, SalarySlip.class));
        return "emp/salarySlip-fiche";
    }

}
