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
            @RequestParam("items_file") MultipartFile itemsFile,
            @RequestParam("suppliers_file") MultipartFile suppliersFile,
            @RequestParam("quotations_file") MultipartFile quotationsFile,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        try {
            String itemsContent = new String(itemsFile.getBytes(), StandardCharsets.UTF_8);
            String suppliersContent = new String(suppliersFile.getBytes(), StandardCharsets.UTF_8);
            String quotationsContent = new String(quotationsFile.getBytes(), StandardCharsets.UTF_8);

            String message = DataService.sendCsvDataToFrappe(session, itemsContent, suppliersContent, quotationsContent);
            redirectAttributes.addFlashAttribute("message", message);
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/data";

    }
    
}
