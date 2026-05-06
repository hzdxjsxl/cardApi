package com.example.cardApi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("sys_user")
@Schema(name = "SysUser", description = "用户表")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "账号")
    @TableField("username")
    private String username;

    @Schema(description = "加密密码")
    @TableField("password")
    private String password;

    @Schema(description = "真实姓名")
    @TableField("real_name")
    private String realName;

    @Schema(description = "归属部门ID")
    @TableField("dept_id")
    private Long deptId;

    @Schema(description = "头像")
    @TableField("avatar")
    private String avatar;

    @Schema(description = "状态 1:正常 0:停用")
    @TableField("status")
    private Boolean status;

    @TableField("create_time")
    private Date createTime;

    @TableField("is_deleted")
    @TableLogic
    private Boolean isDeleted;
}
