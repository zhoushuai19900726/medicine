package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.entity.ShopArticleColumnEntity;
import com.company.project.mapper.ShopArticleColumnMapper;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopArticleMapper;
import com.company.project.entity.ShopArticleEntity;
import com.company.project.service.ShopArticleService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("shopArticleService")
public class ShopArticleServiceImpl extends ServiceImpl<ShopArticleMapper, ShopArticleEntity> implements ShopArticleService {

    @Resource
    private ShopArticleMapper shopArticleMapper;

    @Resource
    private ShopArticleColumnMapper shopArticleColumnMapper;

    @Override
    public IPage<ShopArticleEntity> listByPage(Page<ShopArticleEntity> page, LambdaQueryWrapper<ShopArticleEntity> wrapper) {
        return encapsulatingFieldName(shopArticleMapper.selectPage(page, wrapper));
    }

    /**
     * 封装字段name
     *
     * @param iPage
     * @return
     */
    private IPage<ShopArticleEntity> encapsulatingFieldName(IPage<ShopArticleEntity> iPage) {
        // 文章集合
        List<ShopArticleEntity> shopArticleEntityList = iPage.getRecords();
        // 封装名称
        if (CollectionUtils.isNotEmpty(shopArticleEntityList)) {
            // 查询结果封装
            Map<String, String> shopArticleColumnEntityMap = Maps.newHashMap();
            // 提取栏目ID集合
            List<String> columnIdList = shopArticleEntityList.stream().map(ShopArticleEntity::getColumnId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(columnIdList)) {
                // 查询栏目集合
                List<ShopArticleColumnEntity> shopArticleColumnEntityList = shopArticleColumnMapper.selectBatchIds(columnIdList);
                if (CollectionUtils.isNotEmpty(shopArticleColumnEntityList)) {
                    // 封装栏目名称
                    shopArticleColumnEntityMap.putAll(shopArticleColumnEntityList.stream().collect(Collectors.toMap(ShopArticleColumnEntity::getId, ShopArticleColumnEntity::getName, (k1, k2) -> k1)));
                }
            }
            // 执行封装名称
            shopArticleEntityList.forEach(shopArticleEntity -> shopArticleEntity.setColumnName(shopArticleColumnEntityMap.getOrDefault(shopArticleEntity.getColumnId(), DelimiterConstants.EMPTY_STR)));
        }
        return iPage;
    }


}
