package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopAlbumMapper;
import com.company.project.entity.ShopAlbumEntity;
import com.company.project.service.ShopAlbumService;


@Service("shopAlbumService")
public class ShopAlbumServiceImpl extends ServiceImpl<ShopAlbumMapper, ShopAlbumEntity> implements ShopAlbumService {


}