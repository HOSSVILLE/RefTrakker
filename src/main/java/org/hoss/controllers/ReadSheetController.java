package org.hoss.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ReadSheetController {

    @GetMapping("/api/sheet/all")
    public String getSheetData() {
        return "hello";
    }
    
}
