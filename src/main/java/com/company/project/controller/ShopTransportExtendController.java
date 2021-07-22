package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.aop.annotation.DataScope;
import com.company.project.common.aop.annotation.LogAnnotation;
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

import com.company.project.entity.ShopTransportExtendEntity;
import com.company.project.service.ShopTransportExtendService;

import javax.annotation.Resource;


/**
 * 运费模板扩展
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-22 16:41:53
 */
@Api(tags = "运费模板扩展")
@Controller
@RequestMapping("/")
public class ShopTransportExtendController extends BaseController {

    @Resource
    private ShopTransportExtendService shopTransportExtendService;


//    @ApiOperation(value = "跳转到运费模板扩展列表页面")
//    @GetMapping("/index/shopTransportExtend")
//    public String shopTransportExtend() {
//        return "shoptransportextend/list";
//        }
//
//    @ApiOperation(value = "跳转进入新增/编辑页面")
//    @GetMapping("/index/shopTransportExtend/addOrUpdate")
//    public String addOrUpdate() {
//        return "shoptransportextend/addOrUpdate";
//    }
//
//    @ApiOperation(value = "新增")
//    @PostMapping("shopTransportExtend/add")
//    @RequiresPermissions("shopTransportExtend:add")
//    @LogAnnotation(title = "运费模板扩展", action = "新增")
//    @ResponseBody
//    public DataResult add(@RequestBody ShopTransportExtendEntity shopTransportExtend){
//        return DataResult.success(shopTransportExtendService.save(shopTransportExtend));
//    }
//
//    @ApiOperation(value = "删除")
//    @DeleteMapping("shopTransportExtend/delete")
//    @RequiresPermissions("shopTransportExtend:delete")
//    @LogAnnotation(title = "运费模板扩展", action = "删除")
//    @ResponseBody
//    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
//        return DataResult.success(shopTransportExtendService.removeByIds(ids));
//    }
//
//    @ApiOperation(value = "更新")
//    @PutMapping("shopTransportExtend/update")
//    @RequiresPermissions("shopTransportExtend:update")
//    @LogAnnotation(title = "运费模板扩展", action = "更新")
//    @ResponseBody
//    public DataResult update(@RequestBody ShopTransportExtendEntity shopTransportExtend){
//        return DataResult.success(shopTransportExtendService.updateById(shopTransportExtend));
//    }
//
//    @ApiOperation(value = "查询全部")
//    @GetMapping("shopTransportExtend/listByAll")
//    @RequiresPermissions("shopTransportExtend:list")
//    @LogAnnotation(title = "运费模板扩展", action = "查询全部")
//    @ResponseBody
//    public DataResult findListByAll() {
//        return DataResult.success(shopTransportExtendService.list());
//    }
//
//    @ApiOperation(value = "查询分页数据")
//    @PostMapping("shopTransportExtend/listByPage")
//    @RequiresPermissions("shopTransportExtend:list")
//    @LogAnnotation(title = "运费模板扩展", action = "查询分页数据")
//    @DataScope
//    @ResponseBody
//    public DataResult findListByPage(@RequestBody ShopTransportExtendEntity shopTransportExtend){
//        // 查询条件
//        LambdaQueryWrapper<ShopTransportExtendEntity> queryWrapper = Wrappers.lambdaQuery();
//        queryWrapper
//                .eq(StringUtils.isNotBlank(shopTransportExtend.getId()), ShopTransportExtendEntity::getId, shopTransportExtend.getId())
//        // .apply(StringUtils.isNotBlank(shopTransportExtend.getCreateStartTime()), "UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + shopTransportExtend.getCreateStartTime() + "')")
//        // .apply(StringUtils.isNotBlank(shopTransportExtend.getCreateEndTime()), "UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP('" + shopTransportExtend.getCreateEndTime() + "')")
//                .orderByDesc(ShopTransportExtendEntity::getCreateTime);
//        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
//        return DataResult.success(encapsulationUser(shopTransportExtendService.page(new Page<>(shopTransportExtend.getPage(), shopTransportExtend.getLimit()), encapsulationDataRights(shopTransportExtend, queryWrapper, ShopTransportExtendEntity::getCreateId))));
//    }

}
