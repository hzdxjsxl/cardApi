package com.example.cardApi.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cardApi.common.ApiResponse;
import com.example.cardApi.common.PageResponse;
import com.example.cardApi.entity.User;
import com.example.cardApi.query.LoginQuery;
import com.example.cardApi.query.UserQuery;
import com.example.cardApi.model.UserSaveModel;
import com.example.cardApi.model.UserUpModel;
import com.example.cardApi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Tag(name = "用户模块")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Operation(summary = "用户登录")
    @SaIgnore
    @PostMapping("/login")
    public ApiResponse<Map<String, String>> login(@RequestBody LoginQuery loginForm) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginForm.getUsername())
                .eq(User::getPassword, userService.getSalt(loginForm.getPassword()))
                .eq(User::getStatus, 1);
        User user = userService.getOne(wrapper);
        if (user != null) {
            StpUtil.login(user.getId());
            user.setPassword(null);
            StpUtil.getSession().set("currentUser", user);
            Map<String, String> data = new HashMap<>();
            data.put("token", StpUtil.getTokenValue());
            return ApiResponse.success(data);
        } else {
            return ApiResponse.error(500, "账号或密码错误");
        }
    }
    @Operation(summary = "用户注册")
    @SaIgnore
    @PostMapping("/register")
    public ApiResponse<Map<String, String>> register(@RequestBody LoginQuery loginForm) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginForm.getUsername())
                .eq(User::getPassword, userService.getSalt(loginForm.getPassword()))
                .eq(User::getStatus, 1);
        User user = userService.getOne(wrapper);
        if (user != null) {
            StpUtil.login(user.getId());
            user.setPassword(null);
            StpUtil.getSession().set("currentUser", user);
            Map<String, String> data = new HashMap<>();
            data.put("token", StpUtil.getTokenValue());
            return ApiResponse.success(data);
        } else {
            return ApiResponse.error(500, "账号或密码错误");
        }
    }
    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/info")
    public ApiResponse<Map<String, Object>> getInfo() {
        StpUtil.checkLogin();
        long userId = StpUtil.getLoginIdAsLong();
        User user = userService.getById(userId);
        Map<String, Object> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("realName", user.getRealName());
        data.put("avatar", user.getAvatar());
        data.put("roles", Arrays.asList("admin")); // 抛给前端的极简权限
        return ApiResponse.success(data);
    }
    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public ApiResponse<String> logout() {
        StpUtil.logout();
        return ApiResponse.success("退出成功");
    }
    @Operation(summary = "保存用户")
    @PostMapping("/save")
    public ApiResponse<String> saveUserApi(@RequestBody UserSaveModel userModel) {
        if (!userService.saveUser(userModel)) {
            return ApiResponse.error(400,"保存失败");
        }
        return ApiResponse.success("保存成功");
    }
    @Operation(summary = "编辑用户")
    @PostMapping("/update")
    public ApiResponse<String> updateUserApi(@RequestBody @Validated UserUpModel userModel) {
        if (!userService.updateUser(userModel)) {
            return ApiResponse.error(400,"保存失败");
        }
        return ApiResponse.success("保存成功");
    }
    @Operation(summary = "删除用户")
    @PostMapping("/del")
    public ApiResponse<String> delUserApi(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return ApiResponse.error(400,"要删除的数据不能为空");
        }
        return userService.removeByIds(ids) ? ApiResponse.success("删除成功") : ApiResponse.error(400,"删除失败");
    }
    @Operation(summary = "查询用户列表")
    @PostMapping("/list")
    public ApiResponse<?> getListApi(@RequestBody UserQuery queryModel) {

        Page<User> page = new Page<>(queryModel.getPageIndex(), queryModel.getPageSize());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(queryModel.getUsername())) {
            wrapper.like(User::getUsername, queryModel.getUsername());
        }
        wrapper.orderByDesc(User::getCreateTime);
        wrapper.select(User::getId,User::getUsername,User::getAvatar,User::getRealName,User::getStatus);
        Page<User> result = userService.page(page, wrapper);
        return ApiResponse.success(PageResponse.of(result));
    }
}
