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

    @Select("<script> SELECT * FROM shop_member WHERE member_name = #{shopMemberEntity.memberName} <when test='shopMemberEntity.memberId != null'> AND member_id != #{shopMemberEntity.memberId} </when> </script>")
    ShopMemberEntity findOneByUnique(@Param("shopMemberEntity") ShopMemberEntity shopMemberEntity);
}
