package rhjava.erpnext.demo.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import rhjava.erpnext.demo.model.Base;
import rhjava.erpnext.demo.model.SalarySlip;
import rhjava.erpnext.demo.service.DataService;
import rhjava.erpnext.demo.service.ERPNextService;

import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/data")
public class DataDbController {
    @Autowired
    private DataService DataService;
    @Autowired
    private ERPNextService erpNextService;

    @GetMapping
    public String Accueildata(HttpSession session,Model model) {
        model.addAttribute("activePage", "data");
        model.addAttribute("pageTitle", "Base");
        return "resetdata";
    }

    @PostMapping("/reset")
    public String ReserTdata(
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        String message = DataService.resetAllData(session);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/data";

    }

    @PostMapping("/import")
    public String ImportDataTdata(
            @RequestParam("files1") MultipartFile files1,
            @RequestParam("file2") MultipartFile file2,
            @RequestParam("file3") MultipartFile file3,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        try {
            String fichier1 = new String(files1.getBytes(), StandardCharsets.UTF_8);
            String fichier2 = new String(file2.getBytes(), StandardCharsets.UTF_8);
            String fichier3 = new String(file3.getBytes(), StandardCharsets.UTF_8);

            String message = DataService.sendCsvDataToFrappe(session, fichier1, fichier2, fichier3);
            redirectAttributes.addFlashAttribute("message", message);
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/data";

    }
    
    @GetMapping("/Generate")
    public String postMethodName( 
            @RequestParam(required = true) String Employername,
            @RequestParam(required = true) String monthdebut,
            @RequestParam(required = true) String monthfin,
            @RequestParam() double base,
            @RequestParam(value = "types",required = false) List<String> types,
            RedirectAttributes redirectAttributes,
            Model model,
            HttpSession session) {
        try {
            if (types == null) {
                types = new ArrayList<>(); 
            }
            String message = DataService.generete_Salary_slip( session,  monthdebut,  monthfin, Employername,base,types); 
            redirectAttributes.addFlashAttribute("message", message);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/employee";
    }
    @PostMapping("/ModifSalaryBase")
    public String handleForm(
            @RequestParam("component") String component,
            @RequestParam("condition") String condition,
            @RequestParam("seuil") double seuil,
            @RequestParam("variation") double variation,
            RedirectAttributes redirectAttributes,
            HttpSession session,
            Model model) {
            String message =DataService.ajusterSSA(session, component, condition, seuil, variation) ;
        try {
            redirectAttributes.addFlashAttribute("message", message);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", message);
        }
        return "redirect:/statistique";
    }
    @PostMapping("/FilterSalary")
    public String handleFormF(
            @RequestParam("component") String component,
            @RequestParam("condition") String condition,
            @RequestParam("seuil") double seuil,
            HttpSession session,
            Model model) {
            List<SalarySlip> Slips =DataService.get_salary_By_Component(session, component, condition, seuil) ;
            List<Base> salarycomponents=erpNextService.getList(session, "Salary Component", Base.class);

            model.addAttribute("components", salarycomponents);
            model.addAttribute("data", Slips);
            model.addAttribute("activePage", "filter");
            model.addAttribute("pageTitle", "Salary Slip");
         
        return "SalaryFiltererd";
    }

    @GetMapping("/FilterSalarydebut")
    public String handleFormF(
            HttpSession session,
            Model model) {
            List<Base> salarycomponents=erpNextService.getList(session, "Salary Component", Base.class);
            model.addAttribute("components", salarycomponents);
            model.addAttribute("activePage", "filter");
            model.addAttribute("pageTitle", "Salary Slip");
         
        return "SalaryFiltererdDEBUT";
    }
    
    
}
