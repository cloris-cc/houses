package org.cloris.houses.biz.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import org.cloris.houses.common.model.House;
import org.cloris.houses.common.page.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Jackson Fang
 * Date:   2018/11/11
 * Time:   11:05
 */
@Service
public class RecommendService {
    @Autowired
    private StringRedisTemplate template;
    @Autowired
    private HousesService housesService;

    private static final String HOT_HOUSE_KEY = "hot_house";

    public void incerease(Long id) {
        template.opsForZSet().incrementScore(HOT_HOUSE_KEY, id + "", 1.0D);
    }

    public List<Long> getHot() {
        Set<String> idSet = template.opsForZSet().reverseRange(HOT_HOUSE_KEY, 0, -1);
        List<Long> ids = idSet.stream().map(Long::parseLong).collect(Collectors.toList());
        return ids;
    }

    public List<House> getHotHouse(Integer size) {
        House query = new House();
        List<Long> list = getHot();
        list = list.subList(0, Math.min(list.size(), size));
        if (list.isEmpty()) {
            return Lists.newArrayList();
        }
        query.setIds(list);
        final List<Long> order = list;
        List<House> houses = housesService.queryAndSetImg(query, PageParams.build(1, size));
        Ordering<House> houseSort = Ordering.natural().onResultOf(hs -> order.indexOf(hs.getId()));
        return houseSort.sortedCopy(houses);
    }


    /**
     * 获取最新房源。
     *
     * @return houses
     */
    public List<House> getLatest() {
        House query = new House();
        query.setSort("create_time");
        List<House> houses = housesService.queryAndSetImg(query, PageParams.build(1, 8));
        return houses;
    }
}
