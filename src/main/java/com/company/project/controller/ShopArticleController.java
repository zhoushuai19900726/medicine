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

import com.company.project.entity.ShopArticleEntity;
import com.company.project.service.ShopArticleService;

import javax.annotation.Resource;


/**
 * 文章
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-27 13:56:46
 */
@Api(tags = "文章")
@Controller
@RequestMapping("/")
public class ShopArticleController extends BaseController {

    @Resource
    private ShopArticleService shopArticleService;

    @ApiOperation(value = "跳转到文章列表页面")
    @GetMapping("/index/shopArticle")
    public String shopArticle() {
        return "article/articleList";
        }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopArticle/addOrUpdate")
    public String addOrUpdate() {
        return "article/addOrUpdate";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopArticle/add")
    @RequiresPermissions("shopArticle:add")
    @LogAnnotation(title = "文章", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopArticleEntity shopArticle){
        return DataResult.success(shopArticleService.save(shopArticle));
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopArticle/delete")
    @RequiresPermissions("shopArticle:delete")
    @LogAnnotation(title = "文章", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        return DataResult.success(shopArticleService.removeByIds(ids));
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopArticle/update")
    @RequiresPermissions("shopArticle:update")
    @LogAnnotation(title = "文章", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopArticleEntity shopArticle){
        return DataResult.success(shopArticleService.updateById(shopArticle));
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("shopArticle/listByAll")
    @RequiresPermissions("shopArticle:list")
    @LogAnnotation(title = "文章", action = "查询全部")
    @ResponseBody
    public DataResult findListByAll() {
        return DataResult.success(shopArticleService.list());
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopArticle/listByPage")
    @RequiresPermissions("shopArticle:list")
    @LogAnnotation(title = "文章", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopArticleEntity shopArticle){
        // 查询条件
        LambdaQueryWrapper<ShopArticleEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopArticle.getId()), ShopArticleEntity::getId, shopArticle.getId())
        // .apply(StringUtils.isNotBlank(shopArticle.getCreateStartTime()), "UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + shopArticle.getCreateStartTime() + "')")
        // .apply(StringUtils.isNotBlank(shopArticle.getCreateEndTime()), "UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP('" + shopArticle.getCreateEndTime() + "')")
                .orderByDesc(ShopArticleEntity::getCreateTime);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopArticleService.page(new Page<>(shopArticle.getPage(), shopArticle.getLimit()), encapsulationDataRights(shopArticle, queryWrapper, ShopArticleEntity::getCreateId))));
    }

}
