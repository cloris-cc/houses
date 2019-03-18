package org.cloris.houses.biz.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.apache.commons.lang3.RandomStringUtils;
import org.cloris.houses.biz.mapper.UserMapper;
import org.cloris.houses.common.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author Jackson Fang
 * Date:   2018/11/6
 * Time:   23:18
 */
@Service
public class MailService {

    private final Cache<String, String> registerCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .removalListener(new RemovalListener<String, String>() {
                @Override
                public void onRemoval(RemovalNotification<String, String> notification) {
//                    userMapper.delete(notification.getValue());
                }
            }).build();

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JavaMailSender mailSender;

    @Value("${domain.name}")
    private String domainName;
    @Value("${spring.mail.username}")
    private String from;

    /**
     * 给用户发送激活验证邮件。
     * <p>
     * 步骤：
     * 1. 缓存 key-email 的关系。
     * 2. 用 Spring mail 发送邮件。
     * 3. 用异步框架进行异步操作（通知用户和发送邮件）。
     *
     * @param email
     */
    @Async
    public void registerNotify(String email) {
        // 生成 key
        String randomKey = RandomStringUtils.randomAlphabetic(10);
        // 将 key 放到本地缓存中
        registerCache.put(randomKey, email);
        // 创建激活 URL
        String verifyURL = "http://" + domainName + "/accounts/verify?key=" + randomKey;
        // 发送邮件
        sendMail("房产平台激活邮件", verifyURL, email);
    }


    /**
     * 服务端发送验证邮件。
     *
     * @param title     邮件标题
     * @param verifyURL 激活链接
     * @param email     target email
     */
    public void sendMail(String title, String verifyURL, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setText(verifyURL);
        mailSender.send(message);
    }

    public boolean enable(String key) {
        String email = registerCache.getIfPresent(key);
        if(StringUtils.isEmpty(email)) {
            return false;
        }
        User updateUser = new User();
        updateUser.setEmail(email);
        updateUser.setEnable(1);
        userMapper.update(updateUser);
        registerCache.invalidate(key);
        return true;
    }
}
