package com.pokerface.pokerapi.users;

import java.util.List;

public class ListResponse<T> {
    private List<T> data;
    private int page;
    private int total_results;
    private int total_pages;

    public ListResponse(List<T> data, int page, int total_pages, int results) {
        this.data = data;
        this.page = page;
        this. total_pages = total_pages;
        this.total_results = results;
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

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }
}
