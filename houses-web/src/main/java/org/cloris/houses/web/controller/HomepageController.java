package org.cloris.houses.web.controller;

import org.cloris.houses.biz.service.RecommendService;
import org.cloris.houses.common.model.House;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Jackson Fang
 * Date:   2018/11/11
 * Time:   14:16
 */
@Controller
public class HomepageController {

    @Autowired
    private RecommendService recommendService;

    /**
     * 页面跳转接口。
     *
     * @return 首页
     */
    @RequestMapping({"/", "index"})
    public String index(Model model) {
        List<House> houses = recommendService.getLatest();
        model.addAttribute("recomHouses", houses);
        return "homepage/index";
    }




}
