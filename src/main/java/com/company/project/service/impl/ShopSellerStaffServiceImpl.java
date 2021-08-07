package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.exception.BusinessException;
import com.company.project.common.exception.code.BaseResponseCode;
import com.company.project.common.exception.code.BusinessResponseCode;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.common.utils.PasswordUtils;
import com.company.project.entity.ShopSellerEntity;
import com.company.project.entity.SysDept;
import com.company.project.entity.SysUser;
import com.company.project.mapper.ShopSellerMapper;
import com.company.project.mapper.SysDeptMapper;
import com.company.project.mapper.SysUserMapper;
import com.company.project.service.HttpSessionService;
import com.company.project.service.PermissionService;
import com.company.project.service.RoleService;
import com.company.project.vo.resp.LoginRespVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopSellerStaffMapper;
import com.company.project.entity.ShopSellerStaffEntity;
import com.company.project.service.ShopSellerStaffService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("shopSellerStaffService")
public class ShopSellerStaffServiceImpl extends ServiceImpl<ShopSellerStaffMapper, ShopSellerStaffEntity> implements ShopSellerStaffService {

    @Resource
    private ShopSellerStaffMapper shopSellerStaffMapper;

    @Resource
    private ShopSellerMapper shopSellerMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private RoleService roleService;
    @Resource
    private PermissionService permissionService;

    @Resource
    private HttpSessionService httpSessionService;

    @Value("${spring.redis.allowMultipleLogin}")
    private Boolean allowMultipleLogin;

    @Override
    public IPage<ShopSellerStaffEntity> listByPage(Page<ShopSellerStaffEntity> page, LambdaQueryWrapper<ShopSellerStaffEntity> wrapper) {
        return encapsulatingFieldName(shopSellerStaffMapper.selectPage(page, wrapper));
    }

    @Override
    public DataResult saveShopSellerStaffEntity(ShopSellerStaffEntity shopSellerStaff) {
        ShopSellerStaffEntity result = shopSellerStaffMapper.selectOne(Wrappers.<ShopSellerStaffEntity>lambdaQuery().eq(ShopSellerStaffEntity::getAccount, shopSellerStaff.getAccount()).eq(ShopSellerStaffEntity::getSellerId, shopSellerStaff.getSellerId()));
        if (Objects.nonNull(result)) {
            return DataResult.fail(BusinessResponseCode.ACCOUNT_REPEAT.getMsg());
        }
        shopSellerStaff.setSalt(PasswordUtils.getSalt());
        if (StringUtils.isNotBlank(shopSellerStaff.getPassword())) {
            shopSellerStaff.setPassword(PasswordUtils.encode(shopSellerStaff.getPassword(), shopSellerStaff.getSalt()));
        } else {
            shopSellerStaff.setPassword(PasswordUtils.encode(DelimiterConstants.INIT_PASSWORD, shopSellerStaff.getSalt()));
        }
        return DataResult.success(shopSellerStaffMapper.insert(shopSellerStaff));
    }

    @Override
    public DataResult updateShopSellerStaffEntityById(ShopSellerStaffEntity shopSellerStaff) {
        ShopSellerStaffEntity result = shopSellerStaffMapper.selectOne(Wrappers.<ShopSellerStaffEntity>lambdaQuery().eq(ShopSellerStaffEntity::getAccount, shopSellerStaff.getAccount()).eq(ShopSellerStaffEntity::getSellerId, shopSellerStaff.getSellerId()).ne(ShopSellerStaffEntity::getId, shopSellerStaff.getId()));
        if (Objects.nonNull(result)) {
            return DataResult.fail(BusinessResponseCode.ACCOUNT_REPEAT.getMsg());
        }
        ShopSellerStaffEntity queryResult = shopSellerStaffMapper.selectById(shopSellerStaff.getId());
        if (StringUtils.isNotBlank(shopSellerStaff.getPassword()) && !StringUtils.equals(shopSellerStaff.getPassword(), queryResult.getPassword())) {
            shopSellerStaff.setSalt(PasswordUtils.getSalt());
            shopSellerStaff.setPassword(PasswordUtils.encode(shopSellerStaff.getPassword(), shopSellerStaff.getSalt()));
        }
        return DataResult.success(shopSellerStaffMapper.updateById(shopSellerStaff));
    }

