package com.company.project.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * LogoInfoRespVO
 *
 * @author
 * @version V1.0
 * @date 2020年3月18日
 */
@Data
public class LogoInfoRespVO {
    @ApiModelProperty(value = "首页logo标题")
    private String title;
    @ApiModelProperty(value = "首页logo跳转链接")
    private String href;
    @ApiModelProperty(value = "首页logo地址")
    private String image;
}
