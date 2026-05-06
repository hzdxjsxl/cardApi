package com.example.cardApi.common;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BasePageDTO {
    @Schema(description = "页码")
    private Integer pageIndex = 1;

    @Schema(description = "每页条数")
    private Integer pageSize = 10;
}
