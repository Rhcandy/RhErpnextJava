package rhjava.erpnext.demo.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpSession;
import rhjava.erpnext.demo.config.ERPNextConfig;

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

}
