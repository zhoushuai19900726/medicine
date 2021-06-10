package com.company.project.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * HomeRespVO
 *
 * @author
 * @version V1.0
 * @date 2020年3月18日
 */
@Data
public class HomeRespVO {
    @ApiModelProperty(value = "用户信息")
    private UserInfoRespVO userInfo;
    @ApiModelProperty(value = "首页信息")
    private HomeInfoRespVO homeInfo;
    @ApiModelProperty(value = "LOGO信息")
    private LogoInfoRespVO logoInfo;
    @ApiModelProperty(value = "菜单信息")
    private List<MenuInfoRespVO> menuInfo;

}
