package rhjava.erpnext.demo.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import rhjava.erpnext.demo.model.Employee;
import rhjava.erpnext.demo.model.SalarySlip;
import rhjava.erpnext.demo.service.ERPNextService;

@Controller
@RequestMapping("/employee")
public class EmployeController {
    @Autowired
    private ERPNextService erpNextService;

    @GetMapping
    public String ListEmployer(HttpSession session,Model model) {
        List<String> fields = Arrays.asList("name", "employee_name", "date_of_joining", "status","designation");
        List<Employee> employees=erpNextService.getListWithFields(session, "Employee", fields, Employee.class);
        model.addAttribute("activePage", "employee");
        model.addAttribute("pageTitle", "Employee");
        model.addAttribute("list", employees);
        return "emp/list";
    }

    @GetMapping("/more")
    public String MoreDetailEmployee(HttpSession session,@RequestParam("name") String name, Model model) {
        String filters = "[[\"employee\", \"=\", \"" + name + "\"]]";
        List<String> fields = Arrays.asList("name", "status", "payroll_frequency", "net_pay");
        List<SalarySlip> emSlips=erpNextService.getListByFilterWithFields(null, "Salary Slip", filters, fields, SalarySlip.class);
        model.addAttribute("activePage", "employee");
        model.addAttribute("pageTitle", "Employee");
        model.addAttribute("salarySlipsList", emSlips);
        model.addAttribute("moredetail", (Employee) erpNextService.getDetail(session, "Employee", name, Employee.class));
        return "emp/more"; 
    }

    @GetMapping("/more/salarySlip")
    public String MoreDetailSalarySlip(HttpSession session,@RequestParam("name") String name, Model model) {
        model.addAttribute("activePage", "employee");
        model.addAttribute("pageTitle", "Employee Salary Slip");
        model.addAttribute("moredetail", (SalarySlip)erpNextService.getDetail(session,"Salary Slip", name, SalarySlip.class));
        return "emp/more"; /* Page izay tinao anarany */
    }

}
