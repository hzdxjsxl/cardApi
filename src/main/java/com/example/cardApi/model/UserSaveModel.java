package com.example.cardApi.model;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class UserSaveModel {
    private Long id;
    //用户名既账号
    @NotBlank(message = "用户名不能为空")
    private String username;
    //密码
    @NotBlank(message = "密码绝对不能为空")
    private String password;
    //真实姓名
    @NotBlank(message = "真实姓名绝对不能为空")
    private String realName;
    //头像
    private String avatar;
    // 状态
    private Boolean status;
}
