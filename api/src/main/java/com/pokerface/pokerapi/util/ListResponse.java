package com.pokerface.pokerapi.util;

import java.util.List;

public class ListResponse<T> {
    private List<T> data;
    private int page;
    private int totalResults;
    private int totalPages;

    public ListResponse(List<T> data, int page, int totalPages, int results) {
        this.data = data;
        this.page = page;
        this.totalPages = totalPages;
        this.totalResults = results;
    }

    public ListResponse(List<T> data) {
        this(data, 1,1, data.size());
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
