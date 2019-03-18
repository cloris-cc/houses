package org.cloris.houses.common.page;

import lombok.Data;

/**
 * @author Jackson Fang
 * Date:   2018/11/9
 * Time:   12:30
 */
@Data
public class PageParams {
    private static final Integer DEFAULT_PAGE_SIZE = 2;
    private static final Integer DEFAULT_PAGE_NUM = 1;

    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Integer limit;

    private PageParams(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.offset = pageSize * (pageNum - 1);
        this.limit = pageSize;
    }

    /**
     * PageParams 外部访问入口。
     */
    public static PageParams build(Integer pageNum, Integer pageSize) {
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        if (pageNum == null) {
            pageNum = DEFAULT_PAGE_NUM;
        }
        return new PageParams(pageNum, pageSize);
    }
}
