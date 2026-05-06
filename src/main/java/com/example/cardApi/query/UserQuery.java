package com.example.cardApi.query;

import com.example.cardApi.common.BasePageDTO;
import lombok.Data;

@Data
public class UserQuery extends BasePageDTO {

    private String username;
    private String id;
}
