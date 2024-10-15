package com.lyc.teamnav.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.lyc.teamnav.bean.dto.ChangePasswordDto;
import com.lyc.teamnav.bean.entity.User;
import com.lyc.teamnav.common.utils.SecurityUtils;
import com.lyc.teamnav.repository.UserRepository;
import com.lyc.teamnav.service.IUserService;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserService implements IUserService, UserDetailsService, ApplicationRunner {

    @Resource
    private UserRepository userRepository;

    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    @Value("${change-password.enable}")
    private Boolean changePwdEnable;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user = get();
        // 不支持在线修改密码，则每次都会读取配置，使得通过配置修改密码生效
        if (BooleanUtil.isFalse(changePwdEnable)
                || StringUtils.isBlank(user.getPassword())) {
            // 加载默认配置，兼容老版本
            user.setNickname(SecurityUtils.DEFAULT_USER.getNickname());
            user.setAvatar(SecurityUtils.DEFAULT_USER.getAvatar());
            user.setUsername(username);
            user.setPassword(new BCryptPasswordEncoder().encode(password));
            userRepository.save(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = get();
        Assert.isTrue(StrUtil.equals(username, user.getUsername()), "用户名或密码错误");
        return user;
    }

    /**
     * changePassword
     *
     * @param changePassword ChangePasswordDto
     */
    public void changePassword(ChangePasswordDto changePassword) {
        User user = get();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Assert.isTrue(passwordEncoder.matches(changePassword.getOldPassword(),
                user.getPassword()), "原密码不匹配");
        user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        userRepository.save(user);
    }

    private User get() {
        return userRepository.findAll().stream().findFirst()
                .orElse(new User().setId("1"));
    }

}
