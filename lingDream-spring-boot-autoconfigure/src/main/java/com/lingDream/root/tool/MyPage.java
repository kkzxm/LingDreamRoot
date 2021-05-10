package com.lingDream.root.tool;

import com.baomidou.mybatisplus.plugins.Page;
import lombok.EqualsAndHashCode;

/**
 * 显示5个页码
 *
 *
 * @Author: LI_Lingfei
 * @CreateTime: 2021-03-15 13:27
 */
@EqualsAndHashCode(callSuper = true)
public class MyPage<T> extends Page<T> {
    private Long begin;
    private Long end;

    public Long getBegin() {
        //默认情况
        begin = super.getCurrent()-2L;

        // 后三页
        if (super.getCurrent()>=super.getPages()-2) begin = super.getPages() -4;

        // 前三页
        if (super.getCurrent()<=3) begin = 1L;

        //特殊情况 总页数为4
        if (super.getPages() == 4) begin =1L;

        return begin;
    }


    public Long getEnd() {

        //默认情况
       end = super.getCurrent()+2L;
        // 如果是前三页
        if (super.getCurrent()<=3) end = 5L;

        // 如果是后三页
        if (super.getCurrent() >= super.getPages()-2) end = super.getPages();

        // 特殊情况 总页数为4
        if (super.getPages() == 4) end = 4L;

        return end;
    }

    public MyPage(int current, int size) {
        super(current, size);
    }
}
