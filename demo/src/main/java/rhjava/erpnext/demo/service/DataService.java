package rhjava.erpnext.demo.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import rhjava.erpnext.demo.config.ERPNextConfig;
import rhjava.erpnext.demo.model.TotalAnnee;
import rhjava.erpnext.demo.service.ERPNextService.ERPListResponse;

@Service
public class DataService {

    private final ERPNextConfig erpNextConfig;
    private final RestTemplate restTemplate;

    public DataService(ERPNextConfig erpNextConfig, RestTemplateBuilder restTemplateBuilder) {
        this.erpNextConfig = erpNextConfig;
        this.restTemplate = restTemplateBuilder.build();
    }

    public String resetAllData(HttpSession session) {
        String url = erpNextConfig.getApiUrl() + "/method/my_app.csv.fonction.reset_all_data";

        HttpHeaders headers = erpNextConfig.createHeaders(session);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Aucun paramètre à envoyer
        HttpEntity<String> request = new HttpEntity<>("", headers);
        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                Object message = response.getBody().get("message");
                return "✅ Réponse du serveur : " + (message != null ? message.toString() : "Aucune réponse");
            } else {
                return "❌ Erreur : Code HTTP " + response.getStatusCode();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "❌ Exception lors de l’appel : " + e.getMessage();
        }
    }
    public String sendCsvDataToFrappe(HttpSession session, String fichier1, String fichier2, String fichier3) {
        String url = erpNextConfig.getApiUrl() + "/method/my_app.csv.fonction.import_data_from_files";

        HttpHeaders headers = erpNextConfig.createHeaders(session);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fichier1", fichier1);
        requestBody.put("fichier2", fichier2);
        requestBody.put("fichier3", fichier3);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody().get("message").toString();
            } else {
                return "❌ Erreur Frappe : code HTTP " + response.getStatusCode();
            }
        } catch (Exception e) {
            return "❌ Exception lors de l’appel Frappe : " + e.getMessage();
        }
    }

    public static String getCurrentMonthDateRange() {
        // Obtenir l'année et le mois courant
        YearMonth currentMonth = YearMonth.now();
        // Début du mois : 1er
        LocalDate startDate = currentMonth.atDay(1);
        // Fin du mois : dernier jour du mois
        LocalDate endDate = currentMonth.atEndOfMonth();
        // Format AAAA-MM-JJ
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Retourner sous forme de chaîne utilisable dans l'API
        return String.format(
            "[[\"start_date\", \">=\", \"%s\"], [\"start_date\", \"<=\", \"%s\"]]",
            startDate.format(formatter),
            endDate.format(formatter)
        );
    }
    public static String[] getStartAndEndDateFromMonthInput(String monthInput) {
        try {
            // Parse input au format yyyy-MM
            YearMonth yearMonth = YearMonth.parse(monthInput); // ex: 2025-06

            // Début du mois
            LocalDate startDate = yearMonth.atDay(1);

            // Fin du mois
            LocalDate endDate = yearMonth.atEndOfMonth();

            // Retourner les deux dates au format yyyy-MM-dd
            return new String[]{
                startDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                endDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
            };
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Mois invalide : " + monthInput);
        }
    }

    public List<TotalAnnee> get_salary_statistics_month_by_year(HttpSession session, String year) {
        List<TotalAnnee> result = new ArrayList<>();
        // Met le paramètre year dans l'URL en GET
        String url = erpNextConfig.getApiUrl() + "/method/my_app.csv.fonction.get_salary_statistics_month_by_year?year=" + year;

        HttpHeaders headers = erpNextConfig.createHeaders(session);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
           ResponseEntity<LinkedHashMap> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<LinkedHashMap>() {}
            );

            ObjectMapper mapper = new ObjectMapper();
            Object rawList = response.getBody().get("message");
            List<LinkedHashMap> maps = (List<LinkedHashMap>) rawList;
            for (LinkedHashMap map : maps) {
                result.add(mapper.convertValue(map, TotalAnnee.class));
            }
            System.out.println(result.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


}
