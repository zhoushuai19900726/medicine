package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopAlbumGalleryMapper;
import com.company.project.entity.ShopAlbumGalleryEntity;
import com.company.project.service.ShopAlbumGalleryService;


@Service("shopAlbumGalleryService")
public class ShopAlbumGalleryServiceImpl extends ServiceImpl<ShopAlbumGalleryMapper, ShopAlbumGalleryEntity> implements ShopAlbumGalleryService {


}