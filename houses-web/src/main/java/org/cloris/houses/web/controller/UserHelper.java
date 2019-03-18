package org.cloris.houses.web.controller;

import org.cloris.houses.common.model.User;
import org.cloris.houses.common.result.ResultMsg;
import org.springframework.util.StringUtils;

public class UserHelper {

    public static ResultMsg validate(User account) {
        if (StringUtils.isEmpty(account.getEmail())) {
            return ResultMsg.errorMsg("Email 有误");
        }
        if (StringUtils.isEmpty(account.getPasswd()) || StringUtils.isEmpty(account.getConfirmPasswd()) || !account.getPasswd().equals(account.getConfirmPasswd())) {
            return ResultMsg.errorMsg("密码有误");
        }
        if(account.getPasswd().length() < 6) {
            return ResultMsg.errorMsg("密码长度小于 6 位");
        }
        return ResultMsg.successMsg("");
    }

}
