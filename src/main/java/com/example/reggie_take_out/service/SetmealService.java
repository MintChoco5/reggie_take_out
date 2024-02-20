package com.example.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggie_take_out.dto.SetmealDto;
import com.example.reggie_take_out.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同時需要保存套餐和菜品的關聯關係
     * @param setmealDto
     */
    public  void saveWithDish(SetmealDto setmealDto);

    /**
     * 刪除套餐，同時需要刪除套餐和菜品的關聯數據
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

    //根据id查询套餐信息和对应的菜品信息
    public SetmealDto getByIdWithDishes(Long id);

    //更新套餐信息，同时更新对应的菜品信息
    public void updateWithDishes(SetmealDto setmealDto);
}
