package com.nis.galaxy.web.controller;

 import com.nis.galaxy.domain.dict.UiSysDictInfo;
 import com.nis.galaxy.web.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author GouGe
 * @data 2018/8/30 17:26
 */

@RestController
public class DictController {
    @Autowired
    DictService dictService;
    @RequestMapping("/get")
    public List<UiSysDictInfo> getUiSysDictInfo(){
        return dictService.getUiSysDictInfo();
    }


}
