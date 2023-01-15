package com.hyg.controller;

import com.alibaba.fastjson.JSON;
import com.hyg.domain.Fanhao;
import com.hyg.domain.Star;
import com.hyg.domain.StarFanhao;
import com.hyg.service.dao_related.FanhaoService;
import com.hyg.service.dao_related.quoted.StarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 磁力搜索的接口
 */
@CrossOrigin
@RestController
@RequestMapping("/fanhao")
public class FanhaoController {
    @Autowired
    private StarService starService;
    @Autowired
    private FanhaoService fanhaoService;

    @RequestMapping("/stars")
    public String starInformation(@RequestBody String info){
        Star star = JSON.parseObject(info, Star.class);

        if (star.getName().equals(""))
            return "";

        List<StarFanhao> result = starService.findFanhaoByStar(star.getName());

        return JSON.toJSONString(result);
    }

    @RequestMapping("/fanhaos")
    public String fanhaoInformation(@RequestBody String info){
        Fanhao fanhao = JSON.parseObject(info, Fanhao.class);
        Fanhao result = fanhaoService.getResultByFanhao(fanhao.getFanhao());
        return JSON.toJSONString(result);
    }
}
