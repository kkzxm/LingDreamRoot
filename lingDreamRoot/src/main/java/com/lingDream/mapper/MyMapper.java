package com.lingDream.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 相当于BaseMapper的一个中转站,在这里可以自定义添加一些公共的方法
 *
 * @Author: LI_Lingfei
 * @CreateTime: 2021-03-06 16:03
 */
public interface MyMapper<T> extends BaseMapper<T> {
    /**
     * 批量增加
     */
    int insertList(@Param("list") List<T> list);

    /**
     * 当表中除外键以外,还有一个或多个组合起来的键,它是唯一的,时使用
     * 根据表中的某一个唯一列查询
     */
    T selectByOnly(T t);
}
