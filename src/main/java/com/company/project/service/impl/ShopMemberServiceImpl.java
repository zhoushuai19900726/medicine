package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.enums.TitleAndCodeEnum;
import com.company.project.common.exception.code.BaseResponseCode;
import com.company.project.common.exception.code.BusinessResponseCode;
import com.company.project.common.utils.*;
import com.company.project.entity.*;
import com.company.project.mapper.*;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.service.ShopMemberService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Transactional
@Service("shopMemberService")
public class ShopMemberServiceImpl extends ServiceImpl<ShopMemberMapper, ShopMemberEntity> implements ShopMemberService {

    @Resource
    private ShopMemberMapper shopMemberMapper;

    @Resource
    private ShopRecommendationRelationshipMapper shopRecommendationRelationshipMapper;

    @Resource
    private ShopMemberGradeMapper shopMemberGradeMapper;

    @Resource
    private ShopMemberGrowthValueMapper shopMemberGrowthValueMapper;

    @Resource
    private ShopMemberGrowthValueRecordMapper shopMemberGrowthValueRecordMapper;

    @Resource
    private ShopMemberWalletMapper shopMemberWalletMapper;

    @Resource
    private ShopMemberWalletRecordMapper shopMemberWalletRecordMapper;

    @Override
    public ShopMemberEntity findOneByMemberId(String memberId) {
        return shopMemberMapper.findOneByMemberId(memberId);
    }

    @Override
    public ShopMemberEntity findOneByMemberName(ShopMemberEntity shopMemberEntity) {
        return shopMemberMapper.findOneByMemberName(shopMemberEntity);
    }

    @Override
    public ShopMemberEntity findOneByInvitationCode(ShopMemberEntity shopMemberEntity) {
        return shopMemberMapper.findOneByInvitationCode(shopMemberEntity);
    }

    @Override
    public IPage<ShopMemberEntity> listByPage(Page<ShopMemberEntity> page, LambdaQueryWrapper<ShopMemberEntity> wrapper) {
        wrapper.ne(ShopMemberEntity::getMemberId, DelimiterConstants.SYS_ADMIN_ID);
        return encapsulatingFieldName(shopMemberMapper.selectPage(page, wrapper));
    }

    @Override
    public IPage<ShopMemberEntity> logoutListByPage(Page<ShopMemberEntity> page, LambdaQueryWrapper<ShopMemberEntity> wrapper) {
        wrapper.eq(ShopMemberEntity::getDeleted, NumberConstants.ONE_I);
        return encapsulatingFieldName(shopMemberMapper.selectLogoutPage(page, wrapper));
    }

    @Override
    public DataResult revokeShopMemberEntityById(ShopMemberEntity shopMember) {
        return DataResult.success(shopMemberMapper.revokeShopMemberEntityById(shopMember.getMemberId()));
    }

