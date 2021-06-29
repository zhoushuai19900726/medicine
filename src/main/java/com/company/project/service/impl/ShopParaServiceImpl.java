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

import com.company.project.mapper.ShopParaMapper;
import com.company.project.entity.ShopParaEntity;
import com.company.project.service.ShopParaService;

import javax.annotation.Resource;
import java.util.List;


@Service("shopParaService")
public class ShopParaServiceImpl extends ServiceImpl<ShopParaMapper, ShopParaEntity> implements ShopParaService {

    @Resource
    private ShopParaMapper shopParaMapper;

    @Resource
    private ShopTemplateMapper shopTemplateMapper;

    @Override
    public DataResult saveShopParaEntity(ShopParaEntity shopParaEntity) {
        // 参数名称验重
        List<ShopParaEntity> shopSpecEntityList = shopParaMapper.selectList(Wrappers.<ShopParaEntity>lambdaQuery().eq(ShopParaEntity::getTemplateId, shopParaEntity.getTemplateId()).eq(ShopParaEntity::getName, shopParaEntity.getName()));
        if(CollectionUtils.isNotEmpty(shopSpecEntityList)){
            return DataResult.fail(BusinessResponseCode.PARA_REPEAT.getMsg());
        }
        // 保存参数
        shopParaMapper.insert(shopParaEntity);
        // 更新模板中参数数量
        updateParametersQuantityInTemplate(shopParaEntity.getTemplateId());

        return DataResult.success(shopParaEntity);
    }

    @Override
    public DataResult removeShopParaEntityByIds(List<String> ids) {
        // 查询参数
        ShopParaEntity shopParaEntity = shopParaMapper.selectById(ids.get(NumberConstants.ZERO));
        // 删除规格
        shopParaMapper.deleteBatchIds(ids);
        // 更新模板中参数数量
        updateParametersQuantityInTemplate(shopParaEntity.getTemplateId());
        return DataResult.success();
    }

    @Override
    public DataResult updateShopParaEntityById(ShopParaEntity shopParaEntity) {
        // 参数名称验重
        List<ShopParaEntity> shopSpecEntityList = shopParaMapper.selectList(Wrappers.<ShopParaEntity>lambdaQuery().eq(ShopParaEntity::getTemplateId, shopParaEntity.getTemplateId()).eq(ShopParaEntity::getName, shopParaEntity.getName()).ne(ShopParaEntity::getId, shopParaEntity.getId()));
        if(CollectionUtils.isNotEmpty(shopSpecEntityList)){
            return DataResult.fail(BusinessResponseCode.PARA_REPEAT.getMsg());
        }
        // 保存更新
        return DataResult.success(shopParaMapper.updateById(shopParaEntity));
    }

    private void updateParametersQuantityInTemplate(String templateId) {
        // 查询当前模板下规格数量
        Integer paraCount = shopParaMapper.selectCount(Wrappers.<ShopParaEntity>lambdaQuery().eq(ShopParaEntity::getTemplateId, templateId));
        // 更新到模板主表中
        shopTemplateMapper.updateById(new ShopTemplateEntity().setId(templateId).setParaNum(paraCount));
    }
}
