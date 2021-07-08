package com.company.project;

import com.company.project.common.utils.Constant;
import com.company.project.common.utils.NumberConstants;
import com.company.project.entity.ShopCategoryEntity;
import com.company.project.entity.ShopSpuEntity;
import com.company.project.entity.SysDictDetailEntity;
import com.company.project.mapper.ShopCategoryMapper;
import com.company.project.mapper.ShopSpuMapper;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyFrameApplicationTests {

    @Resource
    private ShopCategoryMapper shopCategoryMapper;
    @Resource
    private ShopSpuMapper shopSpuMapper;
    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void contextLoads() {
//        ShopSpuEntity shopSpuEntity = new ShopSpuEntity();
//        shopSpuEntity.setId("hahahaha");
//        shopSpuMapper.insert(shopSpuEntity);
//        shopCategoryMapper.updateCount(Lists.newArrayList(new ShopCategoryEntity("1405038097969156098", -NumberConstants.ONE), new ShopCategoryEntity("1405083876230340610", -NumberConstants.ONE), new ShopCategoryEntity("1405444419189420034", -NumberConstants.ONE)));


//        redisTemplate.boundHashOps(Constant.DICT_KEY_PREFIX.concat("1412958452197052417")).values().forEach(obj -> {
//            SysDictDetailEntity sysDictDetailEntity = (SysDictDetailEntity) obj;
//            System.out.println(sysDictDetailEntity.getLabel() + ":" + sysDictDetailEntity.getValue());
//        });
        redisTemplate.keys("*").forEach(a -> {
            System.out.println("==================");
            System.out.println(a);

        });
        redisTemplate.delete(redisTemplate.keys("*"));

    }

}
