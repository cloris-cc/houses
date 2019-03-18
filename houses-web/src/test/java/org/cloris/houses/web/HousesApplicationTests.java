package org.cloris.houses.web;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.cloris.houses.biz.service.HousesService;
import org.cloris.houses.common.model.House;
import org.cloris.houses.common.page.PageParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HousesApplicationTests {

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private HousesService housesService;
/*
    @Test
    public void contextLoads() {
    }*/

    //    @Test
    public void testHttpClient() throws IOException {
        System.out.println(EntityUtils.toString(httpClient.execute(new HttpGet("http://www.baidu.com")).getEntity()));

//        System.out.println(httpClient.execute(new HttpGet("http://www.baidu.com")).getEntity().toString());

    }

    @Test
    public void test1() {
        House query = new House();
        query.setId(23l);
        System.out.println(housesService.queryHouse(query, PageParams.build(1, 2)));

    }

}
