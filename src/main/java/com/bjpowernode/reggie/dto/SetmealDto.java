package com.bjpowernode.reggie.dto;


import com.bjpowernode.reggie.entity.Setmeal;
import com.bjpowernode.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
