package com.example.cardApi.model;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserUpModel {
    @NotNull(message = "修改数据必须提供记录 ID！")
    private long id;
    //真实姓名
    @NotBlank(message = "真实姓名不能为空")
    private String realName;
    //真实姓名
    private String phone;
    //居住地址
    private String address;
    //头像
    private String avatar;
}
