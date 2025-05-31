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
        List<String> fields = Arrays.asList("name", "employee_name", "date_of_joining", "status","designation","department","designation","gender","date_of_birth","company","branch");
        List<Employee> employees=erpNextService.getListWithFields(session, "Employee", fields, Employee.class);
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
        model.addAttribute("activePage", "employee");
        model.addAttribute("pageTitle", "Employee");
        model.addAttribute("salarySlipsList", emSlips);
        model.addAttribute("moredetail", (Employee) erpNextService.getDetail(session, "Employee", name, Employee.class));
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
