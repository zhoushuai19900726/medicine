package com.company.project.service.impl;

import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.DictionariesKeyConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopLogisticsCompanyMapper;
import com.company.project.entity.ShopLogisticsCompanyEntity;
import com.company.project.service.ShopLogisticsCompanyService;

import javax.annotation.Resource;
import java.util.List;


@Service("shopLogisticsCompanyService")
public class ShopLogisticsCompanyServiceImpl extends ServiceImpl<ShopLogisticsCompanyMapper, ShopLogisticsCompanyEntity> implements ShopLogisticsCompanyService {

    @Resource
    private ShopLogisticsCompanyMapper shopLogisticsCompanyMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public DataResult saveShopLogisticsCompanyEntity(ShopLogisticsCompanyEntity shopLogisticsCompany) {
        shopLogisticsCompanyMapper.insert(shopLogisticsCompany);
        redisTemplate.boundHashOps(DictionariesKeyConstant.LOGISTICS_COMPANY_KEY).put(shopLogisticsCompany.getId(), shopLogisticsCompanyMapper.selectById(shopLogisticsCompany.getId()));
        return DataResult.success();
    }

    @Override
    public DataResult removeShopLogisticsCompanyEntityByIds(List<String> ids) {
        shopLogisticsCompanyMapper.deleteBatchIds(ids);
        redisTemplate.boundHashOps(DictionariesKeyConstant.LOGISTICS_COMPANY_KEY).delete(ids.toArray());
        return DataResult.success();
    }

    @Override
    public DataResult updateShopLogisticsCompanyEntityById(ShopLogisticsCompanyEntity shopLogisticsCompany) {
        shopLogisticsCompanyMapper.updateById(shopLogisticsCompany);
        redisTemplate.boundHashOps(DictionariesKeyConstant.LOGISTICS_COMPANY_KEY).put(shopLogisticsCompany.getId(), shopLogisticsCompanyMapper.selectById(shopLogisticsCompany.getId()));
        return DataResult.success();
    }


}
