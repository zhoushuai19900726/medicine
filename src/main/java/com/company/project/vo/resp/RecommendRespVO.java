package com.company.project.vo.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 *
 * @author zhoushuai1
 * @version V1.0
 * @date 2020年3月18日
 */
@NoArgsConstructor
@Data
public class RecommendRespVO {

    private String id;
    private String name;
    private String parentId;
    private List<RecommendRespVO> children;

    public RecommendRespVO(String id, String name, String parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }
}
