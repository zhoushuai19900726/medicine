package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.aop.annotation.DataScope;
import com.company.project.common.aop.annotation.LogAnnotation;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.common.utils.NumberConstants;
import com.company.project.entity.ShopTemplateEntity;
import com.company.project.service.ShopTemplateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.company.project.common.utils.DataResult;

import com.company.project.entity.ShopCategoryEntity;
import com.company.project.service.ShopCategoryService;

import javax.annotation.Resource;


/**
 * 商品分类
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-16 10:33:59
 */
@Controller
@RequestMapping("/")
public class ShopCategoryController extends BaseController {

    @Resource
    private ShopCategoryService shopCategoryService;

    @Resource
    private ShopTemplateService shopTemplateService;


    @ApiOperation(value = "跳转到一级列表页面")
    @GetMapping("/index/shopCategory")
    public String shopCategory() {
        return "category/categoryList";
    }

    @ApiOperation(value = "跳转到二级列表页面")
    @GetMapping("/index/shopCategory2")
    public String shopCategory2() {
        return "category/categoryList2";
    }

    @ApiOperation(value = "跳转到三级列表页面")
    @GetMapping("/index/shopCategory3")
    public String shopCategory3() {
        return "category/categoryList3";
    }

    @ApiOperation(value = "跳转进入新增/编辑一级分类页面")
    @GetMapping("/index/shopCategory/addOrUpdate")
    public String addOrUpdate() {
        return "category/addOrUpdate";
    }


    @ApiOperation(value = "跳转进入新增/编辑二级分类页面")
    @GetMapping("/index/shopCategory/addOrUpdate2")
    public String addOrUpdate2() {
        return "category/addOrUpdate2";
    }

    @ApiOperation(value = "跳转进入新增/编辑三级分类页面")
    @GetMapping("/index/shopCategory/addOrUpdate3/{parentId}")
    public String addOrUpdate3(@PathVariable("parentId") String parentId, Model model) {
        model.addAttribute("parentId", parentId);
        return "category/addOrUpdate3";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopCategory/add")
    @RequiresPermissions("shopCategory:add")
    @LogAnnotation(title = "商品分类", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopCategoryEntity shopCategory) {
        return DataResult.success(shopCategoryService.save(shopCategory));
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopCategory/delete")
    @RequiresPermissions("shopCategory:delete")
    @LogAnnotation(title = "商品分类", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return DataResult.success(shopCategoryService.removeByIds(ids));
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopCategory/update")
    @RequiresPermissions("shopCategory:update")
    @LogAnnotation(title = "商品分类", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopCategoryEntity shopCategory) {
        return DataResult.success(shopCategoryService.updateById(shopCategory));
    }

    @ApiOperation(value = "根据ID查询分类")
    @GetMapping("shopCategory/findById/{id}")
    @RequiresPermissions("shopCategory:list")
    @LogAnnotation(title = "商品分类", action = "根据ID查询分类")
    @ResponseBody
    public DataResult findById(@PathVariable("id") String id) {
        ShopCategoryEntity shopCategoryEntity = shopCategoryService.getById(id);
        if(Objects.nonNull(shopCategoryEntity)){
            ShopTemplateEntity shopTemplateEntity = shopTemplateService.getById(shopCategoryEntity.getTemplateId());
            if(Objects.nonNull(shopTemplateEntity)){
                shopCategoryEntity.setTemplateName(shopTemplateEntity.getName());
            }
        }
        return DataResult.success(shopCategoryEntity);
    }

    @ApiOperation(value = "查询下级分类")
    @GetMapping("shopCategory/findSubordinateCategoryList/{parentId}")
    @RequiresPermissions("shopCategory:list")
    @LogAnnotation(title = "商品分类", action = "查询下级分类")
    @ResponseBody
    public DataResult findSubordinateCategoryList(@PathVariable("parentId") String parentId) {
        return DataResult.success(shopCategoryService.findSubordinateCategoryList(parentId));
    }

    @ApiOperation(value = "查询所有分类")
    @GetMapping("shopCategory/listByAll")
    @RequiresPermissions("shopCategory:list")
    @LogAnnotation(title = "商品分类", action = "查询所有分类")
    @ResponseBody
    public DataResult listByAll() {
        return DataResult.success(shopCategoryService.listByAll());
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopCategory/listByPage")
    @RequiresPermissions("shopCategory:list")
    @LogAnnotation(title = "商品分类", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopCategoryEntity shopCategory) {
        // 分页初始化
        Page<ShopCategoryEntity> page = new Page<>(shopCategory.getPage(), shopCategory.getLimit());
        // 查询条件
        LambdaQueryWrapper<ShopCategoryEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopCategory.getId()), ShopCategoryEntity::getId, shopCategory.getId())
                .eq(StringUtils.isNotBlank(shopCategory.getParentId()), ShopCategoryEntity::getParentId, shopCategory.getParentId())
                .like(StringUtils.isNotBlank(shopCategory.getName()), ShopCategoryEntity::getName, shopCategory.getName())
                .orderByAsc(ShopCategoryEntity::getSeq);
        // 封装数据权限 - 执行查询
        IPage<ShopCategoryEntity> iPage = shopCategoryService.page(page, encapsulationDataRights(shopCategory, queryWrapper, ShopCategoryEntity::getCreateId));
        // 封装模板名称
        List<ShopCategoryEntity> shopCategoryEntityList = iPage.getRecords();
        if (CollectionUtils.isNotEmpty(shopCategoryEntityList)) {
            //  提取模板ID集合
            List<String> templateIdList = shopCategoryEntityList.stream().map(ShopCategoryEntity::getTemplateId).collect(Collectors.toList());
            // 查询模板集合
            List<ShopTemplateEntity> shopTemplateEntityList = shopTemplateService.listByIds(templateIdList);
            if (CollectionUtils.isNotEmpty(shopTemplateEntityList)) {
                // 封装模板名称
                Map<String, String> shopTemplateEntityMap = shopTemplateEntityList.stream().collect(Collectors.toMap(ShopTemplateEntity::getId, ShopTemplateEntity::getName, (k1, k2) -> k1));
                shopCategoryEntityList.forEach(shopCategoryEntity -> shopCategoryEntity.setTemplateName(shopTemplateEntityMap.getOrDefault(shopCategoryEntity.getTemplateId(), DelimiterConstants.EMPTY_STR)));
            }
        }
        // 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(iPage));
    }

}
