package com.lingDream.tool;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.EqualsAndHashCode;

/**
 * 得到当前序号
 *
 * @Author: LI_Lingfei
 * @CreateTime: 2021-03-15 13:27
 */
@EqualsAndHashCode(callSuper = true)
public class MyPage<T> extends Page<T> {

    public Long getSerialNumber(Integer thisIndex) {
        return thisIndex + ((current - 1) * size);
    }

    public MyPage(long current, long size) {
        super(current, size);
    }
}

