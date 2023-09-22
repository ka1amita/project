package com.gfa.config;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated // must be present for the field validations to work!
@Component // but not @Configuration!
@ConfigurationProperties(prefix = "pagination")
public class PaginationProperties {
    @Min(1)
    @NotNull
    private Integer pageSizeDefault = 20;
    @Min(1)
    @NotNull
    private Integer pageSizeMax = 100;
    @NotEmpty
    private String sortBy = "id";
    @NotNull
    // Spring recognises the possible values by default and tells the allowed value! :-)
    private Sort.Direction sortOrder = Sort.Direction.DESC;

    public void setPageSizeDefault(Integer pageSizeDefault) {
        this.pageSizeDefault = pageSizeDefault;
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

    public Integer getPageSizeDefault() {
        return pageSizeDefault;
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
