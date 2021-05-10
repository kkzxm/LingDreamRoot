package com.lingDream.root.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @Author: 酷酷宅小明
 * @CreateTime: 2021-04-12 13:39
 */
public interface BaseController<T> {
    @RequestMapping("/getPage")
    String getPage(Model model, String val, String filter, Integer thisPage);

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    String add(HttpServletRequest request,T entity,Model model);


    @RequestMapping(value = "/delById",method = RequestMethod.POST)
    @ResponseBody
    String delById(HttpServletRequest request,T entity);

    @RequestMapping("/updateFindById")
    String updateFindById(HttpServletRequest request,T entity,Model model);

    @RequestMapping(value = "/update", method = POST)
    String update(HttpServletRequest request,T entity,Model model);

    @RequestMapping("/toInsertPage")
    String toInsertPage(Model model);

}
