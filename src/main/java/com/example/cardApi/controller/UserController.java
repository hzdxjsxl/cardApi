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
        return userService.login(loginForm.getUsername(),loginForm.getPassword());
    }
    @Operation(summary = "用户注册")
    @SaIgnore
    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody LoginQuery registerForm) {
        if (!StringUtils.hasText(registerForm.getUsername()) || !StringUtils.hasText(registerForm.getPassword())) {
            return ApiResponse.error(400, "账号或密码不可为空");
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, registerForm.getUsername());
        long count = userService.count(wrapper);
        if (count > 0) {
            return ApiResponse.error(400, "该星象轨迹已存在（账号已被注册）");
        }
        User newUser = new User();
        newUser.setUsername(registerForm.getUsername());
        newUser.setPassword(userService.getSalt(registerForm.getPassword()));
        newUser.setStatus(true);
        boolean success = userService.save(newUser);
        if (success) {
            ApiResponse<Map<String, String>> login = userService.login(registerForm.getUsername(), registerForm.getPassword());
            return ApiResponse.success(login.getData().get("token"));
        } else {
            return ApiResponse.error(500, "灵力波动，注册失败");
        }
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
