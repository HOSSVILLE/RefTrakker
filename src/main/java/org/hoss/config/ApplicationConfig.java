package org.hoss.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class ApplicationConfig {

    private String name;

    @Value("${org.hoss.spreadsheet.id}")
    private String sheetId;

    @Value("${org.hoss.week1}")
    private String [] week1;

    @Value("${org.hoss.week2}")
    private String [] week2;
    
    @Value("${org.hoss.week3}")
    private String [] week3;

    
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
