package com.company.project.mapper;

import com.company.project.entity.ShopRecommendationRelationshipEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会员推荐关系表
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-09 15:40:52
 */
public interface ShopRecommendationRelationshipMapper extends BaseMapper<ShopRecommendationRelationshipEntity> {

    @Delete("<script> DELETE FROM shop_recommendation_relationship WHERE member_id IN  <foreach collection ='memberIdList' item ='memberId' index ='index' separator=',' open='(' close=')'  > #{memberId} </foreach> </script>")
    void absolutelyDeleteByMemberIdList(@Param("memberIdList") List<String> memberIdList);
}
