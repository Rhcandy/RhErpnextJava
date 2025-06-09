package rhjava.erpnext.demo.controller;

import jakarta.servlet.http.HttpSession;
import rhjava.erpnext.demo.model.TotalAnnee;
import rhjava.erpnext.demo.service.DataService;
import rhjava.erpnext.demo.service.ERPNextService;

import java.time.Year;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
public class UserController {
    @Autowired
    private ERPNextService erpNextService;
    @Autowired
    private DataService dataservice;
   

    // Injecter l'URL de l'API ERPNext depuis application.properties
    @Value("${erpnext.api.url}")
    private String erpUrl;
   
    @GetMapping("/")
    public String loginForm(Model model) {
        model.addAttribute("pageTitle", "Login");
        return "login"; // Affiche login.html
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password, Model model,HttpSession session) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String body = "usr=" + username + "&pwd=" + password;
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<String> response = rest.exchange(
                    erpUrl+"/method/login",
                    HttpMethod.POST, request, String.class
            );
            String erpSession =  response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
            session.setAttribute("erpSession", erpSession);
            return "redirect:/home";
        } catch (Exception e) {
            model.addAttribute("error", "Échec de connexion");
            model.addAttribute("pageTitle", "Login");
            return "login";
        }
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        List<TotalAnnee>totalbymonth= dataservice.get_salary_statistics_month_by_year( session, String.valueOf(Year.now().getValue()));
        model.addAttribute("data", totalbymonth);
        model.addAttribute("pageTitle", "Home");
        model.addAttribute("activePage", "home");
        return "home";
    }

    @GetMapping("/filter")
    public String monthlyrecap(@RequestParam(required = false) String year,HttpSession session, Model model) {
        List<TotalAnnee>totalbymonth= dataservice.get_salary_statistics_month_by_year( session, year);
        model.addAttribute("data", totalbymonth);
        model.addAttribute("pageTitle", "Home");
        model.addAttribute("activePage", "home");
        return "home";
    }


    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Supprime toutes les données de session
        return "redirect:/"; // Redirige vers la page de connexion
    }
}
