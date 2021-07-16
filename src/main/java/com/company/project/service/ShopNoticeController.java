package com.company.project.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.aop.annotation.DataScope;
import com.company.project.common.aop.annotation.LogAnnotation;
import com.company.project.controller.BaseController;
import io.swagger.annotations.Api;
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

import com.company.project.entity.ShopNoticeEntity;
import com.company.project.service.ShopNoticeService;

import javax.annotation.Resource;


/**
 * 公告
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-16 13:56:11
 */
@Api(tags = "公告")
@Controller
@RequestMapping("/")
public class ShopNoticeController extends BaseController {

    @Resource
    private ShopNoticeService shopNoticeService;


    @ApiOperation(value = "跳转到公告列表页面")
    @GetMapping("/index/shopNotice")
    public String shopNotice() {
        return "notice/noticeList";
    }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopNotice/addOrUpdate")
    public String addOrUpdate() {
        return "notice/addOrUpdate";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopNotice/add")
    @RequiresPermissions("shopNotice:add")
    @LogAnnotation(title = "公告", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopNoticeEntity shopNotice) {
        return DataResult.success(shopNoticeService.save(shopNotice));
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopNotice/delete")
    @RequiresPermissions("shopNotice:delete")
    @LogAnnotation(title = "公告", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return DataResult.success(shopNoticeService.removeByIds(ids));
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopNotice/update")
    @RequiresPermissions("shopNotice:update")
    @LogAnnotation(title = "公告", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopNoticeEntity shopNotice) {
        return DataResult.success(shopNoticeService.updateById(shopNotice));
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("shopNotice/listByAll")
    @RequiresPermissions("shopNotice:list")
    @LogAnnotation(title = "公告", action = "查询全部")
    @ResponseBody
    public DataResult findListByAll() {
        return DataResult.success(shopNoticeService.list());
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopNotice/listByPage")
    @RequiresPermissions("shopNotice:list")
    @LogAnnotation(title = "公告", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopNoticeEntity shopNotice) {
        // 查询条件
        LambdaQueryWrapper<ShopNoticeEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopNotice.getId()), ShopNoticeEntity::getId, shopNotice.getId())
                .like(StringUtils.isNotBlank(shopNotice.getTitle()), ShopNoticeEntity::getTitle, shopNotice.getTitle())
                .apply(StringUtils.isNotBlank(shopNotice.getCreateStartTime()), "UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + shopNotice.getCreateStartTime() + "')")
                .apply(StringUtils.isNotBlank(shopNotice.getCreateEndTime()), "UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP('" + shopNotice.getCreateEndTime() + "')")
                .orderByAsc(ShopNoticeEntity::getSeq);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopNoticeService.page(new Page<>(shopNotice.getPage(), shopNotice.getLimit()), encapsulationDataRights(shopNotice, queryWrapper, ShopNoticeEntity::getCreateId))));
    }

}
