package com.company.project.controller;

import com.company.project.common.aop.annotation.LogAnnotation;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopMemberEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.company.project.service.ShopRecommendationRelationshipService;

import javax.annotation.Resource;


/**
 * 会员推荐关系表
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-09 15:40:52
 */
@Api(tags = "会员推荐关系表")
@Controller
@RequestMapping("/")
public class ShopRecommendationRelationshipController extends BaseController {

    @Resource
    private ShopRecommendationRelationshipService shopRecommendationRelationshipService;


    @ApiOperation(value = "根据顶点查询推荐关系")
    @GetMapping("shopRecommendationRelationship/recommendationRelationship")
    @RequiresPermissions("shopMember:list")
    @LogAnnotation(title = "推荐关系", action = "根据顶点查询推荐关系")
    @ResponseBody
    public DataResult recommendationRelationship(@RequestParam(value = "memberName") String memberName, @RequestParam(value = "direction") Integer direction) {
        return DataResult.success(shopRecommendationRelationshipService.recommendationRelationship(memberName, direction));
    }




}
