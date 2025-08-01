package com.example.finalprojectcoursemanagementsystem.model;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageImplementation <T> {

    private List<T> list;
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long totalElements;

    public PageImplementation(Page<T> pages){
        this.list = pages.getContent();
        this.pageNumber = pages.getNumber();
        this.pageSize = pages.getSize();
        this.totalPages = pages.getTotalPages();
        this.totalElements = pages.getTotalElements();
    }

}
