package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.common.utils.NumberConstants;
import com.company.project.entity.ShopTemplateEntity;
import com.company.project.mapper.ShopTemplateMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopCategoryMapper;
import com.company.project.entity.ShopCategoryEntity;
import com.company.project.service.ShopCategoryService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("shopCategoryService")
public class ShopCategoryServiceImpl extends ServiceImpl<ShopCategoryMapper, ShopCategoryEntity> implements ShopCategoryService {

    @Resource
    private ShopCategoryMapper shopCategoryMapper;

    @Resource
    private ShopTemplateMapper shopTemplateMapper;

    @Override
    public ShopCategoryEntity getShopCategoryEntityById(String id) {
        ShopCategoryEntity shopCategoryEntity = shopCategoryMapper.selectById(id);
        if (Objects.nonNull(shopCategoryEntity)) {
            ShopTemplateEntity shopTemplateEntity = shopTemplateMapper.selectById(shopCategoryEntity.getTemplateId());
            if (Objects.nonNull(shopTemplateEntity)) {
                shopCategoryEntity.setTemplateName(shopTemplateEntity.getName());
            }
        }
        return shopCategoryEntity;
    }

    @Override
    public List<ShopCategoryEntity> findSubordinateCategoryList(String parentId) {
        return shopCategoryMapper.selectList(Wrappers.<ShopCategoryEntity>lambdaQuery().eq(ShopCategoryEntity::getParentId, parentId).orderByAsc(ShopCategoryEntity::getSeq));
    }

    @Override
    public List<ShopCategoryEntity> listByAll() {
        List<ShopCategoryEntity> shopCategoryEntityList = shopCategoryMapper.selectList(Wrappers.<ShopCategoryEntity>lambdaQuery().orderByAsc(ShopCategoryEntity::getSeq));
        if (CollectionUtils.isNotEmpty(shopCategoryEntityList)) {
            Map<String, List<ShopCategoryEntity>> groupBy = shopCategoryEntityList.stream().collect(Collectors.groupingBy(ShopCategoryEntity::getParentId));
            shopCategoryEntityList.forEach(shopCategoryEntity -> shopCategoryEntity.setChildren(groupBy.get(shopCategoryEntity.getId())));
        }
        return shopCategoryEntityList.stream().filter(a -> a.getParentId().equals(NumberConstants.ZERO_STR)).collect(Collectors.toList());
    }

    @Override
    public IPage<ShopCategoryEntity> listByPage(Page<ShopCategoryEntity> page, LambdaQueryWrapper<ShopCategoryEntity> wrapper) {
        IPage<ShopCategoryEntity> iPage = shopCategoryMapper.selectPage(page, wrapper);
        // ??????????????????
        List<ShopCategoryEntity> shopCategoryEntityList = iPage.getRecords();
        if (CollectionUtils.isNotEmpty(shopCategoryEntityList)) {
            //  ????????????ID??????
            List<String> templateIdList = shopCategoryEntityList.stream().map(ShopCategoryEntity::getTemplateId).collect(Collectors.toList());
            // ??????????????????
            List<ShopTemplateEntity> shopTemplateEntityList = shopTemplateMapper.selectBatchIds(templateIdList);
            if (CollectionUtils.isNotEmpty(shopTemplateEntityList)) {
                // ??????????????????
                Map<String, String> shopTemplateEntityMap = shopTemplateEntityList.stream().collect(Collectors.toMap(ShopTemplateEntity::getId, ShopTemplateEntity::getName, (k1, k2) -> k1));
                shopCategoryEntityList.forEach(shopCategoryEntity -> shopCategoryEntity.setTemplateName(shopTemplateEntityMap.getOrDefault(shopCategoryEntity.getTemplateId(), DelimiterConstants.EMPTY_STR)));
            }
        }
        return iPage;
    }

    @Override
    public Map<String, String> getAllParent(List<String> category3IdList) {
        List<ShopCategoryEntity> shopCategoryEntityList = Lists.newArrayList();
        Map<String, String> returnMap = Maps.newHashMap();
        // ????????????
        if (CollectionUtils.isNotEmpty(category3IdList)) {
            List<ShopCategoryEntity> shopCategory3EntityList = shopCategoryMapper.selectBatchIds(category3IdList);
            if (CollectionUtils.isNotEmpty(shopCategory3EntityList)) {
                shopCategoryEntityList.addAll(shopCategory3EntityList);
                // ????????????
                List<String> category2IdList = shopCategory3EntityList.stream().map(ShopCategoryEntity::getParentId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(category2IdList)) {
                    List<ShopCategoryEntity> shopCategory2EntityList = shopCategoryMapper.selectBatchIds(category2IdList);
                    if (CollectionUtils.isNotEmpty(shopCategory2EntityList)) {
                        shopCategoryEntityList.addAll(shopCategory2EntityList);
                        // ????????????
                        List<String> category1IdList = shopCategory2EntityList.stream().map(ShopCategoryEntity::getParentId).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(category1IdList)) {
                            List<ShopCategoryEntity> shopCategory1EntityList = shopCategoryMapper.selectBatchIds(category1IdList);
                            if (CollectionUtils.isNotEmpty(shopCategory1EntityList)) {
                                shopCategoryEntityList.addAll(shopCategory1EntityList);
                            }
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(shopCategoryEntityList)) {
            returnMap = shopCategoryEntityList.stream().collect(Collectors.toMap(ShopCategoryEntity::getId, ShopCategoryEntity::getParentId, (k1, k2) -> k1));
        }
        return returnMap;
    }
}
