package org.cloris.houses.web.controller;

import org.cloris.houses.biz.service.UserService;
import org.cloris.houses.common.constants.CommonConstants;
import org.cloris.houses.common.model.User;
import org.cloris.houses.common.result.ResultMsg;
import org.cloris.houses.common.utils.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /*    @GetMapping("/users")
    @ResponseBody
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }*/

    /**
     * 注册提交：1. 注册验证 2. 发送邮件 3. 验证失败重定向到注册页面。
     * 注册页获取：根据 account 对象为依据判断是否注册页获取请求。
     */
    @RequestMapping("accounts/register")
    public String accountsRegister(User account, Model model) {
        if (account == null || account.getName() == null) {
            return "/user/accounts/register";
        }
        // 用户验证
        ResultMsg resultMsg = UserHelper.validate(account);
        if (resultMsg.isSuccess() && userService.addAccount(account)) {
            model.addAttribute("email", account.getEmail());
            return "/user/accounts/registerSubmit";
        } else {
            return "redirect:/accounts/register?" + resultMsg.asUrlParams();
        }

    }


    /**
     * 激活验证接口。
     */
    @RequestMapping("accounts/verify")
    public String verify(String key) {
        boolean isVerified = userService.enable(key);
        if (isVerified) {
            return "redirect:/index?" + ResultMsg.successMsg("activation-succeed").asUrlParams();
        } else {
            return "redirect:/accounts/register?" + ResultMsg.errorMsg("activation-failed").asUrlParams();
        }
    }

    /**
     * 登录接口。
     * <p>
     * 需要 HttpServletRequest 来存储登录会话的 Session。
     * 后续使用参数解析器来判断 User 是否为空。
     */
    @RequestMapping("accounts/signIn")
    public String login(HttpServletRequest req) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String target = req.getParameter("target");

        if (username == null || password == null) {
            req.setAttribute("target", target);
            return "/user/accounts/signIn";
        }
        // 验证用户名和密码。
        User user = userService.auth(username, password);
        if (user == null) {
            return "redirect:/accounts/singIn?" + "target=" + target
                    + "&username=" + username + "&" + ResultMsg.errorMsg("wrongUsernameOrPassword").asUrlParams();
        } else { // login success
            // Session 的作用：存储 User 对象
            HttpSession session = req.getSession(true);
            session.setAttribute(CommonConstants.USER_ATTRIBUTE, user);
            session.setAttribute(CommonConstants.PLAIN_USER_ATTRIBUTE, user);
            return StringUtils.isEmpty(target) ? "redirect:/index" : "redirect:" + target;
        }
    }

    /**
     * 注销登录。
     */
    @RequestMapping("accounts/logout")
    public String logout(HttpServletRequest request) {
        // 注销 Session
        HttpSession session = request.getSession(true);
        session.invalidate();
        return "redirect:/index";
    }

    /**
     * 个人信息页接口。
     * <p>
     * 1. 能够提供页面信息。
     * 2. 更新用户信息。
     */
    @RequestMapping("accounts/profile")
    public String profile(HttpServletRequest request, User updateUser) {
        if (updateUser.getEmail() == null) {
            return "/user/accounts/profile";
        }
        userService.updateUser(updateUser, updateUser.getEmail());
        // 不进行数据库的查找，直接将 updateUser 添加到 Session 中。
        request.getSession(true).setAttribute(CommonConstants.USER_ATTRIBUTE, updateUser);
        return "redirect:/accounts/profile?" + ResultMsg.successMsg("updateSucceed").asUrlParams();
    }

    /**
     * 修改密码的接口。
     */
    @RequestMapping("accounts/changePassword")
    public String changePassword(String email, String password, String newPassword, String confirmPassword, Model model) {
        User user = userService.auth(email, password);
        if (user == null || !confirmPassword.equals(newPassword)) {
            return "redirect:/accounts/profile?" + ResultMsg.errorMsg("wrongPassword").asUrlParams();
        }
        User updateUser = new User();
        updateUser.setPasswd(HashUtils.encryptPassword(newPassword));
        userService.updateUser(updateUser, updateUser.getEmail());
        return "redirect:/accounts/profile?" + ResultMsg.successMsg("passwordChanged").asUrlParams();
    }

}
