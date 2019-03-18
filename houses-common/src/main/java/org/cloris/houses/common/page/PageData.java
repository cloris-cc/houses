package org.cloris.houses.common.page;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Jackson Fang
 * Date:   2018/11/10
 * Time:   13:07
 */
@Data
@AllArgsConstructor
public class PageData<T> {
    private List<T> list; // 结果列表
    private Pagination pagination;

    public static <T> PageData<T> buildPage(List<T> dataList, long count, Integer pageSize, Integer pageNum) {
        Pagination pagination = new Pagination(pageSize, pageNum, count);
        return new PageData<>(dataList, pagination);
    }
}
