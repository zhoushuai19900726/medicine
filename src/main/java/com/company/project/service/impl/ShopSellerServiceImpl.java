package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.common.utils.DictionariesKeyConstant;
import com.company.project.common.utils.PasswordUtils;
import com.company.project.entity.AddressLibraryEntity;
import com.company.project.entity.ShopSellerEntity;
import com.company.project.mapper.AddressLibraryMapper;
import com.company.project.mapper.ShopSellerMapper;
import com.company.project.service.AddressLibraryService;
import com.company.project.service.ShopSellerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author
 * @version V1.0
 * @date 2020年3月18日
 */
@Transactional
@Service("sellerService")
public class ShopSellerServiceImpl extends ServiceImpl<ShopSellerMapper, ShopSellerEntity> implements ShopSellerService {

    @Resource
    private ShopSellerMapper shopSellerMapper;

    @Resource
    private AddressLibraryMapper addressLibraryMapper;

    @Override
    public DataResult saveShopSellerEntity(ShopSellerEntity shopSellerEntity) {
        // 商家地址
        if(StringUtils.isNotBlank(shopSellerEntity.getProvince()) && StringUtils.isNotBlank(shopSellerEntity.getCity()) && StringUtils.isNotBlank(shopSellerEntity.getCounty())){
            List<AddressLibraryEntity> addressLibraryEntityList = addressLibraryMapper.selectList(Wrappers.lambdaQuery());
            Map<String, String> addressLibraryEntityMap = addressLibraryEntityList.stream().collect(Collectors.toMap(AddressLibraryEntity::getId, AddressLibraryEntity::getName, (k1, k2) -> k1));
            shopSellerEntity.setSellerAddress(addressLibraryEntityMap.getOrDefault(shopSellerEntity.getProvince(), DelimiterConstants.EMPTY_STR) +
                    addressLibraryEntityMap.getOrDefault(shopSellerEntity.getCity(), DelimiterConstants.EMPTY_STR) +
                    addressLibraryEntityMap.getOrDefault(shopSellerEntity.getCounty(), DelimiterConstants.EMPTY_STR) +
                    shopSellerEntity.getAddress());
        }
        return DataResult.success(shopSellerMapper.insert(shopSellerEntity));
    }

    @Override
    public DataResult updateShopSellerEntityById(ShopSellerEntity shopSellerEntity) {
        // 商家地址
        if(StringUtils.isNotBlank(shopSellerEntity.getProvince()) && StringUtils.isNotBlank(shopSellerEntity.getCity()) && StringUtils.isNotBlank(shopSellerEntity.getCounty())){
            List<AddressLibraryEntity> addressLibraryEntityList = addressLibraryMapper.selectList(Wrappers.lambdaQuery());
            Map<String, String> addressLibraryEntityMap = addressLibraryEntityList.stream().collect(Collectors.toMap(AddressLibraryEntity::getId, AddressLibraryEntity::getName, (k1, k2) -> k1));
            shopSellerEntity.setSellerAddress(addressLibraryEntityMap.getOrDefault(shopSellerEntity.getProvince(), DelimiterConstants.EMPTY_STR) +
                    addressLibraryEntityMap.getOrDefault(shopSellerEntity.getCity(), DelimiterConstants.EMPTY_STR) +
                    addressLibraryEntityMap.getOrDefault(shopSellerEntity.getCounty(), DelimiterConstants.EMPTY_STR) +
                    shopSellerEntity.getAddress());
        }
        return DataResult.success(shopSellerMapper.updateById(shopSellerEntity));
    }


}