    @Override
    public DataResult saveShopMemberEntity(ShopMemberEntity shopMemberEntity) {
        // ????????????
        ShopMemberEntity accountVerification = shopMemberMapper.findOneByMemberName(shopMemberEntity);
        if (Objects.nonNull(accountVerification)) {
            return DataResult.fail(BusinessResponseCode.ACCOUNT_REPEAT.getMsg());
        }
        // ??????????????????
        ShopMemberGradeEntity shopMemberGradeEntity = shopMemberGradeMapper.selectOne(Wrappers.<ShopMemberGradeEntity>lambdaQuery().eq(ShopMemberGradeEntity::getIsDefault, NumberConstants.ONE_I));
        if (Objects.nonNull(shopMemberGradeEntity)) {
            shopMemberEntity.setMemberGradeId(shopMemberGradeEntity.getId());
            shopMemberEntity.setMemberGradeName(shopMemberGradeEntity.getName());
            shopMemberEntity.setGradeTime(new Date());
        }
        // ??????????????????8?????????????????????(????????????????????????????????????????????????)
        do {
            shopMemberEntity.setMemberInvitationCode(CommonUtils.generateShortUUID());
        } while (Objects.nonNull(shopMemberMapper.findOneByInvitationCode(shopMemberEntity)));
        // ??????????????????
        if (StringUtils.isNotBlank(shopMemberEntity.getMemberPasswd())) {
            shopMemberEntity.setMemberPasswd(PasswordUtils.encode(shopMemberEntity.getMemberPasswd(), PasswordUtils.getSalt()));
        } else {
            shopMemberEntity.setMemberPasswd(PasswordUtils.encode(DelimiterConstants.INIT_PASSWORD, PasswordUtils.getSalt()));
        }
        // ??????????????????
        if (StringUtils.isNotBlank(shopMemberEntity.getPaymentPasswd())) {
            shopMemberEntity.setPaymentPasswd(PasswordUtils.encode(shopMemberEntity.getPaymentPasswd(), PasswordUtils.getSalt()));
        } else {
            shopMemberEntity.setPaymentPasswd(PasswordUtils.encode(DelimiterConstants.INIT_PASSWORD, PasswordUtils.getSalt()));
        }
        // ????????????
        if (StringUtils.isBlank(shopMemberEntity.getMemberAvatar())) {
            shopMemberEntity.setMemberAvatar(DelimiterConstants.DEFAULT_AVATAR);
        }
        // ????????????
        if (StringUtils.isBlank(shopMemberEntity.getMemberInvitationCodeFrom())) {
            shopMemberEntity.setMemberFrom(DelimiterConstants.SYS_ADMIN_ID);
            // ????????????????????????
            if (Objects.isNull(shopMemberMapper.selectById(DelimiterConstants.SYS_ADMIN_ID))) {
                shopMemberMapper.insert(DelimiterConstants.INIT_MEMBER());
            }
        }
        ShopMemberEntity references = shopMemberMapper.selectById(shopMemberEntity.getMemberFrom());
        if (Objects.nonNull(references)) {
            shopMemberEntity.setMemberFrom(references.getMemberId());
            shopMemberEntity.setMemberInvitationCodeFrom(references.getMemberInvitationCode());
        } else {
            return DataResult.fail(BusinessResponseCode.INVALID_RECOMMENDATION_CODE.getMsg());
        }
        // ??????????????????
        shopMemberEntity.setMemberVersion(NumberConstants.ONE_L);
        // ??????????????????
        shopMemberMapper.insert(shopMemberEntity);
        // ??????????????????
        shopRecommendationRelationshipMapper.insert(recommendationRelationship(shopMemberEntity, references));
        // ??????????????????
        shopMemberWalletMapper.insert(new ShopMemberWalletEntity(shopMemberEntity.getMemberId()));
        // ???????????????
        shopMemberGrowthValueMapper.insert(new ShopMemberGrowthValueEntity(shopMemberEntity.getMemberId()));
        return DataResult.success(shopMemberEntity);
    }

    private ShopRecommendationRelationshipEntity recommendationRelationship(ShopMemberEntity shopMemberEntity, ShopMemberEntity references) {
        // ????????????
        ShopRecommendationRelationshipEntity shopRecommendationRelationshipEntity = new ShopRecommendationRelationshipEntity();
        shopRecommendationRelationshipEntity.setMemberId(shopMemberEntity.getMemberId());
        shopRecommendationRelationshipEntity.setRecommendId(references.getMemberId());
        // ??????????????????
        ShopRecommendationRelationshipEntity referencesRelationship = shopRecommendationRelationshipMapper.selectOne(Wrappers.<ShopRecommendationRelationshipEntity>lambdaQuery().eq(ShopRecommendationRelationshipEntity::getMemberId, references.getMemberId()));
        if (Objects.nonNull(referencesRelationship)) {
            shopRecommendationRelationshipEntity.setRecommendLevel(referencesRelationship.getRecommendLevel() + NumberConstants.ONE_I);
        } else {
            referencesRelationship = new ShopRecommendationRelationshipEntity();
            referencesRelationship.setMemberId(references.getMemberId());
            referencesRelationship.setRecommendLevel(NumberConstants.ONE_I);
            shopRecommendationRelationshipMapper.insert(referencesRelationship);
            shopRecommendationRelationshipEntity.setRecommendLevel(NumberConstants.TWO_I);
        }
        return shopRecommendationRelationshipEntity;
    }

