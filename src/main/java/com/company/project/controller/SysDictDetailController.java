package com.company.project.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.Constant;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.NumberConstants;
import com.company.project.entity.SysDictDetailEntity;
import com.company.project.entity.SysDictEntity;
import com.company.project.service.SysDictDetailService;
import com.company.project.service.SysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 字典明细管理
 *
 * @author zhoushuai
 * @version V1.0
 * @date 2020年3月18日
 */
@Api(tags = "字典明细管理")
@RestController
@RequestMapping("/sysDictDetail")
public class SysDictDetailController {

    @Resource
    private SysDictService sysDictService;

    @Resource
    private SysDictDetailService sysDictDetailService;

    @Resource
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "新增")
    @PostMapping("/add")
    @RequiresPermissions("sysDict:add")
    public DataResult add(@RequestBody SysDictDetailEntity sysDictDetail) {
        if (StringUtils.isBlank(sysDictDetail.getValue())) {
            return DataResult.fail("字典值不能为空");
        }
        SysDictDetailEntity sysDictDetailEntity = sysDictDetailService.getOne(Wrappers.<SysDictDetailEntity>lambdaQuery().eq(SysDictDetailEntity::getValue, sysDictDetail.getValue()).eq(SysDictDetailEntity::getDictId, sysDictDetail.getDictId()));
        if (Objects.nonNull(sysDictDetailEntity)) {
            return DataResult.fail("字典名称-字典值已存在");
        }
        // 保存字典值
        sysDictDetailService.save(sysDictDetail);
        // 加入缓存
        SysDictEntity sysDictEntity = sysDictService.getById(sysDictDetail.getDictId());
        if (Objects.nonNull(sysDictEntity)) {
            redisTemplate.boundHashOps(Constant.DICT_KEY_PREFIX.concat(sysDictEntity.getName())).put(sysDictDetail.getId(), sysDictDetail);
        }
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/delete")
    @RequiresPermissions("sysDict:delete")
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        // 删除缓存
        List<SysDictDetailEntity> sysDictDetailEntityList = sysDictDetailService.listByIds(ids);
        if (CollectionUtils.isNotEmpty(sysDictDetailEntityList)) {
            List<String> dictIdList = sysDictDetailEntityList.stream().map(SysDictDetailEntity::getDictId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(dictIdList)) {
                List<SysDictEntity> sysDictEntityList = sysDictService.listByIds(dictIdList);
                if (CollectionUtils.isNotEmpty(sysDictEntityList)) {
                    Map<String, String> sysDictEntityMap = sysDictEntityList.stream().collect(Collectors.toMap(SysDictEntity::getId, SysDictEntity::getName, (k1, k2) -> k1));
                    Map<String, List<SysDictDetailEntity>> groupBy = sysDictDetailEntityList.stream().collect(Collectors.groupingBy(SysDictDetailEntity::getDictId));
                    groupBy.forEach((k, v)  -> {
                        if(sysDictEntityMap.containsKey(k)){
                            redisTemplate.boundHashOps(Constant.DICT_KEY_PREFIX.concat(sysDictEntityMap.get(k))).delete(v.stream().map(SysDictDetailEntity::getId).toArray());
                        }
                    });
                }
            }
        }
        return DataResult.success(sysDictDetailService.removeByIds(ids));
    }

    @ApiOperation(value = "更新")
    @PutMapping("/update")
    @RequiresPermissions("sysDict:update")
    public DataResult update(@RequestBody SysDictDetailEntity sysDictDetail) {
        if (StringUtils.isEmpty(sysDictDetail.getValue())) {
            return DataResult.fail("字典值不能为空");
        }
        SysDictDetailEntity sysDictDetailEntity = sysDictDetailService.getOne(Wrappers.<SysDictDetailEntity>lambdaQuery().eq(SysDictDetailEntity::getValue, sysDictDetail.getValue()).eq(SysDictDetailEntity::getDictId, sysDictDetail.getDictId()).ne(SysDictDetailEntity::getId, sysDictDetail.getId()));
        if (Objects.nonNull(sysDictDetailEntity)) {
            return DataResult.fail("字典名称-字典值已存在");
        }
        // 保存更新
        sysDictDetailService.updateById(sysDictDetail);
        // 更新缓存
        SysDictEntity sysDictEntity = sysDictService.getById(sysDictDetail.getDictId());
        redisTemplate.boundHashOps(Constant.DICT_KEY_PREFIX.concat(sysDictEntity.getName())).put(sysDictDetail.getId(), sysDictDetail);
        return DataResult.success();
    }


    @ApiOperation(value = "查询列表数据")
    @PostMapping("/listByPage")
    @RequiresPermissions("sysDict:list")
    public DataResult findListByPage(@RequestBody SysDictDetailEntity sysDictDetail) {
        if (StringUtils.isEmpty(sysDictDetail.getDictId())) {
            return DataResult.success();
        }
        return DataResult.success(sysDictDetailService.listByPage(new Page<>(sysDictDetail.getPage(), sysDictDetail.getLimit()), sysDictDetail.getDictId()));
    }

}
