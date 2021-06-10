package com.company.project.service.impl;

import com.company.project.entity.SysDept;
import com.company.project.entity.SysUser;
import com.company.project.mapper.SysDeptMapper;
import com.company.project.mapper.SysPermissionMapper;
import com.company.project.mapper.SysUserMapper;
import com.company.project.service.DeptService;
import com.company.project.service.HomeService;
import com.company.project.service.PermissionService;
import com.company.project.service.UserService;
import com.company.project.vo.resp.*;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 首页
 *
 * @author wenbin
 * @version V1.0
 * @date 2020年3月18日
 */
@Service
public class HomeServiceImpl implements HomeService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private PermissionService permissionService;

    /**
     * 获取首页数据
     *
     * @param userId userId
     * @return
     */
    @Override
    public HomeRespVO getHomeInfo(String userId) {
        // 返回结果
        HomeRespVO homeRespVO = new HomeRespVO();
        // 首页信息
        HomeInfoRespVO homeInfoRespVO = new HomeInfoRespVO();
        homeInfoRespVO.setTitle("首页");
        homeInfoRespVO.setHref("/index/main");
        homeRespVO.setHomeInfo(homeInfoRespVO);
        // LOGO信息
        LogoInfoRespVO logoInfoRespVO = new LogoInfoRespVO();
        logoInfoRespVO.setTitle("LAYUI MINI");
        logoInfoRespVO.setImage("../layui2.0/images/logo.png");
        logoInfoRespVO.setHref("");
        homeRespVO.setLogoInfo(logoInfoRespVO);
        // 用户信息
        UserInfoRespVO userInfoRespVO = new UserInfoRespVO();
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (Objects.nonNull(sysUser)) {
            BeanUtils.copyProperties(sysUser, userInfoRespVO);
            SysDept sysDept = sysDeptMapper.selectById(sysUser.getDeptId());
            if (Objects.nonNull(sysDept)) {
                userInfoRespVO.setDeptId(sysDept.getId());
                userInfoRespVO.setDeptName(sysDept.getName());
            }
        }
        homeRespVO.setUserInfo(userInfoRespVO);
        // 菜单信息
        List<MenuInfoRespVO> menuInfoRespVOList = Lists.newArrayList();
        List<PermissionRespNode> permissionRespNodeList = permissionService.permissionTreeList(userId);
        if (CollectionUtils.isNotEmpty(permissionRespNodeList)) {
            // 递归无限级子菜单
            permissionRespNodeList.forEach(permissionRespNode -> menuInfoRespVOList.add(infiniteSubmenu(permissionRespNode)));
        }
        homeRespVO.setMenuInfo(menuInfoRespVOList);
        return homeRespVO;
    }

    /**
     * 无限级子菜单
     *
     * @param permissionRespNode
     */
    private MenuInfoRespVO infiniteSubmenu(PermissionRespNode permissionRespNode) {
        MenuInfoRespVO menuInfoRespVO = new MenuInfoRespVO();
        menuInfoRespVO.setTitle(permissionRespNode.getTitle());
        menuInfoRespVO.setHref(StringUtils.isNotBlank(permissionRespNode.getUrl()) ? permissionRespNode.getUrl() : StringUtils.EMPTY);
        menuInfoRespVO.setIcon(permissionRespNode.getIcon());
        menuInfoRespVO.setTarget(permissionRespNode.getTarget());
        if(CollectionUtils.isNotEmpty(permissionRespNode.getChildren())){
            List<MenuInfoRespVO> children = Lists.newArrayList();
            permissionRespNode.getChildren().forEach(permissionRespNodeChild -> children.add(infiniteSubmenu(permissionRespNodeChild)));
            menuInfoRespVO.setChild(children);
        }
        return menuInfoRespVO;
    }


}