    @Override
    public DataResult updateShopMemberEntityById(ShopMemberEntity shopMemberEntity) {
        // ??????????????????
        ShopMemberEntity queryResult = shopMemberMapper.selectById(shopMemberEntity.getMemberId());
        if (Objects.nonNull(queryResult)) {
            // ????????????
            if (StringUtils.isNotBlank(shopMemberEntity.getMemberName())) {
                ShopMemberEntity accountVerification = shopMemberMapper.findOneByMemberName(shopMemberEntity);
                if (Objects.nonNull(accountVerification)) {
                    return DataResult.fail(BusinessResponseCode.ACCOUNT_REPEAT.getMsg());
                }
            }
            // ?????????
            if (StringUtils.isNotBlank(shopMemberEntity.getMemberInvitationCodeFrom())) {
                // ???????????????
                ShopMemberEntity references = shopMemberMapper.findOneByInvitationCode(new ShopMemberEntity(shopMemberEntity.getMemberId(), shopMemberEntity.getMemberInvitationCodeFrom()));
                if (Objects.nonNull(references)) {
                    shopMemberEntity.setMemberFrom(references.getMemberId());
                    // ??????????????????
                    shopRecommendationRelationshipMapper.update(recommendationRelationship(shopMemberEntity, references), Wrappers.<ShopRecommendationRelationshipEntity>lambdaQuery().eq(ShopRecommendationRelationshipEntity::getMemberId, shopMemberEntity.getMemberId()));
                    // TODO ????????????????????????

                } else {
                    return DataResult.fail(BusinessResponseCode.INVALID_RECOMMENDATION_CODE.getMsg());
                }
            }
            // ????????????
            if (StringUtils.isNotBlank(shopMemberEntity.getMemberPasswd())) {
                if (StringUtils.equals(shopMemberEntity.getMemberPasswd(), queryResult.getMemberPasswd())) {
                    shopMemberEntity.setMemberPasswd(null);
                } else {
                    shopMemberEntity.setMemberPasswd(PasswordUtils.encode(shopMemberEntity.getMemberPasswd(), PasswordUtils.getSalt()));
                }
            }
            // ????????????
            if (StringUtils.isNotBlank(shopMemberEntity.getPaymentPasswd())) {
                if (StringUtils.equals(shopMemberEntity.getPaymentPasswd(), queryResult.getPaymentPasswd())) {
                    shopMemberEntity.setPaymentPasswd(null);
                } else {
                    shopMemberEntity.setPaymentPasswd(PasswordUtils.encode(shopMemberEntity.getPaymentPasswd(), PasswordUtils.getSalt()));
                }
            }
            // ?????????+1
            shopMemberEntity.setMemberVersion(queryResult.getMemberVersion() + NumberConstants.ONE_L);
            // ????????????(?????????????????????)
            shopMemberMapper.update(shopMemberEntity, Wrappers.<ShopMemberEntity>lambdaQuery().eq(ShopMemberEntity::getMemberId, queryResult.getMemberId()).eq(ShopMemberEntity::getMemberVersion, queryResult.getMemberVersion()));
        }
        return DataResult.success(shopMemberEntity);
    }

    @Override
    public DataResult absolutelyDelete(List<String> memberIdList) {
        if (memberIdList.contains(DelimiterConstants.SYS_ADMIN_ID)) {
            return DataResult.fail(BaseResponseCode.PROHIBIT_OPERATION.getMsg());
        }
        // ??????????????????
        shopMemberMapper.absolutelyDeleteByMemberIdList(memberIdList);
        // ??????????????????
        shopRecommendationRelationshipMapper.absolutelyDeleteByMemberIdList(memberIdList);
        // TODO ??????????????????????????????

        // ????????????
        shopMemberWalletMapper.absolutelyDeleteByMemberIdList(memberIdList);
        // ??????????????????
        shopMemberWalletRecordMapper.absolutelyDeleteByMemberIdList(memberIdList);
        // ???????????????
        shopMemberGrowthValueMapper.absolutelyDeleteByMemberIdList(memberIdList);
        // ?????????????????????
        shopMemberGrowthValueRecordMapper.absolutelyDeleteByMemberIdList(memberIdList);
        return DataResult.success();
    }

    @Override
    public DataResult removeByMemberIds(List<String> memberIdList) {
        if (memberIdList.contains(DelimiterConstants.SYS_ADMIN_ID)) {
            return DataResult.fail(BaseResponseCode.PROHIBIT_OPERATION.getMsg());
        }
        return DataResult.success(shopMemberMapper.deleteBatchIds(memberIdList));
    }

