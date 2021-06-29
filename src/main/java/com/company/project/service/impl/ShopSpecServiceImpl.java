package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.exception.code.BusinessResponseCode;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.NumberConstants;
import com.company.project.entity.ShopTemplateEntity;
import com.company.project.mapper.ShopTemplateMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopSpecMapper;
import com.company.project.entity.ShopSpecEntity;
import com.company.project.service.ShopSpecService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service("shopSpecService")
public class ShopSpecServiceImpl extends ServiceImpl<ShopSpecMapper, ShopSpecEntity> implements ShopSpecService {

    @Resource
    private ShopSpecMapper shopSpecMapper;

    @Resource
    private ShopTemplateMapper shopTemplateMapper;

    @Override
    public DataResult saveShopSpecEntity(ShopSpecEntity shopSpecEntity) {
        // 规格名称验重
        List<ShopSpecEntity> shopSpecEntityList = shopSpecMapper.selectList(Wrappers.<ShopSpecEntity>lambdaQuery().eq(ShopSpecEntity::getTemplateId, shopSpecEntity.getTemplateId()).eq(ShopSpecEntity::getName, shopSpecEntity.getName()));
        if(CollectionUtils.isNotEmpty(shopSpecEntityList)){
            return DataResult.fail(BusinessResponseCode.SPEC_REPEAT.getMsg());
        }
        // 保存规格
        shopSpecMapper.insert(shopSpecEntity);
        // 更新模板中规格数量
        updateSpecificationQuantityInTemplate(shopSpecEntity.getTemplateId());
        return DataResult.success(shopSpecEntity);
    }

    @Override
    public DataResult removeShopSpecEntityByIds(List<String> ids) {
        // 查询规格
        ShopSpecEntity shopSpecEntity = shopSpecMapper.selectById(ids.get(NumberConstants.ZERO));
        // 删除规格
        shopSpecMapper.deleteBatchIds(ids);
        // 更新模板中规格数量
        updateSpecificationQuantityInTemplate(shopSpecEntity.getTemplateId());
        return DataResult.success();
    }

    @Override
    public DataResult updateShopSpecEntityById(ShopSpecEntity shopSpecEntity) {
        // 规格名称验重
        List<ShopSpecEntity> shopSpecEntityList = shopSpecMapper.selectList(Wrappers.<ShopSpecEntity>lambdaQuery().eq(ShopSpecEntity::getTemplateId, shopSpecEntity.getTemplateId()).eq(ShopSpecEntity::getName, shopSpecEntity.getName()).ne(ShopSpecEntity::getId, shopSpecEntity.getId()));
        if(CollectionUtils.isNotEmpty(shopSpecEntityList)){
            return DataResult.fail(BusinessResponseCode.SPEC_REPEAT.getMsg());
        }
        // 保存更新
        return DataResult.success(shopSpecMapper.updateById(shopSpecEntity));
    }

    private void updateSpecificationQuantityInTemplate(String templateId){
        // 查询当前模板下规格数量
        Integer specCount = shopSpecMapper.selectCount(Wrappers.<ShopSpecEntity>lambdaQuery().eq(ShopSpecEntity::getTemplateId, templateId));
        // 更新到模板主表中
        shopTemplateMapper.updateById(new ShopTemplateEntity().setId(templateId).setSpecNum(specCount));
    }

}
