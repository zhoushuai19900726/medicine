package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopMemberGradeEntity;
import com.company.project.entity.ShopMemberGrowthValueEntity;

/**
 * 会员等级
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-07 15:30:45
 */
public interface ShopMemberGradeService extends IService<ShopMemberGradeEntity> {

    DataResult saveShopMemberGradeEntity(ShopMemberGradeEntity shopMemberGrade);

    DataResult updateShopMemberGradeEntityById(ShopMemberGradeEntity shopMemberGrade);

    ShopMemberGradeEntity calculationMemberGradeByGrowthValue(ShopMemberGrowthValueEntity shopMemberGrowthValueEntity);

}

