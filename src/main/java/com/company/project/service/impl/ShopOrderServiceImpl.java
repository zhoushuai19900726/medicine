package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.enums.*;
import com.company.project.common.exception.code.BusinessResponseCode;
import com.company.project.common.utils.*;
import com.company.project.entity.*;
import com.company.project.mapper.*;
import com.company.project.service.ShopMemberGrowthValueRecordService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.service.ShopOrderService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Transactional
@Service("shopOrderService")
public class ShopOrderServiceImpl extends ServiceImpl<ShopOrderMapper, ShopOrderEntity> implements ShopOrderService {

    @Resource
    private ShopOrderMapper shopOrderMapper;

    @Resource
    private ShopOrderDetailMapper shopOrderDetailMapper;

    @Resource
    private ShopMemberMapper shopMemberMapper;

    @Resource
    private ShopSellerMapper shopSellerMapper;

    @Resource
    private TaskRewardSettingsMapper taskRewardSettingsMapper;

    @Resource
    private ShopMemberGrowthValueRecordService shopMemberGrowthValueRecordService;

    @Override
    public DataResult updateShopOrderEntityById(ShopOrderEntity shopOrder) {
        ShopOrderEntity result = shopOrderMapper.selectById(shopOrder.getId());
        if (Objects.isNull(result)) {
            return DataResult.fail(BusinessResponseCode.INVALID_ORDER.getMsg());
        }
        // 订单状态
        if (Objects.nonNull(shopOrder.getOrderStatus()) && OrderStatusEnum.CLOSED.getType().equals(shopOrder.getOrderStatus())) {
            shopOrder.setCloseTime(shopOrder.getOrderStatus().equals(result.getOrderStatus()) ? result.getCloseTime() : new Date());
        }
        // 支付状态
        if (Objects.nonNull(shopOrder.getPayStatus()) && !shopOrder.getPayStatus().equals(result.getPayStatus()) && PayStatusEnum.PAYMENT_SUCCESSFUL.getType().equals(shopOrder.getPayStatus())) {
            shopOrder.setPayTime(new Date());
        }
        // 发货状态
        if (Objects.nonNull(shopOrder.getConsignStatus()) && !shopOrder.getConsignStatus().equals(result.getConsignStatus())) {
            // 发货
            if (ConsignStatusEnum.DELIVERED.getType().equals(shopOrder.getConsignStatus())) {
                shopOrder.setConsignTime(Objects.nonNull(shopOrder.getConsignTime()) ? shopOrder.getConsignTime() : new Date());
            }
            // 收货
            if (ConsignStatusEnum.RECEIVED.getType().equals(shopOrder.getConsignStatus())) {
                shopOrder.setEndTime(new Date());
            }
        }
        // 订单明细状态
        List<Integer> orderStatusListA = Lists.newArrayList(OrderStatusEnum.REFUND.getType(), OrderStatusEnum.REFUND_AND_RETURN.getType());
        List<Integer> orderStatusListB = Lists.newArrayList(OrderStatusEnum.NOT_FINISHED.getType());
        if (orderStatusListA.contains(result.getOrderStatus()) && orderStatusListB.contains(shopOrder.getOrderStatus())) {
            ShopOrderDetailEntity shopOrderDetailEntity = new ShopOrderDetailEntity();
            shopOrderDetailEntity.setIsReturn(NumberConstants.ZERO_I);
            shopOrderDetailMapper.update(shopOrderDetailEntity, Wrappers.<ShopOrderDetailEntity>lambdaQuery().eq(ShopOrderDetailEntity::getOrderId, result.getId()));
        }
        return DataResult.success(shopOrderMapper.updateById(shopOrder));
    }

