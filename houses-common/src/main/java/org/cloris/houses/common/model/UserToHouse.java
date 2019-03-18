package org.cloris.houses.common.model;

import lombok.Data;

import java.util.Date;

/**
 * @author Jackson Fang
 * Date:   2018/11/12
 * Time:   12:58
 */
@Data
public class UserToHouse {
    private Long id;
    private Long houseId;
    private Long userId;
    private Date createTime;
    private Integer type;

}
