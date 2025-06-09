package rhjava.erpnext.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import rhjava.erpnext.demo.model.Base;
import rhjava.erpnext.demo.model.Employee;
import rhjava.erpnext.demo.model.SalarySlip;
import rhjava.erpnext.demo.service.DataService;
import rhjava.erpnext.demo.service.ERPNextService;

@Controller
@RequestMapping("/statistique")
public class StatistiqueController {
    @Autowired
    private ERPNextService erpNextService;
    @Autowired
    private DataService dataservice;

    @GetMapping
    public String home(HttpSession session, Model model) {
        String filters=DataService.getCurrentMonthDateRange();

        List<SalarySlip> salaryslips = new ArrayList<>();
        List<Employee> employees=erpNextService.getList(session, "Employee", Employee.class);
        List<Base> Bases=erpNextService.getListByFilter(session, "Salary Slip", filters, Base.class);
        double total=0;
        for (Base salaryslip : Bases) {
            SalarySlip one=erpNextService.getDetail(session,"Salary Slip" , salaryslip.getName(), SalarySlip.class);
            salaryslips.add(one);
            total+=one.getNet_pay();
        }
        model.addAttribute("Total", total);
        model.addAttribute("CountSalary", salaryslips.size());
        model.addAttribute("CountEmployee", employees.size());
        model.addAttribute("data", salaryslips);
        model.addAttribute("pageTitle", "Recape Annuel");
        model.addAttribute("activePage", "recap");
        return "dashboard";
    }
    
    @GetMapping("/Tabrecap")
    public String filterEmployees(
            @RequestParam(required = false) String month,
            Model model,
            HttpSession session) {

        String filters=DataService.getCurrentMonthDateRange();

        if (month != null && !month.isEmpty()) {
            String[] dates = DataService.getStartAndEndDateFromMonthInput(month);
            filters="[[\"start_date\", \">=\", \""+dates[0]+"\"], [\"start_date\", \"<=\", \""+ dates[1]+"\"]]";
        }
        List<SalarySlip> salaryslips = new ArrayList<>();
        List<Employee> employees=erpNextService.getList(session, "Employee", Employee.class);
        List<Base> Bases=erpNextService.getListByFilter(session, "Salary Slip", filters, Base.class);
        double total=0;
        for (Base salaryslip : Bases) {
            SalarySlip one=erpNextService.getDetail(session,"Salary Slip" , salaryslip.getName(), SalarySlip.class);
            salaryslips.add(one);
            total+=one.getNet_pay();
        }
        model.addAttribute("Total", total);
        model.addAttribute("CountSalary", salaryslips.size());
        model.addAttribute("CountEmployee", employees.size());
        model.addAttribute("data", salaryslips);
        model.addAttribute("pageTitle", "Recape Annuel");
        model.addAttribute("activePage", "recap");
        return "dashboard";
    }

}
