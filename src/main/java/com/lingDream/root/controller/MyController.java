package com.lingDream.root.controller;


import com.baomidou.mybatisplus.mapper.Wrapper;
import com.lingDream.root.service.MyService;
import com.lingDream.root.tool.MyPage;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;


/**
 * @Author: LI_Lingfei
 * @CreateTime: 2021-03-06 14:46
 */
public abstract class MyController<T> {
    protected final MyService<T> service;
    protected final String COMMENT;

    public MyController(MyService<T> service, String COMMENT) {
        this.service = service;
        this.COMMENT = COMMENT;
    }

    //region 设置信息
    protected void setRequestURL(HttpServletRequest request, Model model) {
        String requestURL = request.getRequestURL().toString();
        String[] split = requestURL.split("/");
        String classRequestMapping = split[split.length - 2];
        model.addAttribute("toPage", classRequestMapping);
    }
    //endregion

    //region 得到page
    public void getMyPage(Integer thisPage, Integer pageSize) {
        service.selectPage(new MyPage<>(thisPage, pageSize));
    }

    public MyPage<T> getMyPage(Integer thisPage) {
        MyPage<T> myPage = new MyPage<>(thisPage, 10);
        return service.selectPage(myPage);
    }

    public MyPage<T> getMyPage(MyPage<T> myPage) {
        return service.selectPage(myPage);
    }

    public MyPage<T> getMyPage(MyPage<T> myPage, Wrapper<T> wrapper) {
        return service.selectPage(myPage,wrapper);
    }

    public MyPage<T> getMyPage(Integer thisPage, Wrapper<T> wrapper) {
        MyPage<T> myPage = new MyPage<>(thisPage, 10);
        return service.selectPage(myPage,wrapper);
    }

    public MyPage<T> getMyPage(Integer thisPage,Integer pageSize, Wrapper<T> wrapper) {
        MyPage<T> myPage = new MyPage<>(thisPage, pageSize);
        return service.selectPage(myPage,wrapper);
    }

    //endregion
}
