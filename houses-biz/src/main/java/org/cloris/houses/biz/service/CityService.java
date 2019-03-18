package org.cloris.houses.biz.service;

import com.google.common.collect.Lists;
import org.cloris.houses.common.model.City;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jackson Fang
 * Date:   2018/11/12
 * Time:   9:42
 */
@Service
public class CityService {
    public List<City> getAllCities() {
        City city = new City();
        city.setCityCode("110000");
        city.setCityName("北京市");
        city.setId(1);
        return Lists.newArrayList(city);
    }
}
