package com.example.demo.service;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DateParserService {

    private final Parser nattyParser;

    public DateParserService() {
        this.nattyParser = new Parser();
    }

    public Date parseDate(String dateString) {
        List<DateGroup> dateGroups = nattyParser.parse(dateString);
        if (!dateGroups.isEmpty()) {
            List<Date> dates = dateGroups.get(0).getDates();
            if (!dates.isEmpty()) {
                return dates.get(0);
            }
        }
        return null;
    }

}
