package com.company.project.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * HomeInfoRespVO
 *
 * @author
 * @version V1.0
 * @date 2020年3月18日
 */
@Data
public class HomeInfoRespVO {
    @ApiModelProperty(value = "首页标题")
    private String title;
    @ApiModelProperty(value = "首页跳转链接")
    private String href;
}
