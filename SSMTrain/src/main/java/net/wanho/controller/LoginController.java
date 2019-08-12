package net.wanho.controller;

import com.github.pagehelper.PageInfo;
import net.wanho.pojo.*;
import net.wanho.service.UserServiceI;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注册登录
 */

@Controller
@RequestMapping("login")
public class LoginController {

    @Autowired
    private UserServiceI userServiceI;


    @RequestMapping("check1")
    public String check1() {
        return "success";
    }

    //    登录注册页面跳转
    @RequestMapping("toLogin")
    public String toLogin() {

        return "login";
    }

    //  注册
    @RequestMapping("register")
    public String register(User user, Map map) {
//        注册失败 跳转失败页面
        String viewName = "fail";
        try {
            userServiceI.register(user);
            viewName = "redirect:toLogin";
        } catch (Exception e) {

            map.put("msg", e.getMessage());
        }
        return viewName;
    }

    //    登录
    @RequestMapping("check")
    public String check(User user, boolean rememberMe, Map map) {
//        注册失败 跳转失败页面
        String viewName = "fail";

        Subject subject = SecurityUtils.getSubject();

        if (user == null) {
            throw new RuntimeException("参数不能为空");
        }

        try {
//            未认证登录
            if (!subject.isAuthenticated()) {
//                加密
                UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), user.getPassword());
//                判断是否选中记住我
                token.setRememberMe(rememberMe);

//                认证登录
                subject.login(token);

//                PrincipalCollection prinCol = subject.getPrincipals();

                viewName = "success";

            }
        } catch (RuntimeException e) {
//            如果密码错误 账号正确 传回来的e.getCause()为空 没有错误信息
            if (e.getCause() != null && !"".equals(e.getCause())) {
//                由于e.getCause()传的值为java.lang.RuntimeException:错误信息语句
//                因此将内容转为字符串并以":"分割
                String s = e.getCause().toString();
                String[] split = s.split(":");
//                放入map中
                String msg = split[1];
                map.put("msg", msg);
            } else {
                map.put("msg", e.getCause());
            }

        }
        return viewName;
    }

    //    登出
    @RequestMapping("/shirologout")
    public String shirologout() {
        try {
            Subject subject = SecurityUtils.getSubject();
//            认证登录为true
            if (subject.isAuthenticated()) {
                subject.logout();
                //登出成功
                return "redirect:toLogin";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }


}
