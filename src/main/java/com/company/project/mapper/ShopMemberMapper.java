package com.company.project.mapper;

import com.company.project.entity.ShopMemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 会员
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-08 13:59:46
 */
public interface ShopMemberMapper extends BaseMapper<ShopMemberEntity> {

    @Select("<script> SELECT * FROM shop_member WHERE member_name = #{shopMemberEntity.memberName} <when test='shopMemberEntity.memberId != null'> AND member_id != #{shopMemberEntity.memberId} </when> LIMIT 1 </script>")
    ShopMemberEntity findOneByMemberName(@Param("shopMemberEntity") ShopMemberEntity shopMemberEntity);

    @Select("<script> SELECT * FROM shop_member WHERE member_invitation_code = #{shopMemberEntity.memberInvitationCode} <when test='shopMemberEntity.memberId != null'> AND member_id != #{shopMemberEntity.memberId} LIMIT 1 </when> </script>")
    ShopMemberEntity findOneByInvitationCode(@Param("shopMemberEntity") ShopMemberEntity shopMemberEntity);

}
