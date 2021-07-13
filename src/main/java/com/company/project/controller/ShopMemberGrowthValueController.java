package com.company.project.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.company.project.service.ShopMemberGrowthValueService;

import javax.annotation.Resource;


/**
 * 会员成长值
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-09 17:10:25
 */
@Api(tags = "会员成长值")
@Controller
@RequestMapping("/")
public class ShopMemberGrowthValueController extends BaseController {

    @Resource
    private ShopMemberGrowthValueService shopMemberGrowthValueService;



}
