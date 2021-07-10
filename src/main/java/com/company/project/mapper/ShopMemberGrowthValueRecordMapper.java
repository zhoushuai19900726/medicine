package com.company.project.mapper;

import com.company.project.entity.ShopMemberGrowthValueRecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会员成长值记录
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-09 17:10:36
 */
public interface ShopMemberGrowthValueRecordMapper extends BaseMapper<ShopMemberGrowthValueRecordEntity> {

    @Delete("<script> DELETE FROM shop_member_growth_value_record WHERE member_id IN  <foreach collection ='memberIdList' item ='memberId' index ='index' separator=',' open='(' close=')'  > #{memberId} </foreach> </script>")
    void absolutelyDeleteByMemberIdList(@Param("memberIdList") List<String> memberIdList);
}
