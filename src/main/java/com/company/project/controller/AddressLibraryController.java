package com.company.project.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.aop.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import com.company.project.common.utils.DataResult;

import com.company.project.entity.AddressLibraryEntity;
import com.company.project.service.AddressLibraryService;

import javax.annotation.Resource;


/**
 * 地址库
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-14 11:56:12
 */
@Api(tags = "地址库")
@Controller
@RequestMapping("/")
public class AddressLibraryController extends BaseController {

    @Resource
    private AddressLibraryService addressLibraryService;


    @ApiOperation(value = "跳转到地址库列表页面")
    @GetMapping("/index/addressLibrary")
    public String addressLibrary() {
        return "addresslibrary/addressLibraryList";
    }

    @ApiOperation(value = "新增")
    @PostMapping("addressLibrary/add")
    @RequiresPermissions("addressLibrary:add")
    @LogAnnotation(title = "地址库", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody AddressLibraryEntity addressLibrary) {
        return addressLibraryService.saveAddressLibraryEntity(addressLibrary);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("addressLibrary/delete")
    @RequiresPermissions("addressLibrary:delete")
    @LogAnnotation(title = "地址库", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> idList) {
        return addressLibraryService.removeByIdlist(idList);
    }

    @ApiOperation(value = "更新")
    @PutMapping("addressLibrary/update")
    @RequiresPermissions("addressLibrary:update")
    @LogAnnotation(title = "地址库", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody AddressLibraryEntity addressLibrary) {
        return addressLibraryService.updateAddressLibraryEntityById(addressLibrary);
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("addressLibrary/listByPage")
    @RequiresPermissions("addressLibrary:list")
    @LogAnnotation(title = "地址库", action = "查询分页数据")
    @ResponseBody
    public DataResult findListByPage(@RequestBody AddressLibraryEntity addressLibrary) {
        return DataResult.success(addressLibraryService.page(new Page<>(addressLibrary.getPage(), addressLibrary.getLimit()), Wrappers.<AddressLibraryEntity>lambdaQuery().eq(AddressLibraryEntity::getParentId, addressLibrary.getParentId()).orderByAsc(AddressLibraryEntity::getSeq)));
    }

}
