package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.TaskRewardSettingsMapper;
import com.company.project.entity.TaskRewardSettingsEntity;
import com.company.project.service.TaskRewardSettingsService;


@Service("taskRewardSettingsService")
public class TaskRewardSettingsServiceImpl extends ServiceImpl<TaskRewardSettingsMapper, TaskRewardSettingsEntity> implements TaskRewardSettingsService {


}