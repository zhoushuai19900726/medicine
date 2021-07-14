package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.AddressLibraryMapper;
import com.company.project.entity.AddressLibraryEntity;
import com.company.project.service.AddressLibraryService;


@Service("addressLibraryService")
public class AddressLibraryServiceImpl extends ServiceImpl<AddressLibraryMapper, AddressLibraryEntity> implements AddressLibraryService {


}