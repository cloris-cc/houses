package org.cloris.houses.biz.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.cloris.houses.common.constants.UserToHouseType;
import org.cloris.houses.common.model.Community;
import org.cloris.houses.common.model.House;
import org.cloris.houses.common.model.UserMsg;
import org.cloris.houses.common.model.UserToHouse;
import org.cloris.houses.common.page.PageParams;

import java.util.List;

/**
 * @author Jackson Fang
 * Date:   2018/11/9
 * Time:   13:11
 */
@Mapper
public interface HouseMapper {

    List<House> selectPageHouses(@Param("house") House house, @Param("pageParams") PageParams pageParams);

    Long selectPageCount(@Param("house") House query);

    /**
     * @param community 注意这里不加 @Param 的原因
     * @return communities
     */
    List<Community> selectCommunity(Community community);

    int insertUserMsg(UserMsg userMsg);

    int insert(House house);


    UserToHouse selectUserToHouse(@Param("userId") Long userId, @Param("id") Long houseId, @Param("type") UserToHouseType userToHouseType);

    int insertUserToHouse(UserToHouse relation);

    int updateHouse(House updateHouse);

    int deleteUserToHouse(@Param("houseId") Long houseId, @Param("userId") Long userId, @Param("type") Integer value);
}
