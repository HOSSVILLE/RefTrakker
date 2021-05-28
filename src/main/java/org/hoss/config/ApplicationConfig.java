package org.hoss.config;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class ApplicationConfig {

    private java.io.File dataStoreDir = new java.io.File("tokens");
    private FileDataStoreFactory dataStoreFactory;
    private List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);
    private JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    private  HttpTransport httpTransport;

    private String name;

    public ApplicationConfig() {
        try {
        dataStoreFactory = new FileDataStoreFactory(dataStoreDir);
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (IOException | GeneralSecurityException ex) {
            ex.printStackTrace();
        }
    }

    @Value("${org.hoss.spreadsheet.id}")
    private String sheetId;

    @Value("${org.hoss.week1}")
    private String [] week1;

    @Value("${org.hoss.week2}")
    private String [] week2;
    
    @Value("${org.hoss.week3}")
    private String [] week3;

    public HttpTransport getHttpTransport() {
        return httpTransport;
    }
    public JsonFactory getJsonFactory() {
        return jsonFactory;
    }
    public List<String> getScopes() {
        return scopes;
    }
    public File getDataStoreDir() {
        return dataStoreDir;
    }
    public FileDataStoreFactory getDataStoreFactory() {
        return dataStoreFactory;
    }
    public String [] getWeek3() {
        return this.week3;
    }
    public String [] getWeek2() {
        return this.week2;
    }

    public String [] getWeek1() {
        return this.week1;
    }
    public void setWeek1(final String[] val) {
        this.week1 = val;
    }
    public void setWeek2(final String[] val) {
        this.week2 = val;
    }
    public void setWeek3(final String[] val) {
        this.week3 = val;
    }
    
    public String getName() {
        return this.name;
    }
    public String getSheetId() {
        return this.sheetId;
    }

    public void setName(final String val) {
        this.name = val;
    }
    public void setSheetId(final String val) {
        this.sheetId = val;
    }
    
}
