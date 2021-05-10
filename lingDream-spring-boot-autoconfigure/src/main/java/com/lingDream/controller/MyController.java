package com.lingDream.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.lingDream.service.BaseService;
import com.lingDream.start.LingDreamProperties;
import com.lingDream.tool.MyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

import static com.lingDream.utils.strUtil.StringUtils.lowFirstChar;
import static java.util.Objects.isNull;


/**
 * @Author: 酷酷宅小明
 * @CreateTime: 2021-03-06 14:46
 */
public abstract class MyController<T> implements BaseController<T> {
    //region 属性和构造方法
    @Autowired
    protected LingDreamProperties lingDreamProperties;

    //region 两个主要属性(通过构造函数注入)
    protected final BaseService<T> service;
    protected final String COMMENT;

    public MyController(BaseService<T> service,
                        String COMMENT) {
        this.service = service;
        this.COMMENT = COMMENT+" →";
    }
    //endregion

    //endregion

    //region getMyPage
    protected MyPage<T> getMyPage(Integer thisPage) {
        return service.selectPage(new MyPage<>(thisPage, 10), null);
    }

    protected MyPage<T> getMyPage(Integer thisPage,Integer pageSize) {
        return service.selectPage(new MyPage<>(thisPage, pageSize), null);
    }

    protected MyPage<T> getMyPage(Integer thisPage,Wrapper<T> wrapper) {
        return service.selectPage(new MyPage<>(thisPage, 10), wrapper);
    }
    //endregion

    //region 设置信息

    //region 公用
    /**
     * 设置共用的标题
     */
    protected void setTitle(Model model){
        String substring = COMMENT.substring(0, COMMENT.indexOf(" →"));
        model.addAttribute("title", substring);
    }
    //endregion

    //region 添加或修改相关

    //region 添加或修改完成后,指定需要跳转到的页面
    protected void setRequestURL(HttpServletRequest request, Model model) {
        String requestURL = request.getRequestURL().toString();
        String[] split = requestURL.split("/");
        String classRequestMapping = split[split.length - 2];
        model.addAttribute("toPage", classRequestMapping);
    }
    //endregion

    //endregion

    //region 查询相关

    //region 拼接查询时用的wrapper
    /**
     * <pre>
     *      此方法作为拼接条件构造器,
     * 模糊查询或查询分页时,
     * 通过登录的班级账号(管理员)信息,
     * 过滤掉与本班级无关的信息
     * (如果是超级管理员登录,则无视过滤,查询出全部),
     *
     *
     * </pre>
     *
     * @param val     模糊查询时的值
     * @param filter  糊糊查询用的数据库字段名
     * @return 拼接出的条件构造器
     */
    protected Wrapper<T> getWrapper(String val, String filter) {
        Wrapper<T> wrapper = new EntityWrapper<>();
        wrapper.like(filter, val);
        return wrapper;
    }
    //endregion
    //endregion

    //region 设置零件地址
    /**
     * 反射获取小写类名,去除Controller字符串(10个字符)
     * 用来设置
     * 页面零件地址
     */
    protected void setPageTitleAndPartAddress(Model model, String val, String filter) {
        setTitle(model);

        String partAddress = this.getClass().getSimpleName();
        partAddress = lowFirstChar(partAddress.substring(0, partAddress.length() - 10));
        model.addAttribute("partAddress", partAddress);

        /*
        分页时用到的地址
        */
        String limitPath = partAddress + "/getPage?";
        if (!isNull(val)) limitPath += "val=" + val + "&";
        if (!isNull(filter)) limitPath += "filter=" + filter + "&";
        limitPath += "thisPage";
        model.addAttribute("limitPath", limitPath);
    }
    //endregion

    //endregion
}
