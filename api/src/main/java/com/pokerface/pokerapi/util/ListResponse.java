package com.pokerface.pokerapi.util;

import java.util.List;

/**
 * A {@link ListResponse} is a generic type that is used for all REST responses that require sending
 * a list of information to the client.
 *
 * @param <T> the type of data that the {@link ListResponse} will hold.
 */
public class ListResponse<T> {
    private List<T> data;
    private int page;
    private int totalResults;
    private int totalPages;

    /**
     * Create a {@link ListResponse} with the given data.
     *
     * @param data the data to return to the client
     * @param page the page number that the data is representing
     * @param totalPages the total number of pages that can be queried
     * @param results the total number of results that the query produced.
     */
    public ListResponse(List<T> data, int page, int totalPages, int results) {
        this.data = data;
        this.page = page;
        this.totalPages = totalPages;
        this.totalResults = results;
    }

    /**
     * Create a simplified {@link ListResponse} without pagination.
     * <p>
     * the page count will be 1, the totalPages will be 1, and the number of results
     * will be the size of the {@code data} list.
     *
     * @param data the data to be sent to the client.
     */
    public ListResponse(List<T> data) {
        this(data, 1,1, data.size());
    }

    /**
     * Get the data that this {@link ListResponse} is encapsulating
     * @return the data
     */
    public List<T> getData() {
        return data;
    }

    /**
     * set the data that this {@link ListResponse} is encapsulating
     * @param data the data that this {@link ListResponse} will encapsulate
     */
    public void setData(List<T> data) {
        this.data = data;
    }

    /**
     * Get the page number of this {@link ListResponse}
     * @return the page number
     */
    public int getPage() {
        return page;
    }

    /**
     * Set the page number
     * @param page the page number
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * Get the total number of results.
     * @return the number of results
     */
    public int getTotalResults() {
        return totalResults;
    }

    /**
     * Set the total number of results
     * @param totalResults the new number of results
     */
    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    /**
     * Get the total number of pages
     * @return the number of pages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * Set the total number of pages
     * @param totalPages the new number of pages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
