package com.company.project.controller;

import com.company.project.common.aop.annotation.LogAnnotation;
import com.company.project.common.utils.NumberConstants;
import io.swagger.annotations.Api;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import io.swagger.annotations.ApiOperation;
import com.company.project.common.utils.DataResult;

import com.company.project.entity.TaskRewardSettingsEntity;
import com.company.project.service.TaskRewardSettingsService;

import javax.annotation.Resource;
import java.util.List;


/**
 * 任务奖励设置
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-13 17:59:56
 */
@Api(tags = "任务奖励设置")
@Controller
@RequestMapping("/")
public class TaskRewardSettingsController extends BaseController {

    @Resource
    private TaskRewardSettingsService taskRewardSettingsService;

    @ApiOperation(value = "跳转进入任务奖励设置页面")
    @RequiresPermissions("taskRewardSettings:list")
    @GetMapping("/index/taskRewardSettings")
    public String taskRewardSettings(Model model) {
        List<TaskRewardSettingsEntity> taskRewardSettingsEntityList = taskRewardSettingsService.list();
        model.addAttribute("taskRewardSettingsEntity", CollectionUtils.isNotEmpty(taskRewardSettingsEntityList) ? taskRewardSettingsEntityList.get(NumberConstants.ZERO) : new TaskRewardSettingsEntity());
        return "member/taskRewardSettings";
    }

    @ApiOperation(value = "新增/修改")
    @PostMapping("taskRewardSettings/addOrUpdate")
    @RequiresPermissions(value = {"taskRewardSettings:add", "taskRewardSettings:update"}, logical = Logical.OR)
    @LogAnnotation(title = "任务奖励设置", action = "新增/修改")
    @ResponseBody
    public DataResult addOrUpdate(@RequestBody TaskRewardSettingsEntity taskRewardSettings){
        return DataResult.success(taskRewardSettingsService.saveOrUpdate(taskRewardSettings));
    }


}
