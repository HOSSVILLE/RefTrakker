package org.hoss.controllers;

import org.hoss.services.RefereeSheetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ReadSheetController {

    private RefereeSheetService refereeSheetService;

    public ReadSheetController(final RefereeSheetService refereeSheetService) {
        this.refereeSheetService = refereeSheetService;
    }

    @GetMapping("/api/sheet/all")
    public String getSheetData() throws Exception {
        refereeSheetService.getSheetData();
        return "Done!";
    }

    @GetMapping("/api/sheet/{week}")
    public String getSheetByWeek(@PathVariable final String week) {
        System.out.println("----> Path Variable "+ week);
        try {
            refereeSheetService.getSheetDataByWeek(Integer.getInteger(week));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Done!";
    }
    
}
