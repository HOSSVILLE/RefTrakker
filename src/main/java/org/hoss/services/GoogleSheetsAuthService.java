package org.hoss.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import org.hoss.config.ApplicationConfig;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class GoogleSheetsAuthService {

    private ApplicationConfig applicationConfig;

    public GoogleSheetsAuthService(final ApplicationConfig config) {
        this.applicationConfig = config;
    }

    public Credential authorize() throws IOException {
        InputStream in = new ByteArrayInputStream(applicationConfig.getGoogleCredential().getBytes());

        GoogleCredential credential = GoogleCredential.fromStream(in).createScoped(applicationConfig.getScopes());
        return credential;
    }
}
