package rhjava.erpnext.demo.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import rhjava.erpnext.demo.service.DataService;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/data")
public class DataDbController {
    @Autowired
    private DataService DataService;

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
            RedirectAttributes redirectAttributes,
            Model model,
            HttpSession session) {
        try {
            String message = DataService.generete_Salary_slip( session,  monthdebut,  monthfin, Employername,base); 
            redirectAttributes.addFlashAttribute("message", message);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/employee";
    }
    
    
}
