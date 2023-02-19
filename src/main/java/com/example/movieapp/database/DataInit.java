package com.example.movieapp.database;

import com.example.movieapp.service.ImportJsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class DataInit {

    private final ImportJsonData importJsonData;

    @Autowired
    public DataInit(ImportJsonData importJsonData) {
        this.importJsonData = importJsonData;
    }

    @PostConstruct
    public void postConstruct() {
        log.info("Loading the database");

        importJsonData.convertData();
    }
}
