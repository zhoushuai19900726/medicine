package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.company.project.common.utils.Constant;
import com.company.project.entity.SysUser;
import com.company.project.service.HttpSessionService;
import com.company.project.service.UserService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


/**
 * 公共Controller
 *
 * @author zhoushuai
 * @version V1.0
 * @date 2020年3月18日
 */
@Controller
public class BaseController {

    @Resource
    private UserService userService;

    @Resource
    private HttpSessionService httpSessionService;

    /**
     * 封装用户信息
     *
     * @param iPage
     * @param <T>
     * @return
     */
    protected <T> IPage<T> encapsulationUser(IPage<T> iPage) {
        List<T> list = iPage.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            // 提取userId
            List<String> userIdList = getUserIdList(list);
            if (CollectionUtils.isNotEmpty(userIdList)) {
                // 查询用户信息
                List<SysUser> sysUserList = userService.listByIds(userIdList);
                if (CollectionUtils.isNotEmpty(sysUserList)) {
                    Map<String, String> sysUserMap = sysUserList.stream().collect(Collectors.toMap(SysUser::getId, SysUser::getUsername, (k1, k2) -> k1));
                    setUserName(list, sysUserMap);
                }
            }
        }
        return iPage;
    }

    private <T> List<String> getUserIdList(List<T> list) {
        List<String> userIdList = Lists.newArrayList();
        list.forEach(t -> {
            userIdList.add(getUserId(t, "createId"));
            userIdList.add(getUserId(t, "updateId"));
        });
        return userIdList;
    }

    private <T> String getUserId(T t, String fieldName) {
        AtomicReference<String> userId = new AtomicReference<>();
        Class<?> tClass = t.getClass();
        // 得到所有private属性
        List<Field> fieldList = Arrays.asList(tClass.getDeclaredFields());
        fieldList.forEach(aField -> {
            if (StringUtils.equals(fieldName, aField.getName())) {
                // 设置可以访问私有变量
                aField.setAccessible(true);
                String name = aField.getName();
                // 将属性名字的首字母大写
                name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
                try {
                    // 整合出 get() 属性这个方法
                    userId.set(String.valueOf(t.getClass().getMethod("get" + name).invoke(t)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return userId.get();
    }

    private <T> void setUserName(List<T> list, Map<String, String> sysUserMap) {
        list.forEach(t -> {
            String createId = getUserId(t, "createId");
            if (StringUtils.isNotBlank(createId)) {
                setUserName(t, sysUserMap.getOrDefault(createId, StringUtils.EMPTY), "createName");
            }
            String updateId = getUserId(t, "updateId");
            if (StringUtils.isNotBlank(updateId)) {
                setUserName(t, sysUserMap.getOrDefault(updateId, StringUtils.EMPTY), "updateName");
            }
        });
    }

    private <T> void setUserName(T t, String userName, String fieldName) {
        Class<?> tClass = t.getClass();
        //得到所有private属性
        List<Field> fieldList = Arrays.asList(tClass.getDeclaredFields());
        fieldList.forEach(aField -> {
            if (StringUtils.equals(aField.getName(), fieldName)) {
                //设置可以访问私有变量
                aField.setAccessible(true);
                try {
                    aField.set(t, userName);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 封装数据权限
     *
     * @param t
     * @param queryWrapper
     * @param <T>
     */
    protected <T> LambdaQueryWrapper<T> encapsulationDataRights(T t, LambdaQueryWrapper<T> queryWrapper, SFunction<T, ?> fn) {
        // 当前登录用户ID
        String userId = httpSessionService.getCurrentUserId();
        // 数据权限
        if (!StringUtils.equals(Constant.ADMIN_ID, userId)) {
            List<String> createIds = getCreateIds(t);
            if (CollectionUtils.isEmpty(createIds)) {
                createIds = Lists.newArrayList(userId);
            }
            queryWrapper.in(fn, createIds);
        }
        return queryWrapper;
    }

    private <T> List<String> getCreateIds(T t) {
        AtomicReference<List<String>> createIds = new AtomicReference<>();
        Class<?> tClass = t.getClass();
        //得到所有private属性
        List<Field> fieldList = Lists.newArrayList();
        //当父类为null的时候说明到达了最上层的父类(Object类).
        while (Objects.nonNull(tClass) && !StringUtils.equals(tClass.getName().toLowerCase(), "java.lang.object")) {
            fieldList.addAll(Arrays.asList(tClass.getDeclaredFields()));
            // 得到父类,然后赋给自己
            tClass = tClass.getSuperclass();
        }
        fieldList.forEach(aField -> {
            if (StringUtils.equals(aField.getName(), "createIds")) {
                //设置可以访问私有变量
                aField.setAccessible(true);
                String name = aField.getName();
                //将属性名字的首字母大写
                name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
                try {
                    //整合出 get() 属性这个方法
                    createIds.set((List<String>) (t.getClass().getMethod("get" + name).invoke(t)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return createIds.get();
    }

    private <T> void setCreateIds(T t, List<String> createIds) {
        Class<?> tClass = t.getClass();
        //得到所有private属性
        List<Field> fieldList = Arrays.asList(tClass.getDeclaredFields());
        fieldList.forEach(aField -> {
            if (StringUtils.equals(aField.getName(), "createIds")) {
                //设置可以访问私有变量
                aField.setAccessible(true);
                try {
                    aField.set(t, createIds);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
