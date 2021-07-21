package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.common.utils.DictionariesKeyConstant;
import com.company.project.common.utils.NumberConstants;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.AddressLibraryMapper;
import com.company.project.entity.AddressLibraryEntity;
import com.company.project.service.AddressLibraryService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Transactional
@Service("addressLibraryService")
public class AddressLibraryServiceImpl extends ServiceImpl<AddressLibraryMapper, AddressLibraryEntity> implements AddressLibraryService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private AddressLibraryMapper addressLibraryMapper;


    @Override
    public DataResult saveAddressLibraryEntity(AddressLibraryEntity addressLibraryEntity) {
        addressLibraryMapper.insert(addressLibraryEntity);
        AddressLibraryEntity queryResult = addressLibraryMapper.selectById(addressLibraryEntity.getId());
        redisTemplate.boundHashOps(DictionariesKeyConstant.ADDRESS_LIBRARY_KEY_PREFIX.concat(queryResult.getParentId())).put(queryResult.getId(), queryResult);
        return DataResult.success(addressLibraryEntity);
    }

    @Override
    public DataResult updateAddressLibraryEntityById(AddressLibraryEntity addressLibraryEntity) {
        addressLibraryMapper.updateById(addressLibraryEntity);
        AddressLibraryEntity queryResult = addressLibraryMapper.selectById(addressLibraryEntity.getId());
        if (StringUtils.equals(NumberConstants.ONE_STR, addressLibraryEntity.getIsShow())) {
            redisTemplate.boundHashOps(DictionariesKeyConstant.ADDRESS_LIBRARY_KEY_PREFIX.concat(queryResult.getParentId())).put(queryResult.getId(), queryResult);
        } else {
            redisTemplate.boundHashOps(DictionariesKeyConstant.ADDRESS_LIBRARY_KEY_PREFIX.concat(queryResult.getParentId())).delete(queryResult.getId());
        }
        return DataResult.success(addressLibraryEntity);
    }

    @Override
    public DataResult removeByIdlist(List<String> idList) {
        List<AddressLibraryEntity> addressLibraryEntityList = addressLibraryMapper.selectBatchIds(idList);
        if (CollectionUtils.isNotEmpty(addressLibraryEntityList)) {
            Map<String, List<AddressLibraryEntity>> groupBy = addressLibraryEntityList.stream().collect(Collectors.groupingBy(AddressLibraryEntity::getParentId));
            groupBy.forEach((k, v) -> redisTemplate.boundHashOps(DictionariesKeyConstant.ADDRESS_LIBRARY_KEY_PREFIX.concat(k)).delete(v.stream().map(AddressLibraryEntity::getId).toArray()));
        }
        addressLibraryMapper.deleteBatchIds(idList);
        return DataResult.success(idList);
    }

    @Override
    public DataResult findAllProvinceAndCity() {
        List<AddressLibraryEntity> addressLibraryEntityList = addressLibraryMapper.selectList(Wrappers.<AddressLibraryEntity>lambdaQuery().in(AddressLibraryEntity::getLevel, Lists.newArrayList(NumberConstants.ONE_I, NumberConstants.TWO_I)).eq(AddressLibraryEntity::getIsShow, NumberConstants.ONE_STR).orderByAsc(AddressLibraryEntity::getSeq));
        Map<String, List<AddressLibraryEntity>> groupBy = addressLibraryEntityList.stream().collect(Collectors.groupingBy(AddressLibraryEntity::getParentId));
        Map<String, List<AddressLibraryEntity>> map = Maps.newLinkedHashMap();
        addressLibraryEntityList.stream().filter(a -> StringUtils.equals(NumberConstants.ZERO_STR, a.getParentId())).forEach(addressLibraryEntity -> map.put(addressLibraryEntity.getId().concat(DelimiterConstants.LINE).concat(addressLibraryEntity.getName()), groupBy.getOrDefault(addressLibraryEntity.getId(), Lists.newArrayList())));
        Map<String, Map<String, List<AddressLibraryEntity>>> finalResult = Maps.newHashMap();
        finalResult.put("中国", map);
        return DataResult.success(finalResult);
    }

}
