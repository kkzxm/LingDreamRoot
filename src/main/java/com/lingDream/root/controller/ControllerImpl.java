package com.lingDream.root.controller;

import com.lingDream.root.service.BaseService;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @Author: 酷酷宅小明
 * @CreateTime: 2021-04-07 09:34
 */
public abstract class ControllerImpl<T> extends MyController<T> {
    public ControllerImpl(BaseService<T> service, String COMMENT) {
        super(service, COMMENT);
    }

    @Override
    public String getPage(Model model, String val, String filter, Integer thisPage) {
        return setThisPageInData(model, val, filter, thisPage);
    }

    @Override
    public String toInsertPage(Model model) {
        return setToInsertPage(model);
    }

    @Override
    public String add(HttpServletRequest request, T entity, Model model) {
        super.setRequestURL(request, model);
        model.addAttribute("result", COMMENT + "添加成功");
        if (!service.insert(entity)) {
            model.addAttribute("result", COMMENT + "添加失败");
        }
        return "admin/adminPublic/addResult";
    }

    @Override
    public String delById(HttpServletRequest request, T entity) {
        if (service.deleteById((Serializable) entity)) {
            return COMMENT + "删除成功";
        } else {
            return COMMENT + "删除失败";
        }
    }

    @Override
    public String updateFindById(HttpServletRequest request, T entity, Model model) {
        T t = service.selectById((Serializable) entity);
        model.addAttribute("entity", t);
        return toInsertPage(model);
    }

    @Override
    public String update(HttpServletRequest request, T entity, Model model) {
        super.setRequestURL(request, model);
        model.addAttribute("result", COMMENT + "修改成功");
        if (!service.updateById(entity)) {
            model.addAttribute("result", COMMENT + "修改失败");
        }
        return "admin/adminPublic/addResult";
    }
}