    private IPage<ShopMemberEntity> encapsulatingFieldName(IPage<ShopMemberEntity> iPage) {
        // ????????????
        List<ShopMemberEntity> shopMemberEntityList = iPage.getRecords();
        // ????????????
        if (CollectionUtils.isNotEmpty(shopMemberEntityList)) {
            // ??????????????????
            Map<String, BigDecimal> walletMap = Maps.newHashMap();
            Map<String, BigDecimal> growthMap = Maps.newHashMap();
            // ??????memberId
            List<String> memberIdList = shopMemberEntityList.stream().map(ShopMemberEntity::getMemberId).collect(Collectors.toList());
            // ??????????????????
            List<ShopMemberWalletEntity> shopMemberWalletEntityList = shopMemberWalletMapper.selectList(Wrappers.<ShopMemberWalletEntity>lambdaQuery().in(ShopMemberWalletEntity::getMemberId, memberIdList));
            if (CollectionUtils.isNotEmpty(shopMemberWalletEntityList)) {
                // ??????????????????
                walletMap.putAll(shopMemberWalletEntityList.stream().collect(Collectors.toMap(ShopMemberWalletEntity::getMemberId, ShopMemberWalletEntity::getBalance, (k1, k2) -> k1)));
            }
            // ?????????????????????
            List<ShopMemberGrowthValueEntity> shopMemberGrowthValueEntityList = shopMemberGrowthValueMapper.selectList(Wrappers.<ShopMemberGrowthValueEntity>lambdaQuery().in(ShopMemberGrowthValueEntity::getMemberId, memberIdList));
            if (CollectionUtils.isNotEmpty(shopMemberGrowthValueEntityList)) {
                // ???????????????
                growthMap.putAll(shopMemberGrowthValueEntityList.stream().collect(Collectors.toMap(ShopMemberGrowthValueEntity::getMemberId, ShopMemberGrowthValueEntity::getGrowthValue, (k1, k2) -> k1)));
            }
            // ????????????
            shopMemberEntityList.forEach(shopMemberEntity -> {
                shopMemberEntity.setWalletBalance(walletMap.getOrDefault(shopMemberEntity.getMemberId(), BigDecimal.ZERO));
                shopMemberEntity.setGrowthValue(growthMap.getOrDefault(shopMemberEntity.getMemberId(), BigDecimal.ZERO));
            });
        }
        return iPage;
    }

    @Override
    public DataResult saveFile(MultipartFile file, Integer type) {
        try {
            // ??????
            if (Objects.isNull(file) || StringUtils.isBlank(file.getOriginalFilename()) || StringUtils.equalsIgnoreCase(DelimiterConstants.EMPTY_STR, file.getOriginalFilename().trim())) {
                return DataResult.fail(BusinessResponseCode.FILE_EMPTY.getMsg());
            }
            //???????????????
            String name = file.getOriginalFilename();
            // ???????????????????????????????????????????????????????????????0?????????????????????null??????????????????????????????
            if (file.getSize() == NumberConstants.ZERO || !CommonUtils.validateExcel(name)) {
                return DataResult.fail(BusinessResponseCode.FILE_FORMAT_ERROR.getMsg());
            }
            // 1.??????Excel??????-->????????????
            List<String> excelTitle = ExcelExportUtil.readExcelTitle(file.getInputStream(), name);
            List<String> systemTitle = TitleAndCodeEnum.IMPORT_MEMBER.getTitle();
            if (Objects.isNull(excelTitle) || excelTitle.size() != systemTitle.size()) {
                return DataResult.fail(BusinessResponseCode.FILE_TITLE_ERROR.getMsg());
            }
            // ????????????
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
            // 2.??????Excel????????????
            List<String> systemCode = TitleAndCodeEnum.IMPORT_MEMBER.getCode();
            List<LinkedHashMap<String, Object>> data = ExcelExportUtil.readExcelContent(file.getInputStream(), name, systemCode);
            if (CollectionUtils.isEmpty(data)) {
                return DataResult.fail(BusinessResponseCode.FILE_DATA_EMPTY.getMsg());
            }
            // memberName??????
            List<String> memberNameList = data.stream().map(a -> String.valueOf(a.get(TitleAndCodeEnum.IMPORT_MEMBER.getCode().get(NumberConstants.ZERO)))).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(memberNameList)) {
                return DataResult.fail(BusinessResponseCode.FILE_DATA_EMPTY.getMsg());
            }
            // ??????????????????
            List<ShopMemberEntity> shopMemberEntityList = shopMemberMapper.findListByMemberNameList(memberNameList);
            switch (type) {
                case NumberConstants.ONE:
                    if (CollectionUtils.isNotEmpty(shopMemberEntityList)) {
                        return DataResult.fail(BusinessResponseCode.ACCOUNT_REPEAT.getMsg().concat(DelimiterConstants.COLON).concat(Joiner.on(DelimiterConstants.COMMA).skipNulls().join(shopMemberEntityList.stream().map(ShopMemberEntity::getMemberName).collect(Collectors.toList()))));
                    }
                    // ????????????
                    memberNameList.forEach(memberName -> saveShopMemberEntity(new ShopMemberEntity(memberName)));
                    break;
                case NumberConstants.TWO:
                    // ????????????
                    return removeByMemberIds(shopMemberEntityList.stream().map(ShopMemberEntity::getMemberId).collect(Collectors.toList()));
                case NumberConstants.THREE:
                    // ????????????
                    return absolutelyDelete(shopMemberEntityList.stream().map(ShopMemberEntity::getMemberId).collect(Collectors.toList()));
            }
            return DataResult.success(data);
        } catch (Exception e) {
            return DataResult.fail(e.getMessage());
        }
    }


}
