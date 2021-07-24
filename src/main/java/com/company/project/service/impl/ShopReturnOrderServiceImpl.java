package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.enums.ConsignStatusEnum;
import com.company.project.common.enums.OrderStatusEnum;
import com.company.project.common.enums.PayStatusEnum;
import com.company.project.common.enums.ReturnOrderTypeEnum;
import com.company.project.common.exception.BusinessException;
import com.company.project.common.exception.code.BusinessResponseCode;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.NumberConstants;
import com.company.project.entity.ShopOrderDetailEntity;
import com.company.project.entity.ShopOrderEntity;
import com.company.project.entity.ShopReturnOrderDetailEntity;
import com.company.project.mapper.ShopOrderDetailMapper;
import com.company.project.mapper.ShopOrderMapper;
import com.company.project.mapper.ShopReturnOrderDetailMapper;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopReturnOrderMapper;
import com.company.project.entity.ShopReturnOrderEntity;
import com.company.project.service.ShopReturnOrderService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@Service("shopReturnOrderService")
public class ShopReturnOrderServiceImpl extends ServiceImpl<ShopReturnOrderMapper, ShopReturnOrderEntity> implements ShopReturnOrderService {

    @Resource
    private ShopReturnOrderMapper shopReturnOrderMapper;

    @Resource
    private ShopReturnOrderDetailMapper shopReturnOrderDetailMapper;

    @Resource
    private ShopOrderMapper shopOrdeMapper;

    @Resource
    private ShopOrderDetailMapper shopOrderDetailMapper;


