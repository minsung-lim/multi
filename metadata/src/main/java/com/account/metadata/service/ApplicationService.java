package com.account.metadata.service;

import com.account.metadata.dto.ApplicationRequest;
import com.account.metadata.dto.ApplicationResponse;
import com.account.metadata.model.Application;
import com.account.metadata.repository.ApplicationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    @Transactional
    public ApplicationResponse createApplication(ApplicationRequest request) {
        Application application = new Application();

        // appId 생성 (중복 체크)
        String appId;
        do {
            appId = RandomStringUtils.randomAlphanumeric(10);
        } while (applicationRepository.existsById(appId));
        application.setAppId(appId);

        application.setAppName(request.getAppName());
        application.setScopes(request.getScopes());

        // create는 무작위 생성
        application.setSecretKey(RandomStringUtils.randomAlphanumeric(20));
        application.setCipherKey(RandomStringUtils.randomAlphanumeric(40));

        application.setRedirectUri(request.getRedirectUri());
        application.setGrantTypes(request.getGrantTypes());

        Application savedApplication = applicationRepository.save(application);
        return convertToResponse(savedApplication);
    }

    @Transactional(readOnly = true)
    public ApplicationResponse getApplication(String appId) {
        Application application = applicationRepository.findById(appId)
                .orElseThrow(() -> new EntityNotFoundException("Application not found with id: " + appId));
        return convertToResponse(application);
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getAllApplications() {
        return applicationRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ApplicationResponse updateApplication(String appId, ApplicationRequest request) {
        Application application = applicationRepository.findById(appId)
                .orElseThrow(() -> new EntityNotFoundException("Application not found with id: " + appId));

        application.setAppName(request.getAppName());
        application.setScopes(request.getScopes());

        // update는 입력값 사용
        application.setSecretKey(request.getSecretKey());
        application.setCipherKey(request.getCipherKey());

        application.setRedirectUri(request.getRedirectUri());
        application.setGrantTypes(request.getGrantTypes());

        Application updatedApplication = applicationRepository.save(application);
        return convertToResponse(updatedApplication);
    }

    @Transactional
    public void deleteApplication(String appId) {
        if (!applicationRepository.existsById(appId)) {
            throw new EntityNotFoundException("Application not found with id: " + appId);
        }
        applicationRepository.deleteById(appId);
    }

    private ApplicationResponse convertToResponse(Application application) {
        ApplicationResponse response = new ApplicationResponse();
        response.setAppId(application.getAppId());
        response.setAppName(application.getAppName());
        response.setScopes(application.getScopes());
        response.setSecretKey(application.getSecretKey());
        response.setCipherKey(application.getCipherKey());
        response.setRedirectUri(application.getRedirectUri());
        response.setGrantTypes(application.getGrantTypes());
        response.setCreatedDate(application.getCreatedDate());
        response.setModifiedDate(application.getModifiedDate());
        return response;
    }
} 