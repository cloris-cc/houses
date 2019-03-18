package org.cloris.houses.common.page;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author Jackson Fang
 * Date:   2018/11/10
 * Time:   13:11
 */
@Data
public class Pagination {
    private int pageNum;
    private int pageSize;
    private long totalCount;
    private List<Integer> pages = Lists.newArrayList();

    public Pagination(Integer pageSize, Integer pageNum, Long totalCount) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.totalCount = totalCount;
        for (int i = 1; i <= pageNum; i++) {
            pages.add(i);
        }
        Long pageCount = totalCount / pageSize + ((totalCount % pageSize == 0) ? 0 : 1);
        if (pageCount > pageNum) {
            for (int i = pageNum + 1; i < pageCount; i++) {
                pages.add(i);
            }
        }
    }
}
