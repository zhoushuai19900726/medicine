package com.company.project.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * MenuInfoRespVO
 *
 * @author
 * @version V1.0
 * @date 2020年3月18日
 */
@Data
public class MenuInfoRespVO {
    @ApiModelProperty(value = "菜单标题")
    private String title;
    @ApiModelProperty(value = "菜单跳转链接")
    private String href;
    @ApiModelProperty(value = "菜单图标")
    private String icon;
    @ApiModelProperty(value = "菜单跳转方式")
    private String target;
    @ApiModelProperty(value = "子菜单")
    private List<MenuInfoRespVO> child;
}
