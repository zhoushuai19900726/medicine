package com.company.project;

import com.company.project.common.utils.NumberConstants;
import com.company.project.entity.ShopCategoryEntity;
import com.company.project.mapper.ShopCategoryMapper;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyFrameApplicationTests {

    @Resource
    private ShopCategoryMapper shopCategoryMapper;

    @Test
    public void contextLoads() {


//        shopCategoryMapper.updateCount(Lists.newArrayList(new ShopCategoryEntity("1405038097969156098", -NumberConstants.ONE), new ShopCategoryEntity("1405083876230340610", -NumberConstants.ONE), new ShopCategoryEntity("1405444419189420034", -NumberConstants.ONE)));

    }

}
