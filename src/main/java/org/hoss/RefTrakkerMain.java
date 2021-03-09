package org.hoss;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.services.sheets.v4.model.*;
import com.google.api.services.sheets.v4.Sheets;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.hoss.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
@EnableAutoConfiguration
public class RefTrakkerMain implements CommandLineRunner {

    private Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                RefTrakkerMain.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(appConfig.getJsonFactory(), new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        appConfig.getHttpTransport(), appConfig.getJsonFactory(), clientSecrets, appConfig.getScopes())
                        .setDataStoreFactory(appConfig.getDataStoreFactory())
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");

        return credential;
    }

     private Sheets getSheetsService() throws IOException {
        Credential credential = authorize();
        return new Sheets.Builder(appConfig.getHttpTransport(), appConfig.getJsonFactory(), credential)
                .setApplicationName(appConfig.getName())
                .build();
    }
    @Autowired
    private ApplicationConfig appConfig;


    public void run(String ... args) throws Exception {
        System.out.println("******** "+appConfig.getName());
        System.out.println("******** "+appConfig.getSheetId());
        Sheets service = getSheetsService();
            
        for (String data: appConfig.getWeek1()) {
            System.out.println("---"+data+"---");
            printRange(data,appConfig.getSheetId(),service);
        }
        for (String data: appConfig.getWeek2()) {
            System.out.println("---"+data+"---");
            printRange(data,appConfig.getSheetId(),service);
        }
        for (String data: appConfig.getWeek3()) {
            System.out.println("---"+data+"---");
            printRange(data,appConfig.getSheetId(),service);
        }
    }
    public static void main(String[] args) throws IOException, ParseException {

        SpringApplication app = new SpringApplication(RefTrakkerMain.class);
        app.run();
    }

    private static void printRange(String range, String spreadSheetId, Sheets service) throws IOException, ParseException {
        ValueRange response = service.spreadsheets().values()
                .get(spreadSheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();

         SimpleDateFormat sFormatter = new SimpleDateFormat("MMM dd, yyyy");
        if (values == null || values.size() == 0) {
            System.out.println("No data found.");
        } else {
            Date gameDate;
            System.out.println("Processing...");
            for (List row : values) {
                if (row.size() == 1 && ((String) row.get(0)).endsWith("DAY")) {
                    System.out.println(row.get(0));
                } else if (row.size() == 1 && ((String) row.get(0)).matches("[a-zA-z]*\\s\\d{1,2},\\s\\d{4}")) {
                    gameDate = sFormatter.parse(row.get(0).toString());
                    System.out.println("Date--> " + sFormatter.format(gameDate));
                } else if (row.size() == 5 && !((String) row.get(0)).startsWith("TIMES")) {
                    System.out.printf("%s\t%15s\t%30s\t%s\t%s\n", row.get(0), row.get(1), row.get(3), row.get(4), row.get(2));
                } else if (row.size() == 3 &&
                        (((String) row.get(2)).startsWith("Expected") || ((String) row.get(2)).endsWith("Coed Playoffs") || ((String) row.get(2)).contains("Mens Playoffs"))) {
                    System.out.printf("%s\tNo Ref\t%s\n", row.get(0), row.get(2));
                } else if (row.size() == 3 && ((String)row.get(2)).contains("Coed")) {
                    System.out.printf("%s\tNo Ref\t%s\n", row.get(0), row.get(2));
                }else if (row.size() == 3 &&  ((String)row.get(2)).contains(" v ")) {
                    System.out.printf("%s\tNo Ref\t%s\n",row.get(0),row.get(2));
                }

            }
        }
    }

}
