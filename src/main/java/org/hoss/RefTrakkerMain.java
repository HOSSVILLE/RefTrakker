package org.hoss;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
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

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            ".credentials");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            GsonFactory.getDefaultInstance();
    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/sheets.googleapis.com-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    private static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                RefTrakkerMain.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

     private Sheets getSheetsService() throws IOException {
        Credential credential = authorize();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
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
  /**


        range = "Schedule!A55:E81";
        printRange(range,spreadsheetId,service);
        range = "Schedule!H55:L81";
        printRange(range,spreadsheetId,service);
        range = "Schedule!O55:S81";
        printRange(range,spreadsheetId,service);
        range = "Schedule!V55:Z81";
        printRange(range,spreadsheetId,service);
        range = "Schedule!AC55:AG81";
        printRange(range,spreadsheetId,service);
        range = "Schedule!AJ55:AN81";
        printRange(range,spreadsheetId,service);
        range = "Schedule!AQ55:AU81";
        printRange(range,spreadsheetId,service);*/
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
