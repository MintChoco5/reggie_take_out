package com.example.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggie_take_out.dto.DishDto;
import com.example.reggie_take_out.entity.Dish;

public interface DishService extends IService<Dish> {

    // 新增菜品，同時插入菜品對應的口味數據，需要操作兩張表：dish、dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    // 根據id查詢菜品信息和對應的口味信息
    public DishDto getByIdWithFlavor(Long id);

    // 更新菜品信息，同時更新對應的口味信息
    public void updateWithFlavor(DishDto dishDto);
}
