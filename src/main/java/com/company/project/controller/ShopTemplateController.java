package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.aop.annotation.DataScope;
import com.company.project.common.aop.annotation.LogAnnotation;
import com.company.project.entity.ShopParaEntity;
import com.company.project.entity.ShopSpecEntity;
import com.company.project.service.ShopParaService;
import com.company.project.service.ShopSpecService;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopTemplateEntity;
import com.company.project.service.ShopTemplateService;

import javax.annotation.Resource;


/**
 * 商品模板
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-10 11:02:07
 */
@Api(tags = "商品模板")
@Controller
@RequestMapping("/")
public class ShopTemplateController extends BaseController {

    @Resource
    private ShopTemplateService shopTemplateService;

    @Resource
    private ShopSpecService shopSpecService;

    @Resource
    private ShopParaService shopParaService;

    // *******************************************************************************************模板*******************************************************************************************  //

    @ApiOperation(value = "跳转进入列表页面")
    @GetMapping("/index/shopTemplate")
    public String shopTemplate() {
        return "template/templateList";
    }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopTemplate/addOrUpdate")
    public String addOrUpdate() {
        return "template/addOrUpdate";
    }

    @ApiOperation(value = "跳转进入模板设置页面")
    @GetMapping("/index/shopTemplate/templateConfig")
    public String templateConfig() {
        return "template/templateConfig";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopTemplate/add")
    @RequiresPermissions("shopTemplate:add")
    @LogAnnotation(title = "商品模板", action = "新增模板")
    @ResponseBody
    public DataResult add(@RequestBody ShopTemplateEntity shopTemplate) {
        return DataResult.success(shopTemplateService.save(shopTemplate));
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopTemplate/delete")
    @RequiresPermissions("shopTemplate:delete")
    @LogAnnotation(title = "商品模板", action = "删除模板")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return DataResult.success(shopTemplateService.removeByIds(ids));
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopTemplate/update")
    @RequiresPermissions("shopTemplate:update")
    @LogAnnotation(title = "商品模板", action = "更新模板")
    @ResponseBody
    public DataResult update(@RequestBody ShopTemplateEntity shopTemplate) {
        return DataResult.success(shopTemplateService.updateById(shopTemplate));
    }

    @ApiOperation(value = "查询模板分页数据")
    @PostMapping("shopTemplate/listByPage")
    @RequiresPermissions("shopTemplate:list")
    @LogAnnotation(title = "商品模板", action = "查询模板分页数据")
    @DataScope
    @ResponseBody
    public DataResult findTemplateListByPage(@RequestBody ShopTemplateEntity shopTemplate) {
        // 分页初始化
        Page<ShopTemplateEntity> page = new Page<>(shopTemplate.getPage(), shopTemplate.getLimit());
        // 查询条件
        LambdaQueryWrapper<ShopTemplateEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopTemplate.getId()), ShopTemplateEntity::getId, shopTemplate.getId())
                .like(StringUtils.isNotBlank(shopTemplate.getName()), ShopTemplateEntity::getName, shopTemplate.getName())
                .apply(StringUtils.isNotBlank(shopTemplate.getCreateStartTime()), "UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + shopTemplate.getCreateStartTime() + "')")
                .apply(StringUtils.isNotBlank(shopTemplate.getCreateEndTime()), "UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP('" + shopTemplate.getCreateEndTime() + "')")
                .orderByDesc(ShopTemplateEntity::getCreateTime);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopTemplateService.page(page, encapsulationDataRights(shopTemplate, queryWrapper, ShopTemplateEntity::getCreateId))));
    }

    // *******************************************************************************************规格*******************************************************************************************  //

    @ApiOperation(value = "跳转进入新增/编辑规格页面")
    @GetMapping("/index/shopTemplate/addOrUpdateSpec")
    public String addOrUpdateSpec() {
        return "template/addOrUpdateSpec";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopTemplate/addSpec")
    @RequiresPermissions("shopTemplate:add")
    @LogAnnotation(title = "商品规格", action = "新增规格")
    @ResponseBody
    public DataResult addSpec(@RequestBody ShopSpecEntity shopSpecEntity) {
        return shopSpecService.saveShopSpecEntity(shopSpecEntity);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopTemplate/deleteSpec")
    @RequiresPermissions("shopTemplate:delete")
    @LogAnnotation(title = "商品规格", action = "删除规格")
    @ResponseBody
    public DataResult deleteSpec(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return shopSpecService.removeShopSpecEntityByIds(ids);
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopTemplate/updateSpec")
    @RequiresPermissions("shopTemplate:update")
    @LogAnnotation(title = "商品规格", action = "更新规格")
    @ResponseBody
    public DataResult updateSpec(@RequestBody ShopSpecEntity shopSpecEntity) {
        return shopSpecService.updateShopSpecEntityById(shopSpecEntity);
    }

    @ApiOperation(value = "查询规格分页数据")
    @PostMapping("shopTemplate/specListByPage")
    @RequiresPermissions("shopTemplate:list")
    @LogAnnotation(title = "商品模板", action = "查询规格分页数据")
    @DataScope
    @ResponseBody
    public DataResult findSpecListByPage(@RequestBody ShopSpecEntity shopSpecEntity) {
        // 分页初始化
        Page<ShopSpecEntity> page = new Page<>(shopSpecEntity.getPage(), shopSpecEntity.getLimit());
        // 查询条件
        LambdaQueryWrapper<ShopSpecEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(StringUtils.isNotBlank(shopSpecEntity.getTemplateId()), ShopSpecEntity::getTemplateId, shopSpecEntity.getTemplateId()).orderByAsc(ShopSpecEntity::getSeq);
        // 封装数据权限 - 执行查询 - 响应前端
        return DataResult.success(shopSpecService.page(page, encapsulationDataRights(shopSpecEntity, queryWrapper, ShopSpecEntity::getCreateId)));
    }

    @ApiOperation(value = "查询所有规格数据")
    @PostMapping("shopTemplate/findAllSpecList")
    @RequiresPermissions("shopTemplate:list")
    @LogAnnotation(title = "商品模板", action = "查询所有规格数据")
    @DataScope
    @ResponseBody
    public DataResult findAllSpecList(@RequestBody ShopSpecEntity shopSpecEntity) {
        // 查询条件
        LambdaQueryWrapper<ShopSpecEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(StringUtils.isNotBlank(shopSpecEntity.getTemplateId()), ShopSpecEntity::getTemplateId, shopSpecEntity.getTemplateId()).orderByAsc(ShopSpecEntity::getSeq);
        // 封装数据权限 - 执行查询 - 响应前端
        return DataResult.success(shopSpecService.list(encapsulationDataRights(shopSpecEntity, queryWrapper, ShopSpecEntity::getCreateId)));
    }

    // *******************************************************************************************参数*******************************************************************************************  //

    @ApiOperation(value = "跳转进入新增/编辑参数页面")
    @GetMapping("/index/shopTemplate/addOrUpdatePara")
    public String addOrUpdatePara() {
        return "template/addOrUpdatePara";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopTemplate/addPara")
    @RequiresPermissions("shopTemplate:add")
    @LogAnnotation(title = "商品参数", action = "新增参数")
    @ResponseBody
    public DataResult addPara(@RequestBody ShopParaEntity shopParaEntity) {
        return shopParaService.saveShopParaEntity(shopParaEntity);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopTemplate/deletePara")
    @RequiresPermissions("shopTemplate:delete")
    @LogAnnotation(title = "商品参数", action = "删除参数")
    @ResponseBody
    public DataResult deletePara(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return shopParaService.removeShopParaEntityByIds(ids);
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopTemplate/updatePara")
    @RequiresPermissions("shopTemplate:update")
    @LogAnnotation(title = "商品参数", action = "更新参数")
    @ResponseBody
    public DataResult updatePara(@RequestBody ShopParaEntity shopParaEntity) {
        return shopParaService.updateShopParaEntityById(shopParaEntity);
    }

    @ApiOperation(value = "查询参数分页数据")
    @PostMapping("shopTemplate/paraListByPage")
    @RequiresPermissions("shopTemplate:list")
    @LogAnnotation(title = "商品模板", action = "查询参数分页数据")
    @DataScope
    @ResponseBody
    public DataResult findParaListByPage(@RequestBody ShopParaEntity shopParaEntity) {
        // 分页初始化
        Page<ShopParaEntity> page = new Page<>(shopParaEntity.getPage(), shopParaEntity.getLimit());
        // 查询条件
        LambdaQueryWrapper<ShopParaEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(StringUtils.isNotBlank(shopParaEntity.getTemplateId()), ShopParaEntity::getTemplateId, shopParaEntity.getTemplateId()).orderByAsc(ShopParaEntity::getSeq);
        // 封装数据权限 - 执行查询 - 响应前端
        return DataResult.success(shopParaService.page(page, encapsulationDataRights(shopParaEntity, queryWrapper, ShopParaEntity::getCreateId)));
    }

    @ApiOperation(value = "查询所有参数数据")
    @PostMapping("shopTemplate/findAllParaList")
    @RequiresPermissions("shopTemplate:list")
    @LogAnnotation(title = "商品模板", action = "查询所有参数数据")
    @DataScope
    @ResponseBody
    public DataResult findAllParaList(@RequestBody ShopParaEntity shopParaEntity) {
        // 查询条件
        LambdaQueryWrapper<ShopParaEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(StringUtils.isNotBlank(shopParaEntity.getTemplateId()), ShopParaEntity::getTemplateId, shopParaEntity.getTemplateId()).orderByAsc(ShopParaEntity::getSeq);
        // 封装数据权限 - 执行查询 - 响应前端
        return DataResult.success(shopParaService.list(encapsulationDataRights(shopParaEntity, queryWrapper, ShopParaEntity::getCreateId)));
    }


}
