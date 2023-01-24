package com.example.demo.requests;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SearchRequest {
    private String machineName;
    private List<String> status;
    private Date dateFrom;
    private Date dateTo;
}