    @Override
    public DataResult uploadFreeBill(MultipartFile file) {
        try {
            // 文件
            if (Objects.isNull(file) || StringUtils.isBlank(file.getOriginalFilename()) || StringUtils.equalsIgnoreCase(DelimiterConstants.EMPTY_STR, file.getOriginalFilename().trim())) {
                return DataResult.fail(BusinessResponseCode.FILE_EMPTY.getMsg());
            }
            //获取文件名
            String name = file.getOriginalFilename();
            // 进一步判断文件是否为空（即判断其大小是否为0或其名称是否为null）验证文件名是否合格
            if (file.getSize() == NumberConstants.ZERO || !CommonUtils.validateExcel(name)) {
                return DataResult.fail(BusinessResponseCode.FILE_FORMAT_ERROR.getMsg());
            }
            // 1.获取Excel标题-->校对表头
            List<String> excelTitle = ExcelExportUtil.readExcelTitle(file.getInputStream(), name);
            List<String> systemTitle = TitleAndCodeEnum.IMPORT_FREE_BILL.getTitle();
            if (Objects.isNull(excelTitle) || excelTitle.size() != systemTitle.size()) {
                return DataResult.fail(BusinessResponseCode.FILE_TITLE_ERROR.getMsg());
            }
            // 标题校验
            StringBuilder sb = new StringBuilder();
            AtomicInteger i = new AtomicInteger();
            systemTitle.forEach(title -> {
                if (!StringUtils.equals(title, ExcelExportUtil.getTitle(excelTitle.get(i.get())))) {
                    sb.append(BusinessResponseCode.FILE_TITLE_CONTENT_ERROR_FRONT.getMsg()).append(i.get() + NumberConstants.ONE).append(BusinessResponseCode.FILE_TITLE_CONTENT_ERROR_AFTER.getMsg()).append(title).append("\n");
                }
                i.getAndIncrement();
            });
            if (StringUtils.isNotBlank(sb.toString())) {
                return DataResult.fail(sb.toString());
            }
            // 2.读取Excel数据信息
            List<String> systemCode = TitleAndCodeEnum.IMPORT_FREE_BILL.getCode();
            List<LinkedHashMap<String, Object>> data = ExcelExportUtil.readExcelContent(file.getInputStream(), name, systemCode);
            if (CollectionUtils.isEmpty(data)) {
                return DataResult.fail(BusinessResponseCode.FILE_DATA_EMPTY.getMsg());
            }
            // memberName集合
            List<String> memberNameList = data.stream().map(a -> String.valueOf(a.get(TitleAndCodeEnum.IMPORT_FREE_BILL.getCode().get(NumberConstants.ZERO)))).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(memberNameList)) {
                return DataResult.fail(BusinessResponseCode.FILE_DATA_EMPTY.getMsg());
            }
            // 查询账号
            List<ShopMemberEntity> shopMemberEntityList = shopMemberMapper.findListByMemberNameList(memberNameList);
            if (CollectionUtils.isEmpty(shopMemberEntityList)) {
                return DataResult.fail(BusinessResponseCode.FILE_DATA_EMPTY.getMsg());
            }
            Map<String, ShopMemberEntity> shopMemberEntityMap = shopMemberEntityList.stream().collect(Collectors.toMap(ShopMemberEntity::getMemberName, a -> a, (k1, k2) -> k1));
            // 查询店铺
            ShopSellerEntity shopSellerEntity = shopSellerMapper.selectById(NumberConstants.ONE_STR);
            // 成长值配置
            TaskRewardSettingsEntity taskRewardSettingsEntity = taskRewardSettingsMapper.selectOne(Wrappers.lambdaQuery());
            // 保存赠单
            data.forEach(map -> {
                String memberName = String.valueOf(map.get(TitleAndCodeEnum.IMPORT_FREE_BILL.getCode().get(NumberConstants.ZERO)));
                ShopMemberEntity shopMemberEntity = shopMemberEntityMap.get(memberName);
                if (Objects.nonNull(shopMemberEntity)) {
                    // 保存赠单
                    ShopOrderEntity shopOrderEntity = new ShopOrderEntity();
                    saveFreeBill(shopOrderEntity, shopSellerEntity, map, shopMemberEntity);
                    // 保存成长值记录 - 会员升级
                    upgrade(taskRewardSettingsEntity, shopOrderEntity);
                }
            });
            return DataResult.success(data);
        } catch (Exception e) {
            return DataResult.fail(e.getMessage());
        }
    }

    private void upgrade(TaskRewardSettingsEntity taskRewardSettingsEntity, ShopOrderEntity shopOrderEntity) {
        if (Objects.isNull(taskRewardSettingsEntity)) {
            taskRewardSettingsEntity = new TaskRewardSettingsEntity();
            taskRewardSettingsEntity.setShoppingConsumption(NumberConstants.ONE_STR);
            taskRewardSettingsEntity.setActualPayment(NumberConstants.ZERO_STR);
        }
        if (shopOrderEntity.getPayMoney().compareTo(new BigDecimal(taskRewardSettingsEntity.getActualPayment())) >= NumberConstants.ZERO) {
            ShopMemberGrowthValueRecordEntity shopMemberGrowthValueRecord = new ShopMemberGrowthValueRecordEntity();
            shopMemberGrowthValueRecord.setMemberId(shopOrderEntity.getBuyerId());
            shopMemberGrowthValueRecord.setGrowthValue(shopOrderEntity.getPayMoney().divide(new BigDecimal(taskRewardSettingsEntity.getShoppingConsumption()), NumberConstants.TWO, BigDecimal.ROUND_HALF_UP));
            shopMemberGrowthValueRecord.setType(GrowthValueDetailsTypeEnum.CONSUMPTION.getType());
            shopMemberGrowthValueRecordService.saveShopMemberGrowthValueRecordEntity(shopMemberGrowthValueRecord);
        }
    }

    private void saveFreeBill(ShopOrderEntity shopOrderEntity, ShopSellerEntity shopSellerEntity, LinkedHashMap<String, Object> map, ShopMemberEntity shopMemberEntity) {
        String UUID = CommonUtils.generateUUID();
        shopOrderEntity.setId(UUID);
        shopOrderEntity.setTransactionId(UUID);
        shopOrderEntity.setSerialNumber(UUID);
        if (Objects.nonNull(shopSellerEntity)) {
            shopOrderEntity.setSellerId(shopSellerEntity.getId());
            shopOrderEntity.setSellerName(shopSellerEntity.getSellerName());
        }
        shopOrderEntity.setTotalNum(NumberConstants.ONE_I);
        String amount = String.valueOf(map.get(TitleAndCodeEnum.IMPORT_FREE_BILL.getCode().get(NumberConstants.ONE)));
        shopOrderEntity.setTotalMoney(new BigDecimal(amount).setScale(NumberConstants.TWO, BigDecimal.ROUND_HALF_UP));
        shopOrderEntity.setPreMoney(BigDecimal.ZERO);
        shopOrderEntity.setPostFee(BigDecimal.ZERO);
        shopOrderEntity.setPayMoney(shopOrderEntity.getTotalMoney());
        shopOrderEntity.setPayType(PayTypeEnum.SYSTEM_GIFT.getType());
        Date date = new Date();
        shopOrderEntity.setPayTime(date);
        shopOrderEntity.setEndTime(date);
        shopOrderEntity.setCloseTime(date);
        shopOrderEntity.setBuyerId(shopMemberEntity.getMemberId());
        shopOrderEntity.setBuyerName(shopMemberEntity.getMemberName());
        shopOrderEntity.setSourceType(SourceTypeEnum.FREE_BILL.getType());
        shopOrderEntity.setOrderStatus(OrderStatusEnum.CLOSED.getType());
        shopOrderEntity.setPayStatus(PayStatusEnum.PAYMENT_SUCCESSFUL.getType());
        shopOrderEntity.setConsignStatus(ConsignStatusEnum.UNDELIVERED.getType());
        shopOrderMapper.insert(shopOrderEntity);
    }

}
