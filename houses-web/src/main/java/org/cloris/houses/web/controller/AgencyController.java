package org.cloris.houses.web.controller;

import org.cloris.houses.biz.service.AgencyService;
import org.cloris.houses.biz.service.HousesService;
import org.cloris.houses.common.model.House;
import org.cloris.houses.common.model.User;
import org.cloris.houses.common.page.PageData;
import org.cloris.houses.common.page.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Jackson Fang
 * Date:   2018/11/11
 * Time:   8:56
 */
@Controller
public class AgencyController {

    @Autowired
    private AgencyService agencyService;
    @Autowired
    private HousesService housesService;

    @RequestMapping("/agency/agentList")
    public String agentList(Integer pageSize, Integer pageNum, Model model) {
        PageData<User> ps = agencyService.getAllAgent(PageParams.build(pageNum,pageSize));
        model.addAttribute("ps", ps);
        return "/user/agent/agentList"; // 页面还没写
    }

    @RequestMapping("/agency/agentDetail")
    public String agentDetail(Long id,Model model) {
        User user = agencyService.getAgentDetail(id);
        House query = new House();
        query.setUserId(id);
        query.setBookmarked(false);
        PageData<House> bindHouse = housesService.queryHouse(query, PageParams.build(1,3));
        if (bindHouse != null) {
            model.addAttribute("bindHouses", bindHouse.getList());
        }
        model.addAttribute("agent", user);
        return "/user/agent/agentDetail";
    }

}
