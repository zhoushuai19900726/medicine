package com.company.project.controller;

import io.swagger.annotations.Api;
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




}
