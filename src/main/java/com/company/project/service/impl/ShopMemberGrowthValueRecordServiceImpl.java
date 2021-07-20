package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.exception.code.BusinessResponseCode;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.common.utils.DictionariesKeyConstant;
import com.company.project.common.utils.NumberConstants;
import com.company.project.entity.ShopMemberEntity;
import com.company.project.entity.ShopMemberGradeEntity;
import com.company.project.entity.ShopMemberGrowthValueEntity;
import com.company.project.mapper.ShopMemberGrowthValueMapper;
import com.company.project.mapper.ShopMemberMapper;
import com.company.project.service.ShopMemberGradeService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopMemberGrowthValueRecordMapper;
import com.company.project.entity.ShopMemberGrowthValueRecordEntity;
import com.company.project.service.ShopMemberGrowthValueRecordService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Transactional
@Service("shopMemberGrowthValueRecordService")
public class ShopMemberGrowthValueRecordServiceImpl extends ServiceImpl<ShopMemberGrowthValueRecordMapper, ShopMemberGrowthValueRecordEntity> implements ShopMemberGrowthValueRecordService {

    @Resource
    private ShopMemberGrowthValueMapper shopMemberGrowthValueMapper;

    @Resource
    private ShopMemberGrowthValueRecordMapper shopMemberGrowthValueRecordMapper;

    @Resource
    private ShopMemberMapper shopMemberMapper;

    @Resource
    private ShopMemberGradeService shopMemberGradeService;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public IPage<ShopMemberGrowthValueRecordEntity> listByPage(Page<ShopMemberGrowthValueRecordEntity> page, LambdaQueryWrapper<ShopMemberGrowthValueRecordEntity> wrapper) {
        return encapsulatingFieldName(shopMemberGrowthValueRecordMapper.selectPage(page, wrapper));
    }

    @Override
    public DataResult saveShopMemberGrowthValueRecordEntity(ShopMemberGrowthValueRecordEntity shopMemberGrowthValueRecord) {
        // 升级校验
        computingUpgrade(shopMemberGrowthValueRecord);
        return DataResult.success(shopMemberGrowthValueRecordMapper.insert(shopMemberGrowthValueRecord));
    }

    @Override
    public void computingUpgrade(ShopMemberGrowthValueRecordEntity shopMemberGrowthValueRecord) {
        // 查询会员
        ShopMemberEntity shopMemberEntity = shopMemberMapper.findOneByMemberId(shopMemberGrowthValueRecord.getMemberId());
        if (Objects.nonNull(shopMemberEntity)) {
            // 成长值
            ShopMemberGrowthValueEntity shopMemberGrowthValueEntity = shopMemberGrowthValueMapper.selectOne(Wrappers.<ShopMemberGrowthValueEntity>lambdaQuery().eq(ShopMemberGrowthValueEntity::getMemberId, shopMemberEntity.getMemberId()));
            if (Objects.isNull(shopMemberGrowthValueEntity)) {
                shopMemberGrowthValueEntity = new ShopMemberGrowthValueEntity();
                shopMemberGrowthValueEntity.setMemberId(shopMemberGrowthValueRecord.getMemberId());
                shopMemberGrowthValueEntity.setGrowthValue(BigDecimal.ZERO);
                shopMemberGrowthValueMapper.insert(shopMemberGrowthValueEntity);
            }
            // 计算余额
            BigDecimal balance = shopMemberGrowthValueEntity.getGrowthValue().add(shopMemberGrowthValueRecord.getGrowthValue()).setScale(NumberConstants.TWO, BigDecimal.ROUND_HALF_UP);
            shopMemberGrowthValueEntity.setGrowthValue(balance);
            shopMemberGrowthValueMapper.updateById(shopMemberGrowthValueEntity);
            shopMemberGrowthValueRecord.setBalance(balance);
            // VIP升级
            ShopMemberGradeEntity shopMemberGradeEntity = shopMemberGradeService.calculationMemberGradeByGrowthValue(shopMemberGrowthValueEntity);
            if (!StringUtils.equals(shopMemberGradeEntity.getId(), shopMemberEntity.getMemberGradeId())) {
                ShopMemberEntity updShopMemberEntity = new ShopMemberEntity();
                updShopMemberEntity.setMemberId(shopMemberEntity.getMemberId());
                updShopMemberEntity.setMemberGradeId(shopMemberGradeEntity.getId());
                updShopMemberEntity.setMemberGradeName(shopMemberGradeEntity.getName());
                updShopMemberEntity.setGradeTime(new Date());
                shopMemberMapper.updateById(updShopMemberEntity);
            }

        }
    }

    private IPage<ShopMemberGrowthValueRecordEntity> encapsulatingFieldName(IPage<ShopMemberGrowthValueRecordEntity> iPage) {
        // 会员集合
        List<ShopMemberGrowthValueRecordEntity> shopMemberGrowthValueRecordEntityList = iPage.getRecords();
        // 封装名称
        if (CollectionUtils.isNotEmpty(shopMemberGrowthValueRecordEntityList)) {
            // 执行封装
            shopMemberGrowthValueRecordEntityList.forEach(shopMemberGrowthValueRecordEntity -> shopMemberGrowthValueRecordEntity.setTypeName(getTypeName(shopMemberGrowthValueRecordEntity.getType())));
        }
        return iPage;
    }

    private String getTypeName(Integer type) {
        AtomicReference<String> typeName = new AtomicReference<>(DelimiterConstants.EMPTY_STR);
        redisTemplate.boundHashOps(DictionariesKeyConstant.GROWTH_VALUE_DETAILS_TYPE).values().forEach(obj -> {
            try {
                Map<String, String> map = BeanUtils.describe(obj);
                if (StringUtils.equals(map.get("value"), String.valueOf(type))) {
                    typeName.set(map.get("label"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return typeName.get();
    }
}
