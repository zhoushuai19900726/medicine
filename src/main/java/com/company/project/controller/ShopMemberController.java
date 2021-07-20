package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.aop.annotation.LogAnnotation;
import com.company.project.common.utils.DownFileUtil;
import com.company.project.common.utils.SystemConstants;
import com.company.project.entity.ShopMemberGrowthValueEntity;
import com.company.project.entity.ShopMemberWalletEntity;
import com.company.project.service.ShopMemberGrowthValueService;
import com.company.project.service.ShopMemberWalletService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.commons.lang.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;
import java.util.Objects;

import com.company.project.common.utils.DataResult;

import com.company.project.entity.ShopMemberEntity;
import com.company.project.service.ShopMemberService;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;


/**
 * 会员
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-08 13:59:46
 */
@Api(tags = "会员")
@Controller
@RequestMapping("/")
public class ShopMemberController extends BaseController {

    @Resource
    private ShopMemberService shopMemberService;

    @Resource
    private ShopMemberWalletService shopMemberWalletService;

    @Resource
    private ShopMemberGrowthValueService shopMemberGrowthValueService;

    @ApiOperation(value = "跳转到会员列表页面")
    @GetMapping("/index/shopMember")
    public String shopMember() {
        return "member/memberList";
    }

    @ApiOperation(value = "跳转到会员成长值列表页面")
    @GetMapping("/index/shopMember/growthValueList")
    public String growthValueList() {
        return "member/growthValueList";
    }

    @ApiOperation(value = "跳转到注销会员列表页面")
    @GetMapping("/index/shopMember/logoutList")
    public String shopLogoutMember() {
        return "member/logoutList";
    }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopMember/addOrUpdate")
    public String addOrUpdate() {
        return "member/addOrUpdate";
    }

    @ApiOperation(value = "跳转进入推荐关系页面")
    @GetMapping("/index/shopMember/recommend")
    public String recommend() {
        return "member/recommend";
    }

    @ApiOperation(value = "跳转进入详情页面")
    @GetMapping("/index/shopMember/detail/{memberId}")
    public String detail(@PathVariable("memberId") String memberId, Model model) {
        // 会员信息
        ShopMemberEntity shopMemberEntity = shopMemberService.findOneByMemberId(memberId);
        model.addAttribute("shopMemberEntity", shopMemberEntity);
        // 推荐人信息
        model.addAttribute("references", shopMemberService.findOneByMemberId(shopMemberEntity.getMemberFrom()));
        // 钱包余额
        model.addAttribute("shopMemberWalletEntity", shopMemberWalletService.getOne(Wrappers.<ShopMemberWalletEntity>lambdaQuery().in(ShopMemberWalletEntity::getMemberId, memberId)));
        // 成长值
        model.addAttribute("shopMemberGrowthValueEntity", shopMemberGrowthValueService.getOne(Wrappers.<ShopMemberGrowthValueEntity>lambdaQuery().in(ShopMemberGrowthValueEntity::getMemberId, memberId)));
        return "member/memberDetail";
    }

    @ApiOperation(value = "跳转进入消费记录页面")
    @GetMapping("/index/shopMember/walletRecord/{memberId}")
    public String walletRecord(@PathVariable("memberId") String memberId, Model model) {
        // 会员信息
        model.addAttribute("shopMemberEntity", shopMemberService.findOneByMemberId(memberId));
        // 钱包余额
        model.addAttribute("shopMemberWalletEntity", shopMemberWalletService.getOne(Wrappers.<ShopMemberWalletEntity>lambdaQuery().in(ShopMemberWalletEntity::getMemberId, memberId)));
        return "member/walletRecord";
    }

    @ApiOperation(value = "跳转进入成长值记录页面")
    @GetMapping("/index/shopMember/growthValueRecord/{memberId}")
    public String growthValueRecord(@PathVariable("memberId") String memberId, Model model) {
        // 会员信息
        model.addAttribute("shopMemberEntity", shopMemberService.findOneByMemberId(memberId));
        // 成长值
        model.addAttribute("shopMemberGrowthValueEntity", shopMemberGrowthValueService.getOne(Wrappers.<ShopMemberGrowthValueEntity>lambdaQuery().in(ShopMemberGrowthValueEntity::getMemberId, memberId)));
        return "member/growthValueRecord";
    }

