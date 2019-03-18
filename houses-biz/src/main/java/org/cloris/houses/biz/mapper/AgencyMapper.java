package org.cloris.houses.biz.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.cloris.houses.common.model.User;
import org.cloris.houses.common.page.PageParams;

import java.util.List;

/**
 * @author Jackson Fang
 * Date:   2018/11/11
 * Time:   5:36
 */
@Mapper
public interface AgencyMapper {

    List<User> selectAgent(@Param("user") User query, @Param("pageParams") PageParams pageParams);

    Long selectAgentCount(@Param("user") User query);
}
