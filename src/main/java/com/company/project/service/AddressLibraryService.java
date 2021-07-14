package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.AddressLibraryEntity;

import java.util.List;

/**
 * 地址库
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-14 11:56:12
 */
public interface AddressLibraryService extends IService<AddressLibraryEntity> {

    DataResult saveAddressLibraryEntity(AddressLibraryEntity addressLibraryEntity);

    DataResult updateAddressLibraryEntityById(AddressLibraryEntity addressLibraryEntity);

    DataResult removeByIdlist(List<String> idList);

}

