package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.aop.annotation.DataScope;
import com.company.project.common.aop.annotation.LogAnnotation;
import com.company.project.entity.ShopAlbumGalleryEntity;
import com.company.project.service.ShopAlbumGalleryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import com.company.project.common.utils.DataResult;

import com.company.project.entity.ShopAlbumEntity;
import com.company.project.service.ShopAlbumService;

import javax.annotation.Resource;


/**
 * 相册
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-25 14:17:03
 */
@Controller
@RequestMapping("/")
public class ShopAlbumController extends BaseController {

    @Resource
    private ShopAlbumService shopAlbumService;

    @Resource
    private ShopAlbumGalleryService shopAlbumGalleryService;


    @ApiOperation(value = "跳转到相册列表页面")
    @GetMapping("/index/shopAlbum")
    public String shopAlbum() {
        return "album/shopAlbumList";
    }

    @ApiOperation(value = "跳转到图库列表页面")
    @GetMapping("/index/shopAlbum/shopAlbumGallery")
    public String shopAlbumGallery() {
        return "album/shopAlbumGalleryList";
    }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopAlbum/addOrUpdate")
    public String addOrUpdate() {
        return "album/addOrUpdate";
    }

    // =========================================================================== 相册 ===========================================================================  //

    @ApiOperation(value = "新增")
    @PostMapping("shopAlbum/add")
    @RequiresPermissions("shopAlbum:add")
    @LogAnnotation(title = "相册", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopAlbumEntity shopAlbum) {
        return DataResult.success(shopAlbumService.save(shopAlbum));
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopAlbum/delete")
    @RequiresPermissions("shopAlbum:delete")
    @LogAnnotation(title = "相册", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return DataResult.success(shopAlbumService.removeByIds(ids));
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopAlbum/update")
    @RequiresPermissions("shopAlbum:update")
    @LogAnnotation(title = "相册", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopAlbumEntity shopAlbum) {
        return DataResult.success(shopAlbumService.updateById(shopAlbum));
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("shopAlbum/listByAll")
    @RequiresPermissions("shopAlbum:list")
    @LogAnnotation(title = "相册", action = "查询全部")
    @ResponseBody
    public DataResult findListByAll() {
        return DataResult.success(shopAlbumService.list());
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopAlbum/listByPage")
    @RequiresPermissions("shopAlbum:list")
    @LogAnnotation(title = "相册", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopAlbumEntity shopAlbum) {
        // 分页初始化
        Page<ShopAlbumEntity> page = new Page<>(shopAlbum.getPage(), shopAlbum.getLimit());
        // 查询条件
        LambdaQueryWrapper<ShopAlbumEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopAlbum.getId()), ShopAlbumEntity::getId, shopAlbum.getId())
                .orderByAsc(ShopAlbumEntity::getSeq);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopAlbumService.page(page, encapsulationDataRights(shopAlbum, queryWrapper, ShopAlbumEntity::getCreateId))));
    }


    // =========================================================================== 相册图片 ===========================================================================  //

    @ApiOperation(value = "新增")
    @PostMapping("shopAlbum/addDetail")
    @RequiresPermissions("shopAlbum:add")
    @LogAnnotation(title = "相册图片", action = "新增")
    @ResponseBody
    public DataResult addDetail(@RequestBody ShopAlbumGalleryEntity shopAlbumGallery) {
        return DataResult.success(shopAlbumGalleryService.save(shopAlbumGallery));
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopAlbum/deleteDetail")
    @RequiresPermissions("shopAlbum:delete")
    @LogAnnotation(title = "相册图片", action = "删除")
    @ResponseBody
    public DataResult deleteDetail(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return DataResult.success(shopAlbumGalleryService.removeByIds(ids));
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopAlbum/updateDetail")
    @RequiresPermissions("shopAlbum:update")
    @LogAnnotation(title = "相册图片", action = "更新")
    @ResponseBody
    public DataResult updateDetail(@RequestBody ShopAlbumGalleryEntity shopAlbumGallery) {
        return DataResult.success(shopAlbumGalleryService.updateById(shopAlbumGallery));
    }


    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopAlbumGallery/findDetailListByPage")
    @RequiresPermissions("shopAlbum:list")
    @LogAnnotation(title = "相册图片", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findDetailListByPage(@RequestBody ShopAlbumGalleryEntity shopAlbumGallery) {
        // 分页初始化
        Page<ShopAlbumGalleryEntity> page = new Page<>(shopAlbumGallery.getPage(), shopAlbumGallery.getLimit());
        // 查询条件
        LambdaQueryWrapper<ShopAlbumGalleryEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopAlbumGallery.getAlbumId()), ShopAlbumGalleryEntity::getAlbumId, shopAlbumGallery.getAlbumId())
                .orderByAsc(ShopAlbumGalleryEntity::getSeq);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopAlbumGalleryService.page(page, encapsulationDataRights(shopAlbumGallery, queryWrapper, ShopAlbumGalleryEntity::getCreateId))));
    }


}
