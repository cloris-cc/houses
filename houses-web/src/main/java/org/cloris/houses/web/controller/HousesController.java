package org.cloris.houses.web.controller;

import org.cloris.houses.biz.service.AgencyService;
import org.cloris.houses.biz.service.CityService;
import org.cloris.houses.biz.service.HousesService;
import org.cloris.houses.biz.service.RecommendService;
import org.cloris.houses.common.constants.CommonConstants;
import org.cloris.houses.common.constants.UserToHouseType;
import org.cloris.houses.common.model.House;
import org.cloris.houses.common.model.User;
import org.cloris.houses.common.model.UserMsg;
import org.cloris.houses.common.page.PageData;
import org.cloris.houses.common.page.PageParams;
import org.cloris.houses.common.result.ResultMsg;
import org.cloris.houses.web.interceptor.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Jackson Fang
 * Date:   2018/11/9
 * Time:   10:51
 */
@Controller
public class HousesController {

    @Autowired
    private HousesService housesService;

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private CityService cityService;

    /**
     * 展示房产列表的接口。需要实现以下功能：
     * 1. 分页
     * 2. 支持小区搜索、类型搜索
     * 3. 支持排序
     * 4. 展示房子的图片、价格、标题、地址等信息
     * <p>
     * 如果不带 request 参数，拦截器能够拿到该接口的 request 吗？
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示房子的数量
     * @param query    查询 house 的条件
     * @return
     */
    @RequestMapping("/house/list")
    public String houseList(Integer pageNum, Integer pageSize, House query, Model model) {
        PageData<House> ps = housesService.queryHouse(query, PageParams.build(pageNum, pageSize));
        List<House> hotHouses = recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
        model.addAttribute("recomHouses", hotHouses);
        model.addAttribute("ps", ps);
        model.addAttribute("vo", query);
        return "house/listing";
    }


    /**
     * 房屋详情接口。需要实现以下功能：
     * 1. 通过房屋 id 查询房屋详情
     * 2. 查询关联经纪人
     *
     * @param id 房屋 id
     * @return
     */
    @RequestMapping("house/detail")
    public String houseDetail(Long id, Model model) {
        House house = housesService.queryOneHouse(id);
        recommendService.incerease(id);
//        if (house.getUserId() != null && house.getUserId() != 0) {
//            model.addAttribute("agent", agencyService.getAgentDetail(house.getUserId()));
//        }

        List<House> rcHouses = recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
        model.addAttribute("recomHouses", rcHouses);
        model.addAttribute("agent", agencyService.getAgentDetail(house.getUserId()));
        model.addAttribute("house", house);
        return "house/detail";
    }

    /**
     * 留言接口。
     *
     * @param userMsg
     * @return
     */
    @RequestMapping("house/leaveMsg")
    public String houseMsg(UserMsg userMsg) {
        housesService.addUserMsg(userMsg);
        return "redirect:/house/detail?id=" + userMsg.getHouseId();
    }

    @RequestMapping("house/toAdd")
    public String toAdd(Model model) {
        // 有空再把前端模版的单词改正...
        model.addAttribute("citys", cityService.getAllCities());
        model.addAttribute("communitys", housesService.getAllCommunities());
        return "house/add";
    }

    /**
     * 1. 获取用户
     * 2. 设置房产状态
     * 3. 添加房产
     *
     * @param house
     * @return
     */
    @RequestMapping("house/add")
    public String doAdd(House house) {
        System.out.println("用户上传的房屋信息：" + house);
        User currentUser = UserContext.getUser();
        house.setState(CommonConstants.HOUSE_STATE_UP);
        housesService.addHouse(house, currentUser);
        return "redirect:/house/ownlist"; // redirect 会把 Session 清除掉吗？不会，等到浏览器关闭才清除。
    }

    @RequestMapping("house/ownlist")
    public String ownlist(House house, Integer pageNum, Integer pageSize, Model model) {
        User user = UserContext.getUser();
        house.setUserId(user.getId());
        house.setBookmarked(false);
        model.addAttribute("ps", housesService.queryHouse(house, PageParams.build(pageNum, pageSize)));
        model.addAttribute("pageType", "own");
        return "house/ownlist";
    }

    /**
     * 1. 评分
     */
    @ResponseBody
    @RequestMapping("house/rating")
    public ResultMsg houseRate(Double rating, Long id) {
        housesService.updateRating(id, rating);
        return ResultMsg.successMsg("success");
    }

    /**
     * 2. 收藏
     */
    @ResponseBody
    @RequestMapping("house/bookmark")
    public ResultMsg bookmark(Long id) {
        User user = UserContext.getUser();
        housesService.bindUserToHouse(id, user.getId(), true);
        return ResultMsg.successMsg("success");
    }

    /**
     * 3. 删除收藏
     */
    @ResponseBody
    @RequestMapping("house/unbookmark")
    public ResultMsg unbookmark(Long id) {
        User user = UserContext.getUser();
        housesService.unbindUserToHouse(id, user.getId(), UserToHouseType.BOOKMARK);
        return ResultMsg.successMsg("success");
    }

    @RequestMapping("house/del")
    public String delsale(Long id, String pageType) {
        User user = UserContext.getUser();
        housesService.unbindUserToHouse(id, user.getId(), pageType.equals("own") ? UserToHouseType.SALE : UserToHouseType.BOOKMARK);
        return "redirect:/house/ownlist";
    }

    /**
     * 4. 收藏列表
     */
    @RequestMapping("house/bookmarked")
    public String bookmarked(House house, Integer pageNum, Integer pageSize, Model model) {
        User user = UserContext.getUser();
        house.setBookmarked(true);
        house.setUserId(user.getId());
        model.addAttribute("ps", housesService.queryHouse(house, PageParams.build(pageNum, pageSize)));
        model.addAttribute("pageType", "book");
        return "house/ownlist";
    }
}
