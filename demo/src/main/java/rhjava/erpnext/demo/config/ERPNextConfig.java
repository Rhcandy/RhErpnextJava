package rhjava.erpnext.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;

@Component
public class ERPNextConfig {
    @Value("${erpnext.api.url}")
    private String apiUrl;

    public String getApiUrl() {
        return apiUrl;
    }

    public HttpHeaders createHeaders(HttpSession session) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    
        String fullCookie = (String) session.getAttribute("erpSession");
        if (fullCookie != null && fullCookie.contains("sid=")) {
            String sid = extractSid(fullCookie);
            headers.set("Cookie", sid);
        }
        return headers;
    }
    
    // Méthode utilitaire pour extraire uniquement le "sid=..."
    private String extractSid(String cookieString) {
        for (String part : cookieString.split(";")) {
            part = part.trim();
            if (part.startsWith("sid=")) {
                return part; 
            }
        }
        return "";
    }
    
}
