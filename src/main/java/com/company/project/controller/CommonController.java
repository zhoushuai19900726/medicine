package com.company.project.controller;

import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.common.utils.DictionariesKeyConstant;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


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

    @ApiOperation(value = "查询所有性别")
    @GetMapping("findAllSex")
    @ResponseBody
    public DataResult findAllSex() {
        return DataResult.success(analysisRedisData(DictionariesKeyConstant.SEX));
    }

    @ApiOperation(value = "查询所有商品上下架状态")
    @GetMapping("findAllGoodsStatus")
    @ResponseBody
    public DataResult findAllGoodsStatus() {
        return DataResult.success(analysisRedisData(DictionariesKeyConstant.GOODS_STATUS));
    }

    @ApiOperation(value = "查询所有商品审核状态")
    @GetMapping("findAllGoodsExamineStatus")
    @ResponseBody
    public DataResult findAllGoodsExamineStatus() {
        return DataResult.success(analysisRedisData(DictionariesKeyConstant.GOODS_EXAMINE_STATUS));
    }

    @ApiOperation(value = "查询所有商品服务保证")
    @GetMapping("findAllServiceGuarantee")
    @ResponseBody
    public DataResult findAllServiceGuarantee() {
        return DataResult.success(analysisRedisData(DictionariesKeyConstant.SERVICE_GUARANTEE));
    }

//    @ApiOperation(value = "查询所有钱包明细类型")
//    @GetMapping("findAllWalletDetailsType")
//    @ResponseBody
//    public DataResult findAllWalletDetailsType() {
//        return DataResult.success(analysisRedisData(DictionariesKeyConstant.WALLET_DETAILS_TYPE));
//    }


    @ApiOperation(value = "根据字典值获取字典名称")
    @GetMapping("getLabelByValue")
    @ResponseBody
    public DataResult getLabelByValue(String dictionariesKey, String value) {
        AtomicReference<String> label = new AtomicReference<>(DelimiterConstants.EMPTY_STR);
        redisTemplate.boundHashOps(dictionariesKey).values().forEach(obj -> {
            try {
                Map<String, String> map = BeanUtils.describe(obj);
                if(StringUtils.equals(map.get("value"), value)){
                    label.set(map.get("label"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return DataResult.success(label.get());
    }

    private List<Map<String, String>> analysisRedisData(String key) {
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
        // 排序
        result.sort(Comparator.comparing(a -> a.get("sort")));
        return result;
    }

}
