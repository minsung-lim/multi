package com.account.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MetadataService {

    private final String metadataServiceUrl;
    private final ObjectMapper objectMapper;
    private final CloseableHttpClient httpClient;

    public MetadataService(
            @Value("${metadata.service.url:http://localhost:8083}") String metadataServiceUrl,
            ObjectMapper objectMapper) {
        this.metadataServiceUrl = metadataServiceUrl;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClients.createDefault();
    }

    public List<String> getGrantTypes(String appId) {
        try {
            HttpGet request = new HttpGet(metadataServiceUrl + "/metadata/" + appId);
            return httpClient.execute(request, response -> {
                if (response.getCode() != 200) {
                    throw new RuntimeException("Failed to fetch metadata for appId: " + appId);
                }

                JsonNode root = objectMapper.readTree(response.getEntity().getContent());
                JsonNode grantTypesNode = root.get("grantTypes");
                List<String> grantTypes = new ArrayList<>();
                if (grantTypesNode.isArray()) {
                    for (JsonNode type : grantTypesNode) {
                        grantTypes.add(type.asText());
                    }
                }
                return grantTypes;
            });
        } catch (IOException e) {
            throw new RuntimeException("Error fetching metadata for appId: " + appId, e);
        }
    }
} 