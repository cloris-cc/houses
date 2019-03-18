package org.cloris.houses.biz.service;


import com.google.common.collect.Lists;
import org.cloris.houses.biz.mapper.UserMapper;
import org.cloris.houses.common.model.User;
import org.cloris.houses.common.utils.BeanHelper;
import org.cloris.houses.common.utils.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FileService fileService;
    @Autowired
    private MailService mailService;

    public List<User> getAllUsers() {
        return userMapper.findAllUsers();
    }

    /**
     * 1. 插入数据库，非激活；密码加盐再 md5；保存头像到本地
     * 2. 生成 key，绑定 email
     * 3. 发送邮件给用户
     *
     * @param account
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addAccount(User account) {
        account.setPasswd(HashUtils.encryptPassword(account.getPasswd()));
        List<String> imgList = fileService.getImgPaths(Lists.newArrayList(account.getAvatarFile()));
        if (!imgList.isEmpty()) {
            account.setAvatar(imgList.get(0));
        }
        BeanHelper.setDefaultProp(account, User.class);
        BeanHelper.onInsert(account);
        account.setEnable(0);
        userMapper.insert(account);
        mailService.registerNotify(account.getEmail());
        return true;
    }

    public boolean enable(String key) {
        return mailService.enable(key);
    }

    /**
     * 验证用户名和密码。
     *
     * @param username 帐号
     * @param password 密码
     * @return
     */
    public User auth(String username, String password) {
        String dbPass = HashUtils.encryptPassword(password);
        User user = userMapper.findUser(username, dbPass);
        return user;
    }

    /**
     * 更新用户个人信息的接口。
     *
     * @param updateUser
     * @param email
     * @return
     */
    public int updateUser(User updateUser, String email) {
        updateUser.setEmail(email);
        BeanHelper.onUpdate(updateUser);
        return userMapper.update(updateUser);
    }
}
