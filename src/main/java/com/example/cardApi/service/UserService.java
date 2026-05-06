package com.example.cardApi.service;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cardApi.common.ApiResponse;
import com.example.cardApi.entity.User;
import com.example.cardApi.mapper.UserMapper;
import com.example.cardApi.model.UserSaveModel;
import com.example.cardApi.model.UserUpModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IService<User> {
    @Value("${secure.pwd-salt}")
    private String salt;

    //获取盐值
    public String  getSalt(String info) {
        String pwd = SaSecureUtil.sha256BySalt(info, salt);
        return pwd;
    }
    public boolean saveUser(UserSaveModel userModel) {
        User user = new User();
        //入库前拦截明文，替换为 SHA256 密文
        String pwd = this.getSalt(userModel.getPassword());
        BeanUtils.copyProperties(userModel, user);
        user.setPassword(pwd);
        return this.save(user);
    }
    public boolean updateUser(UserUpModel userModel) {
        User user = this.getById(userModel.getId());
        if (userModel.getAddress() != null) {

        }
        if (userModel.getPhone() != null) {

        }
        if (userModel.getAvatar() != null) {

        }
        if (userModel.getRealName() != null) {
            user.setRealName(userModel.getRealName());
        }
        return this.updateById(user);
    }

    public ApiResponse<Map<String, String>> login(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username)
                .eq(User::getPassword, this.getSalt(password))
                .eq(User::getStatus, 1);
        User user = this.getOne(wrapper);
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
}
