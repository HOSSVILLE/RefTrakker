package org.hoss.services;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.hoss.RefTrakkerMain;
import org.hoss.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class RefereeSheetService {
    private final Logger logger = LoggerFactory.getLogger(RefereeSheetService.class);

    private ApplicationConfig appConfig;
    private GoogleSheetsService googleSheetsService;

    public RefereeSheetService(final ApplicationConfig appConfig, final GoogleSheetsService googleSheetsService) {
        this.appConfig = appConfig;
        this.googleSheetsService = googleSheetsService;
    }

    public void getSheetData() throws Exception {
        logger.info("******** Name {}", appConfig.getName());
        logger.info("******** SheetId {} ", appConfig.getSheetId());
        Sheets service = googleSheetsService.getSheetsService();

        for (String data : appConfig.getWeek1()) {
            System.out.println("---" + data + "---");
            printRange(data, appConfig.getSheetId(), service);
        }
        for (String data : appConfig.getWeek2()) {
            System.out.println("---" + data + "---");
            printRange(data, appConfig.getSheetId(), service);
        }
        for (String data : appConfig.getWeek3()) {
            System.out.println("---" + data + "---");
            printRange(data, appConfig.getSheetId(), service);
        }
    }
        public void getSheetDataByWeek(final int week) throws Exception {
            Sheets service = googleSheetsService.getSheetsService();

            if (week == 1) {
                for (String data : appConfig.getWeek1()) {
                    System.out.println("---" + data + "---");
                    printRange(data, appConfig.getSheetId(), service);
                }
            }
            if (week == 2) {
                for (String data : appConfig.getWeek2()) {
                    System.out.println("---" + data + "---");
                    printRange(data, appConfig.getSheetId(), service);
                }
            }
            if (week == 3) {
                for (String data : appConfig.getWeek3()) {
                    System.out.println("---" + data + "---");
                    printRange(data, appConfig.getSheetId(), service);
                }
            }
    }

    private void printRange(String range, String spreadSheetId, Sheets service) throws IOException, ParseException {
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
                } else if (row.size() == 3 && ((String) row.get(2)).contains("Coed")) {
                    System.out.printf("%s\tNo Ref\t%s\n", row.get(0), row.get(2));
                } else if (row.size() == 3 && ((String) row.get(2)).contains(" v ")) {
                    System.out.printf("%s\tNo Ref\t%s\n", row.get(0), row.get(2));
                }

            }
        }
    }
}
