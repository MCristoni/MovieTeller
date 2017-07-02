package com.example.mathe.movieteller.classes;

import java.util.List;

public class RetornoTrailers {
    private int id;
    private List<ResultsTrailer> results;
    private  int status_code;
    private String status_message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ResultsTrailer> getResults() {
        return results;
    }

    public void setResults(List<ResultsTrailer> results) {
        this.results = results;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }
}
