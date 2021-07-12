package com.company.project.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.company.project.entity.ShopMemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 会员
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-08 13:59:46
 */
public interface ShopMemberMapper extends BaseMapper<ShopMemberEntity> {

    @Select("SELECT * FROM shop_member WHERE member_id = #{memberId}")
    ShopMemberEntity findOneByMemberId(@Param("memberId") String memberId);

    @Select("<script> SELECT * FROM shop_member WHERE member_name = #{shopMemberEntity.memberName} <when test='shopMemberEntity.memberId != null'> AND member_id != #{shopMemberEntity.memberId} </when> LIMIT 1 </script>")
    ShopMemberEntity findOneByMemberName(@Param("shopMemberEntity") ShopMemberEntity shopMemberEntity);

    @Select("<script> SELECT * FROM shop_member WHERE member_name IN <foreach collection ='memberNameList' item ='memberName' index ='index' separator=',' open='(' close=')'  > #{memberName} </foreach> </script>")
    List<ShopMemberEntity> findOneByMemberNameList(@Param("memberNameList") List<String> memberNameList);

    @Select("<script> SELECT * FROM shop_member WHERE member_invitation_code = #{shopMemberEntity.memberInvitationCode} <when test='shopMemberEntity.memberId != null'> AND member_id != #{shopMemberEntity.memberId} LIMIT 1 </when> </script>")
    ShopMemberEntity findOneByInvitationCode(@Param("shopMemberEntity") ShopMemberEntity shopMemberEntity);

    @Delete("<script> DELETE FROM shop_member WHERE member_id IN  <foreach collection ='memberIdList' item ='memberId' index ='index' separator=',' open='(' close=')'  > #{memberId} </foreach> </script>")
    void absolutelyDeleteByMemberIdList(@Param("memberIdList") List<String> memberIdList);

    @Select("SELECT * FROM shop_member ${ew.customSqlSegment}")
    IPage<ShopMemberEntity> selectLogoutPage(IPage<ShopMemberEntity> page, @Param(Constants.WRAPPER) Wrapper<ShopMemberEntity> wrapper);

    @Update("UPDATE shop_member SET deleted = 0 WHERE member_id = #{memberId}")
    Integer revokeShopMemberEntityById(@Param("memberId") String memberId);
}
