package com.company.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.common.utils.PasswordUtils;
import com.company.project.entity.ShopSellerEntity;
import com.company.project.mapper.ShopSellerMapper;
import com.company.project.service.ShopSellerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 *
 *
 * @author
 * @version V1.0
 * @date 2020年3月18日
 */
@Transactional
@Service("sellerService")
public class ShopSellerServiceImpl extends ServiceImpl<ShopSellerMapper, ShopSellerEntity> implements ShopSellerService {

    @Resource
    private ShopSellerMapper shopSellerMapper;

    @Override
    public DataResult saveShopSellerEntity(ShopSellerEntity shopSellerEntity) {
        // 密码加密
        if (StringUtils.isNotBlank(shopSellerEntity.getPassword())) {
            shopSellerEntity.setPassword(PasswordUtils.encode(shopSellerEntity.getPassword(), PasswordUtils.getSalt()));
        } else {
            shopSellerEntity.setPassword(PasswordUtils.encode(DelimiterConstants.INIT_PASSWORD, PasswordUtils.getSalt()));
        }
        return DataResult.success(shopSellerMapper.insert(shopSellerEntity));
    }

    @Override
    public DataResult updateShopSellerEntityById(ShopSellerEntity shopSellerEntity) {
        // 查询商家
        ShopSellerEntity queryResult = shopSellerMapper.selectById(shopSellerEntity.getId());
        if(StringUtils.isNotBlank(shopSellerEntity.getPassword()) && !StringUtils.equals(shopSellerEntity.getPassword(), queryResult.getPassword())){
            shopSellerEntity.setPassword(PasswordUtils.encode(shopSellerEntity.getPassword(), PasswordUtils.getSalt()));
        }
        return DataResult.success(shopSellerMapper.updateById(shopSellerEntity));
    }


}
