package com.lingDream.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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

    /**
     * 自定义的一个分页查询,
     * 用来处理一对多的特殊情况
     * (原因:
     * MyBatisPlus会自动添加后面的Limit参数,
     * 暂时没找到更好的办法
     * )
     *
     */
    List<T> selectMyPage(@Param("start") Long start,@Param("size") Long size);
}
