package com.gfa.configurations;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

@Configuration
@ConfigurationProperties(prefix = "pagination")
public class PaginationConfig {
    @Min(1)
    private Integer pageSize = 20;
    @Min(1)
    private Integer pageSizeMax = 100;
    @NotEmpty
    private String sortBy = "id";
    @NotNull
    private Sort.Direction sortOrder = Sort.Direction.ASC;

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageSizeMax(Integer pageSizeMax) {
        this.pageSizeMax = pageSizeMax;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public void setSortOrder(Sort.Direction sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getPageSizeMax() {
        return pageSizeMax;
    }

    public String getSortBy() {
        return sortBy;
    }

    public Sort.Direction getSortOrder() {
        return sortOrder;
    }
}
