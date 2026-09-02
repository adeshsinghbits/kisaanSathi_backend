package com.kissansathi.dto.common;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Simplified, Android-friendly pagination envelope, wraps a Spring Data {@link Page}.
 */
@Getter
public class PagedResponse<T> {
    private final List<T> content;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final boolean last;

    public PagedResponse(Page<T> page) {
        this.content = page.getContent();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.last = page.isLast();
    }

    public PagedResponse(List<T> content, Page<?> sourcePage) {
        this.content = content;
        this.page = sourcePage.getNumber();
        this.size = sourcePage.getSize();
        this.totalElements = sourcePage.getTotalElements();
        this.totalPages = sourcePage.getTotalPages();
        this.last = sourcePage.isLast();
    }
}