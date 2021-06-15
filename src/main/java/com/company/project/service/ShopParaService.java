package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.ShopParaEntity;

/**
 * 商品参数
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-15 11:26:57
 */
public interface ShopParaService extends IService<ShopParaEntity> {

    /**
     * 更新模板中参数数量
     * @param templateId
     */
    void updateParametersQuantityInTemplate(String templateId);
}

