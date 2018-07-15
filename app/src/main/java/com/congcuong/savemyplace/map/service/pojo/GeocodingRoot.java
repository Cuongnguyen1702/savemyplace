package com.congcuong.savemyplace.map.service.pojo;

import java.util.List;

public class GeocodingRoot {

    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }

    public GeocodingRoot setResults(List<Result> results) {
        this.results = results;
        return this;
    }
}
