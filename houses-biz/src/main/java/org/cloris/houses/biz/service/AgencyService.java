package org.cloris.houses.biz.service;

import org.cloris.houses.biz.mapper.AgencyMapper;
import org.cloris.houses.common.model.User;
import org.cloris.houses.common.page.PageData;
import org.cloris.houses.common.page.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jackson Fang
 * Date:   2018/11/11
 * Time:   5:18
 */
@Service
public class AgencyService {

    @Autowired
    private AgencyMapper agencyMapper;

    @Value("${file.path}")
    private String imgPrefix;

    /**
     * 查询房屋经纪人的方法。
     * <p>
     * 从 User 表中获取详情。
     * 添加用户头像
     *
     * @param userId 用户 id
     * @return agent 查询的经纪人
     */
    public User getAgentDetail(Long userId) {
        User query = new User();
        query.setId(userId);
        query.setType(2);
        List<User> list = agencyMapper.selectAgent(query, PageParams.build(1, 1));
        setImg(list);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 设置用户头像。
     */
    private void setImg(List<User> list) {
        list.forEach(user -> {
            user.setAvatar(imgPrefix + user.getAvatar());
        });
    }

    public PageData<User> getAllAgent(PageParams pageParams) {
        List<User> agents = agencyMapper.selectAgent(new User(), pageParams);
        setImg(agents);
        Long count = agencyMapper.selectAgentCount(new User());
        return PageData.buildPage(agents, count, pageParams.getPageSize(), pageParams.getPageNum());
    }
}
