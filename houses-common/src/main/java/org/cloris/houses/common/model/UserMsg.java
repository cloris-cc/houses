package org.cloris.houses.common.model;

import lombok.Data;

import java.util.Date;

/**
 * 蓝瘦的双十一鸭 >o<
 *
 * @author Jackson Fang
 * Date:   2018/11/11
 * Time:   6:44
 */
@Data
public class UserMsg {
    private Long id;
    private String msg;
    private Long userId;
    private Date createTime;
    private Long agentId;
    private Long houseId;
    private String email;
    private String userName;
}
