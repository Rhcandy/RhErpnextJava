package rhjava.erpnext.demo.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import rhjava.erpnext.demo.config.ERPNextConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;


import java.net.URI;
import java.net.URLEncoder;
import lombok.Data;

@Service
public class ERPNextService {
    private final ERPNextConfig erpNextConfig;
    private final RestTemplate restTemplate;

    public ERPNextService(ERPNextConfig erpNextConfig, RestTemplateBuilder restTemplateBuilder) {
        this.erpNextConfig = erpNextConfig;
        this.restTemplate = restTemplateBuilder.build();
    }

    public <T> List<T> getList(HttpSession session, String resource, Class<T> type) {
        try {
            String encodedResource = URLEncoder.encode(resource, StandardCharsets.UTF_8.name());
            URI url = UriComponentsBuilder.fromHttpUrl(erpNextConfig.getApiUrl())
                .pathSegment("resource", encodedResource)
                .queryParam("limit_page_length", 200)  // limite à 200 résultats
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

            HttpEntity<String> request = new HttpEntity<>(erpNextConfig.createHeaders(session));

            ResponseEntity<ERPListResponse<LinkedHashMap>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<ERPListResponse<LinkedHashMap>>() {}
            );

            ObjectMapper mapper = new ObjectMapper();
            List<T> result = new ArrayList<>();
            for (LinkedHashMap map : response.getBody().getData()) {
                result.add(mapper.convertValue(map, type));
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des données", e);
        }
    }

    public <T> List<T> getListByFilter(HttpSession session, String resource, String filtersJson, Class<T> type) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(erpNextConfig.getApiUrl())
                .pathSegment("resource", resource)
                .queryParam("filters", filtersJson)
                .queryParam("limit_page_length", 200)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

            System.out.println("Final URL: " + url);

            HttpEntity<String> request = new HttpEntity<>(erpNextConfig.createHeaders(session));
            ResponseEntity<ERPListResponse<LinkedHashMap>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<ERPListResponse<LinkedHashMap>>() {}
            );

            // Mapper les LinkedHashMap en objets du type souhaité
            ObjectMapper mapper = new ObjectMapper();
            List<LinkedHashMap> rawList = response.getBody().getData();
            List<T> result = new ArrayList<>();
            for (LinkedHashMap map : rawList) {
                T object = mapper.convertValue(map, type);
                result.add(object);
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des données", e);
        }
    }


    public <T> List<T> getListByFilterWithFields(HttpSession session, String resource, String filtersJson, List<String> fields ,Class<T> type) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String fieldsJson= mapper.writeValueAsString(fields);

           URI url = UriComponentsBuilder.fromHttpUrl(erpNextConfig.getApiUrl())
                .pathSegment("resource", resource)
                .queryParam("filters", filtersJson)
                .queryParam("fields", fieldsJson)
                .queryParam("limit_page_length", 200)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

            System.out.println("Final URL: " + url);
    
            HttpEntity<String> request = new HttpEntity<>(erpNextConfig.createHeaders(session));
            ResponseEntity<ERPListResponse<LinkedHashMap>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<ERPListResponse<LinkedHashMap>>() {}
            );
            List<LinkedHashMap> rawData = response.getBody().getData();
            // On convertit chaque élément LinkedHashMap en T
            List<T> typedData = rawData.stream()
                .map(item -> mapper.convertValue(item, type))
                .collect(Collectors.toList());

            return typedData;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des données", e);
        }
    }
    
   public <T> List<T> getListWithFields(HttpSession session, String resource, List<String> fields, Class<T> type) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String fieldsJson = mapper.writeValueAsString(fields);

            URI url = UriComponentsBuilder.fromHttpUrl(erpNextConfig.getApiUrl())
                .pathSegment("resource", resource)
                .queryParam("fields", fieldsJson)
                .queryParam("limit_page_length", 200)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

            System.out.println("Final URL: " + url);
            HttpEntity<String> request = new HttpEntity<>(erpNextConfig.createHeaders(session));
            // On récupère d'abord une réponse avec List<LinkedHashMap>
            ResponseEntity<ERPListResponse<LinkedHashMap>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<ERPListResponse<LinkedHashMap>>() {}
            );
            List<LinkedHashMap> rawData = response.getBody().getData();
            // On convertit chaque élément LinkedHashMap en T
            List<T> typedData = rawData.stream()
                .map(item -> mapper.convertValue(item, type))
                .collect(Collectors.toList());

            return typedData;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des données", e);
        }
    }


    public <T> T getDetail(HttpSession session, String resource, String name, Class<T> type) {
        String url = erpNextConfig.getApiUrl() + "/resource/" + resource + "/" + name;
        HttpEntity<String> request = new HttpEntity<>(erpNextConfig.createHeaders(session));
        
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, request, responseType);

        ObjectMapper mapper = new ObjectMapper();
        Object dataObj = response.getBody().get("data");
        return mapper.convertValue(dataObj, type); 
    }

    // Classes pour le mapping JSON
    @Data
    public static class ERPListResponse<T> {
        private List<T> data;
    }

    public <T> List<T> getListByFilterOrder(
        HttpSession session,
        String resource,
        String filtersJson,
        String orderBy,         // nom de la colonne à trier
        String orderType,       // "asc" ou "desc"
        Class<T> type
    ) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(erpNextConfig.getApiUrl())
                .pathSegment("resource", resource)
                .queryParam("filters", filtersJson)
                .queryParam("limit_page_length", 200);
                

            // Ajouter tri si les paramètres sont valides
            if (orderBy != null && !orderBy.isEmpty() &&
                orderType != null && (orderType.equalsIgnoreCase("asc") || orderType.equalsIgnoreCase("desc"))) {
                builder.queryParam("order_by", orderBy + " " + orderType.toLowerCase()); // 
            }

            URI url = builder
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

            System.out.println("Final URL: " + url);

            HttpEntity<String> request = new HttpEntity<>(erpNextConfig.createHeaders(session));

            ResponseEntity<ERPListResponse<T>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<ERPListResponse<T>>() {}
            );
            return response.getBody().getData();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des données", e);
        }
    }

}
