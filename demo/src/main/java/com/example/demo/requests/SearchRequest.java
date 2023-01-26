package com.example.demo.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SearchRequest {
    private String machineName;
    private List<String> status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Europe/Belgrade") // example string: 24-01-2023 22:45
    private Date dateFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Europe/Belgrade") // example string: 24-01-2023 22:45
    private Date dateTo;
}
