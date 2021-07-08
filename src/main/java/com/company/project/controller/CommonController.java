package com.company.project.controller;

import com.company.project.common.enums.GoodsExamineStatusEnum;
import com.company.project.common.enums.GoodsStatusEnum;
import com.company.project.common.enums.ServiceGuaranteeEnum;
import com.company.project.common.utils.Constant;
import com.company.project.common.utils.DataResult;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;


/**
 * 公共Controller
 *
 * @author zhoushuai
 * @version V1.0
 * @date 2020年3月18日
 */
@Api(tags = "公共Controller")
@Controller
@RequestMapping("/common")
public class CommonController {

    @Resource
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "查询所有商品上下架状态")
    @GetMapping("findAllGoodsStatus")
    @RequiresPermissions("goods:list")
    @ResponseBody
    public DataResult findAllGoodsStatus() {
        return DataResult.success(GoodsStatusEnum.toList());
    }

    @ApiOperation(value = "查询所有商品审核状态")
    @GetMapping("findAllGoodsExamineStatus")
    @RequiresPermissions("goods:list")
    @ResponseBody
    public DataResult findAllGoodsExamineStatus() {
        return DataResult.success(GoodsExamineStatusEnum.toList());
    }

    @ApiOperation(value = "查询所有商品服务保证")
    @GetMapping("findAllServiceGuarantee")
    @RequiresPermissions("goods:list")
    @ResponseBody
    public DataResult findAllServiceGuarantee() {
        return DataResult.success(getDataFromRedis(Constant.SERVICE_GUARANTEE));
    }



    private List<Map<String, String>> getDataFromRedis(String key){
        List<Map<String, String>> result = Lists.newArrayList();
        redisTemplate.boundHashOps(key).values().forEach(obj -> {
            try {
                Map<String, String> map = BeanUtils.describe(obj);
                map.put("id", map.get("value"));
                map.put("name", map.get("label"));
                result.add(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return  result;
    }

}