    @ApiOperation(value = "跳转进入赠送订单页面")
    @GetMapping("/index/shopMember/freeOrder/{memberId}")
    public String freeOrder(@PathVariable("memberId") String memberId, Model model) {
        // 会员信息
        model.addAttribute("shopMemberEntity", shopMemberService.findOneByMemberId(memberId));
        // 钱包余额
        model.addAttribute("shopMemberWalletEntity", shopMemberWalletService.getOne(Wrappers.<ShopMemberWalletEntity>lambdaQuery().in(ShopMemberWalletEntity::getMemberId, memberId)));
        // TODO 订单列表

        return "member/freeOrder";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopMember/add")
    @RequiresPermissions("shopMember:add")
    @LogAnnotation(title = "会员", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopMemberEntity shopMember) {
        return shopMemberService.saveShopMemberEntity(shopMember);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopMember/delete")
    @RequiresPermissions("shopMember:delete")
    @LogAnnotation(title = "会员", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return shopMemberService.removeByMemberIds(ids);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopMember/absolutelyDelete")
    @RequiresPermissions("shopMember:delete")
    @LogAnnotation(title = "会员", action = "物理删除")
    @ResponseBody
    public DataResult absolutelyDelete(@RequestBody @ApiParam(value = "id集合") List<String> memberIdList) {
        return shopMemberService.absolutelyDelete(memberIdList);
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopMember/update")
    @RequiresPermissions("shopMember:update")
    @LogAnnotation(title = "会员", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopMemberEntity shopMember) {
        return shopMemberService.updateShopMemberEntityById(shopMember);
    }

    @ApiOperation(value = "撤销注销")
    @PutMapping("shopMember/revoke")
    @RequiresPermissions("shopMember:update")
    @LogAnnotation(title = "会员", action = "撤销注销")
    @ResponseBody
    public DataResult revoke(@RequestBody ShopMemberEntity shopMember) {
        return shopMemberService.revokeShopMemberEntityById(shopMember);
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("shopMember/listByAll")
    @RequiresPermissions("shopMember:list")
    @LogAnnotation(title = "会员", action = "查询全部")
    @ResponseBody
    public DataResult findListByAll() {
        return DataResult.success(shopMemberService.list());
    }


    @ApiOperation(value = "根据账号查询")
    @GetMapping("shopMember/findOneByMemberName")
    @RequiresPermissions("shopMember:list")
    @LogAnnotation(title = "会员", action = "查询全部")
    @ResponseBody
    public DataResult findOneByMemberName(ShopMemberEntity shopMemberEntity) {
        return DataResult.success(shopMemberService.findOneByMemberName(shopMemberEntity));
    }

    @ApiOperation(value = "根据邀请码查询")
    @GetMapping("shopMember/findOneByInvitationCode")
    @RequiresPermissions("shopMember:list")
    @LogAnnotation(title = "会员", action = "查询全部")
    @ResponseBody
    public DataResult findOneByInvitationCode(ShopMemberEntity shopMemberEntity) {
        return DataResult.success(shopMemberService.findOneByInvitationCode(shopMemberEntity));
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopMember/listByPage")
    @RequiresPermissions("shopMember:list")
    @LogAnnotation(title = "会员", action = "查询分页数据")
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopMemberEntity shopMember) {
        // 查询条件
        LambdaQueryWrapper<ShopMemberEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopMember.getMemberName()), ShopMemberEntity::getMemberName, shopMember.getMemberName())
                .eq(StringUtils.isNotBlank(shopMember.getMemberMobile()), ShopMemberEntity::getMemberMobile, shopMember.getMemberMobile())
                .eq(StringUtils.isNotBlank(shopMember.getMemberInvitationCode()), ShopMemberEntity::getMemberInvitationCode, shopMember.getMemberInvitationCode())
                .orderByDesc(ShopMemberEntity::getCreateTime);
        return DataResult.success(shopMemberService.listByPage(new Page<>(shopMember.getPage(), shopMember.getLimit()), queryWrapper));
    }

    @ApiOperation(value = "查询注销分页数据")
    @PostMapping("shopMember/logoutListByPage")
    @RequiresPermissions("shopMember:list")
    @LogAnnotation(title = "注销会员", action = "查询注销分页数据")
    @ResponseBody
    public DataResult findLogoutListByPage(@RequestBody ShopMemberEntity shopMember) {
        // 查询条件
        LambdaQueryWrapper<ShopMemberEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopMember.getMemberName()), ShopMemberEntity::getMemberName, shopMember.getMemberName())
                .eq(StringUtils.isNotBlank(shopMember.getMemberMobile()), ShopMemberEntity::getMemberMobile, shopMember.getMemberMobile())
                .eq(StringUtils.isNotBlank(shopMember.getMemberInvitationCode()), ShopMemberEntity::getMemberInvitationCode, shopMember.getMemberInvitationCode())
                .orderByDesc(ShopMemberEntity::getCreateTime);
        return DataResult.success(shopMemberService.logoutListByPage(new Page<>(shopMember.getPage(), shopMember.getLimit()), queryWrapper));
    }

    @ApiOperation(value = "下载导入模板")
    @GetMapping(value = "shopMember/downloadImportTemplate")
    @LogAnnotation(title = "会员", action = "下载导入模板")
    public void downloadImportTemplate(HttpServletResponse response) {
        DownFileUtil.downFileByPath(response, SystemConstants.importExport + "/会员模板(批量导入、批量撤销、批量删除).xlsx");
    }

    @ApiOperation(value = "导入模板")
    @PostMapping("shopMember/upload")
    @RequiresPermissions("shopMember:add")
    @LogAnnotation(title = "会员", action = "导入模板")
    @ResponseBody
    public DataResult batchImport(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "type") Integer type) {
        return shopMemberService.saveFile(file, type);
    }

}
