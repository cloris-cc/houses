package org.cloris.houses.common.constants;

/**
 * @author Jackson Fang
 * Date:   2018/11/12
 * Time:   13:00
 */
public enum UserToHouseType {
    SALE(1), BOOKMARK(2);

    public final Integer value;

    UserToHouseType(Integer value) {
        this.value = value;
    }
}
