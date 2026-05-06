package com.example.cardApi.common.utils;

import cn.dev33.satoken.stp.StpUtil;
import com.example.cardApi.entity.User;

public class LoginUserUtils {
    /**
     * 极速获取当前登录用户的全套信息
     */
    public static User getCurrentUser() {
        if (!StpUtil.isLogin()) {
            return null;
        }
        return (User) StpUtil.getSession().get("currentUser");
    }
    public static Long getUserId() {
        return StpUtil.getLoginIdAsLong();
    }
}