    @Override
    public DataResult saveShopReturnOrderEntity(ShopReturnOrderEntity shopReturnOrderEntity) {
        // 查询订单
        ShopOrderEntity shopOrderEntity = shopOrdeMapper.selectById(shopReturnOrderEntity.getOrderId());
        if (Objects.isNull(shopOrderEntity)) {
            return DataResult.fail(BusinessResponseCode.INVALID_ORDER.getMsg());
        }
        if (!PayStatusEnum.PAYMENT_SUCCESSFUL.getType().equals(shopOrderEntity.getPayStatus())) {
            return DataResult.fail(BusinessResponseCode.NOT_SUPPORTED_REFUND.getMsg());
        }
        if(shopReturnOrderEntity.getReturnMoney().compareTo(shopOrderEntity.getPayMoney()) > NumberConstants.ZERO){
            return DataResult.fail(BusinessResponseCode.CHARGEBACK_AMOUNT_OVERRUN.getMsg());
        }
        List<ShopReturnOrderDetailEntity> shopReturnOrderDetailEntityList = shopReturnOrderEntity.getShopReturnOrderDetailEntityList();
        if (CollectionUtils.isEmpty(shopReturnOrderDetailEntityList)) {
            return DataResult.fail(BusinessResponseCode.NOT_REFUND_SKU.getMsg());
        }
        // 封装退单信息
        shopReturnOrderEntity.setMemberId(shopOrderEntity.getBuyerId());
        shopReturnOrderEntity.setMemberName(shopOrderEntity.getBuyerName());
        shopReturnOrderEntity.setLinkman(shopOrderEntity.getReceiverContact());
        shopReturnOrderEntity.setLinkmanMobile(shopOrderEntity.getReceiverMobile());
        shopReturnOrderEntity.setType(ConsignStatusEnum.UNDELIVERED.getType().equals(shopOrderEntity.getConsignStatus()) ? ReturnOrderTypeEnum.REFUND.getType() : ReturnOrderTypeEnum.RETURN_REFUND.getType());
        // 保存退单申请
        shopReturnOrderMapper.insert(shopReturnOrderEntity);
        // 退单明细 - 提取订单明细ID
        List<String> orderDetailIdList = shopReturnOrderDetailEntityList.stream().map(ShopReturnOrderDetailEntity::getOrderDetailId).collect(Collectors.toList());
        List<String> finalOrderDetailIdList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(orderDetailIdList)) {
            List<ShopOrderDetailEntity> shopOrderDetailEntityList = shopOrderDetailMapper.selectBatchIds(orderDetailIdList);
            // 校验金额
            BigDecimal totalPayMoney = shopOrderDetailEntityList.stream().filter(a -> a.getIsReturn().equals(NumberConstants.ZERO_I)).map(ShopOrderDetailEntity::getPayMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
            if(shopReturnOrderEntity.getReturnMoney().compareTo(totalPayMoney) > NumberConstants.ZERO){
                throw new BusinessException(BusinessResponseCode.CHARGEBACK_AMOUNT_OVERRUN);
            }
            Map<String, ShopOrderDetailEntity> shopOrderDetailEntityMap = shopOrderDetailEntityList.stream().collect(Collectors.toMap(ShopOrderDetailEntity::getId, a -> a, (k1, k2) -> k1));
            // 封装并保存退单明细
            shopReturnOrderDetailEntityList.forEach(shopReturnOrderDetailEntity -> {
                if(shopOrderDetailEntityMap.containsKey(shopReturnOrderDetailEntity.getOrderDetailId())){
                    ShopOrderDetailEntity shopOrderDetailEntity = shopOrderDetailEntityMap.get(shopReturnOrderDetailEntity.getOrderDetailId());
                    if(NumberConstants.ZERO_I.equals(shopOrderDetailEntity.getIsReturn())){
                        shopReturnOrderDetailEntity.setCategoryId(shopOrderDetailEntity.getCategoryId3());
                        shopReturnOrderDetailEntity.setSpuId(shopOrderDetailEntity.getSpuId());
                        shopReturnOrderDetailEntity.setSkuId(shopOrderDetailEntity.getSkuId());
                        shopReturnOrderDetailEntity.setName(shopOrderDetailEntity.getName());
                        shopReturnOrderDetailEntity.setOrderId(shopOrderDetailEntity.getOrderId());
                        shopReturnOrderDetailEntity.setReturnOrderId(shopReturnOrderEntity.getId());
                        shopReturnOrderDetailEntity.setPrice(shopOrderDetailEntity.getPrice());
                        shopReturnOrderDetailEntity.setNum(shopOrderDetailEntity.getNum());
                        shopReturnOrderDetailEntity.setMoney(shopOrderDetailEntity.getMoney());
                        shopReturnOrderDetailEntity.setPayMoney(shopOrderDetailEntity.getPayMoney());
                        shopReturnOrderDetailEntity.setImage(shopOrderDetailEntity.getImage());
                        shopReturnOrderDetailEntity.setWeight(shopOrderDetailEntity.getWeight());
                        shopReturnOrderDetailEntity.setPostFee(shopOrderDetailEntity.getPostFee());
                        shopReturnOrderDetailMapper.insert(shopReturnOrderDetailEntity);
                        finalOrderDetailIdList.add(shopOrderDetailEntity.getId());
                    }
                }
            });
        }
        // 修改订单状态
        ShopOrderEntity updShopOrderEntity = new ShopOrderEntity();
        updShopOrderEntity.setId(shopOrderEntity.getId());
        updShopOrderEntity.setOrderStatus(ConsignStatusEnum.UNDELIVERED.getType().equals(shopOrderEntity.getConsignStatus()) ? OrderStatusEnum.REFUND.getType() : OrderStatusEnum.REFUND_AND_RETURN.getType());
        shopOrdeMapper.updateById(updShopOrderEntity);
        // 修改订单明细状态
        if(CollectionUtils.isNotEmpty(finalOrderDetailIdList)){
            ShopOrderDetailEntity updShopOrderDetailEntity = new ShopOrderDetailEntity();
            updShopOrderDetailEntity.setIsReturn(NumberConstants.ONE_I);
            shopOrderDetailMapper.update(updShopOrderDetailEntity, Wrappers.<ShopOrderDetailEntity>lambdaQuery().in(ShopOrderDetailEntity::getId, finalOrderDetailIdList));
        }
        return DataResult.success();
    }

    @Override
    public DataResult auditSuccess(ShopReturnOrderEntity shopReturnOrderEntity) {






        return DataResult.success();
    }

    @Override
    public DataResult auditFailed(ShopReturnOrderEntity shopReturnOrderEntity) {







        return DataResult.success();
    }
}
