package org.cloris.houses.biz.service;

import com.google.common.base.Joiner;
import org.cloris.houses.biz.mapper.HouseMapper;
import org.cloris.houses.common.constants.UserToHouseType;
import org.cloris.houses.common.model.*;
import org.cloris.houses.common.page.PageData;
import org.cloris.houses.common.page.PageParams;
import org.cloris.houses.common.utils.BeanHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.management.relation.Relation;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jackson Fang
 * Date:   2018/11/9
 * Time:   10:52
 */
@Service
@Transactional
public class HousesService {

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private MailService mailService;

    @Autowired
    private FileService fileService;

    @Value("${file.path}")
    private String imgPrefix;

    /**
     * 1. 查询小区
     * 2. 添加图片服务地址前缀
     * 3. 构建分页结果
     *
     * @param query
     * @param pageParams
     * @return
     */
    public PageData<House> queryHouse(House query, PageParams pageParams) {
        List<House> houses;
        if (query.getName() != null && !StringUtils.isEmpty(query.getName())) {
            Community community = new Community();
            community.setName(query.getName());
            // 你不想写一个万能的查询语句？
            List<Community> communities = houseMapper.selectCommunity(community);
            if (!communities.isEmpty()) { // 如果为空的话则会查询表中所有 communities。
                query.setCommunityId(communities.get(0).getId());
            }
        }
        houses = queryAndSetImg(query, pageParams); // 此方法调用houseMapper.selectPageHouse();
        Long count = houseMapper.selectPageCount(query);
        return PageData.buildPage(houses, count, pageParams.getPageSize(), pageParams.getPageNum());
    }


    public House queryOneHouse(Long id) {
        House query = new House();
        query.setId(id);
        List<House> houses = queryAndSetImg(query, PageParams.build(1, 1));
        if (!houses.isEmpty()) {
            return houses.get(0);
        }
        return null;
    }

    public List<House> queryAndSetImg(House query, PageParams pageParams) {
        List<House> houses = houseMapper.selectPageHouses(query, pageParams);
        houses.forEach(h -> {
            h.setFirstImg(imgPrefix + h.getFirstImg());
            h.setImageList(h.getImageList().stream().map(img -> imgPrefix + img).collect(Collectors.toList()));
            h.setFloorPlanList(h.getFloorPlanList().stream().map(img -> imgPrefix + img).collect(Collectors.toList()));
        });
        return houses;
    }

    public void addUserMsg(UserMsg userMsg) {
        BeanHelper.onInsert(userMsg);
        houseMapper.insertUserMsg(userMsg);
        User agent = agencyService.getAgentDetail(userMsg.getAgentId());
        mailService.sendMail("来自用户" + userMsg.getEmail() + "的留言", userMsg.getMsg(), agent.getEmail());
    }

    public List<Community> getAllCommunities() {
        Community community = new Community();
        return houseMapper.selectCommunity(community);
    }

    /**
     * 1. 添加房产图片
     * 2. 添加户型图片
     * 3. 插入房产信息
     * 4. 绑定用户关系 *
     *
     * @param house       待添加的 house
     * @param currentUser 执行该操作的 user
     */
    public void addHouse(House house, User currentUser) {
        if (!CollectionUtils.isEmpty(house.getHouseFiles())) { // 判断用户是否上传了房屋的图片。
            String images = Joiner.on(",").join(fileService.getImgPaths(house.getHouseFiles()));
            house.setImages(images);
        }

        if (!CollectionUtils.isEmpty(house.getFloorPlanFiles())) {
            String images = Joiner.on(",").join(fileService.getImgPaths(house.getFloorPlanFiles()));
            house.setFloorPlan(images);
        }

        BeanHelper.onInsert(house); // 填充 house 的 create_time 信息。
        houseMapper.insert(house);

        bindUserToHouse(house.getId(), currentUser.getId(), false);
    }

    /**
     * 在 house_user 表中绑定 house 和 user 的关系。
     */
    public void bindUserToHouse(Long houseId, Long userId, boolean isCollected) {
        UserToHouse oldRelation = houseMapper.selectUserToHouse(userId, houseId, isCollected ? UserToHouseType.BOOKMARK : UserToHouseType.SALE);
        if (oldRelation != null) {
            return;
        }
        UserToHouse relation = new UserToHouse();
        relation.setHouseId(houseId);
        relation.setUserId(userId);
        relation.setType(isCollected ? UserToHouseType.BOOKMARK.value : UserToHouseType.SALE.value);
        BeanHelper.setDefaultProp(relation, UserToHouse.class);
        BeanHelper.onInsert(relation);
        houseMapper.insertUserToHouse(relation);
    }

    public int updateRating(Long id, Double rating) {
        House house = queryOneHouse(id);
        Double oldRating = house.getRating();
        Double newRating = oldRating.equals(0D) ? rating : Math.min((oldRating + rating) / 2, 5);

        House updateHouse = new House();
        updateHouse.setId(id);
        updateHouse.setRating(newRating);
        BeanHelper.onUpdate(updateHouse);
        return houseMapper.updateHouse(updateHouse);

    }

    public void unbindUserToHouse(Long houseId, Long userId, UserToHouseType type) {
        houseMapper.deleteUserToHouse(houseId, userId, type.value);
    }
}
