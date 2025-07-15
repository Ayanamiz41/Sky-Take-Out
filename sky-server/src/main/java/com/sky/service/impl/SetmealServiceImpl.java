package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    SetmealMapper setmealMapper;
    @Autowired
    SetmealDishMapper setmealDishMapper;
    @Autowired
    DishMapper dishMapper;
    /**
     * 新增套餐
     * @param setmealDTO
     */
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {setmealDish.setSetmealId(setmeal.getId());});
        setmealDishMapper.insertBatch(setmealDTO.getSetmealDishes());
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        long total = page.getTotal();
        List<SetmealVO> records = page.getResult();
        return new PageResult(total, records);
    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    public void deleteBatch(List<Long> ids) {
        //判断当前套餐能否删除
        //启售的不能删除
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if(setmeal.getStatus().equals(StatusConstant.ENABLE)){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        //删除套餐表中的数据
        setmealMapper.deleteByIds(ids);
    }

    /**
     * 修改套餐状态
     * @param status
     * @param id
     * @return
     */
    public void updateStatus(Integer status, Long id) {
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
        //如果套餐内包含未启售的菜品，则无法启售套餐
        setmealDishes.forEach(setmealDish -> {
            Dish dish = dishMapper.getById(setmealDish.getDishId());
            if(dish.getStatus().equals(StatusConstant.DISABLE) && status.equals(StatusConstant.ENABLE)){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ENABLE_FAILED);
            }
        });
        Setmeal setmeal = Setmeal.builder().
                id(id).
                status(status).
                build();
        setmealMapper.update(setmeal);
    }

    /**
     * 修改套餐
     * @param setmealDTO
     * @return
     */
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //更新套餐基本信息
        setmealMapper.update(setmeal);
        //删除套餐原关联的菜品
        setmealDishMapper.deleteById(setmealDTO.getId());
        //设置套餐关联菜品的套餐id为当前套餐的id
        List<SetmealDish>  setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealDTO.getId());
        });
        //重新插入套餐关联菜品
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 根据id查询套餐及关联的菜品
     * @param id
     * @return
     */
    public SetmealVO getByIdWithDish(Long id) {
        //根据id查询套餐
        Setmeal setmeal = setmealMapper.getById(id);
        //根据套餐id查询关联菜品
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
        //将查询的数据封装到VO
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