    @Override
    public LoginRespVO login(SysUser vo) {
        ShopSellerStaffEntity shopSellerStaffEntity = shopSellerStaffMapper.selectOne(Wrappers.<ShopSellerStaffEntity>lambdaQuery().eq(ShopSellerStaffEntity::getAccount, vo.getUsername()).eq(ShopSellerStaffEntity::getSellerId, vo.getSellerId()));
        if (null == shopSellerStaffEntity) {
            throw new BusinessException(BaseResponseCode.NOT_ACCOUNT);
        }
        if (shopSellerStaffEntity.getStatus() == 0) {
            throw new BusinessException(BaseResponseCode.USER_LOCK);
        }
        if (!PasswordUtils.matches(shopSellerStaffEntity.getSalt(), vo.getPassword(), shopSellerStaffEntity.getPassword())) {
            throw new BusinessException(BaseResponseCode.PASSWORD_ERROR);
        }
        LoginRespVO respVO = new LoginRespVO();
        respVO.setId(shopSellerStaffEntity.getId());
        respVO.setUsername(shopSellerStaffEntity.getName());
        respVO.setPhone(shopSellerStaffEntity.getContactNumber());
        //是否删除之前token， 此处控制是否支持多登陆端；
        // true:允许多处登陆; false:只能单处登陆，顶掉之前登陆
        if (!allowMultipleLogin) {
            httpSessionService.abortUserById(shopSellerStaffEntity.getId());
        }


        SysUser sysUser = sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, "common_staff"));
        if (Objects.isNull(sysUser)) {
            throw new BusinessException("通道已被关闭, 请联系管理员开启员工通道");
        }
        String userId = sysUser.getId();
        if (StringUtils.isNotBlank(sysUser.getDeptId())) {
            SysDept sysDept = sysDeptMapper.selectById(sysUser.getDeptId());
            if (sysDept != null) {
                sysUser.setDeptNo(sysDept.getDeptNo());
            }
        }
        sysUser.setId(shopSellerStaffEntity.getId());

        String token = httpSessionService.createTokenAndUser(sysUser, roleService.getRoleNames(userId), permissionService.getPermissionsByUserId(userId));
        respVO.setAccessToken(token);

        return respVO;
    }

    private IPage<ShopSellerStaffEntity> encapsulatingFieldName(IPage<ShopSellerStaffEntity> iPage) {
        // 员工集合
        List<ShopSellerStaffEntity> shopSellerStaffEntitieList = iPage.getRecords();
        // 封装名称
        if (CollectionUtils.isNotEmpty(shopSellerStaffEntitieList)) {
            // 查询结果封装
            Map<String, String> shopSellerEntityMap = Maps.newHashMap();
            // 提取商家ID集合
            List<String> sellerIdList = shopSellerStaffEntitieList.stream().map(ShopSellerStaffEntity::getSellerId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(sellerIdList)) {
                // 查询商家集合
                List<ShopSellerEntity> shopSellerEntityList = shopSellerMapper.selectBatchIds(sellerIdList);
                if (CollectionUtils.isNotEmpty(shopSellerEntityList)) {
                    // 封装商家名称
                    shopSellerEntityMap.putAll(shopSellerEntityList.stream().collect(Collectors.toMap(ShopSellerEntity::getId, ShopSellerEntity::getSellerName, (k1, k2) -> k1)));
                }
            }
            // 执行封装名称
            shopSellerStaffEntitieList.forEach(shopSellerStaffEntity -> shopSellerStaffEntity.setSellerName(shopSellerEntityMap.getOrDefault(shopSellerStaffEntity.getSellerId(), DelimiterConstants.EMPTY_STR)));
        }
        return iPage;
    }
}
