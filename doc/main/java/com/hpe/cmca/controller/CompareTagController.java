package com.hpe.cmca.controller;

import com.hpe.cmca.service.CompareTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comparetag")
public class CompareTagController {
    @Autowired
    public CompareTagService  compareTagService;

    @RequestMapping(value = "downAndUnzip", produces = "text/json;charset=UTF-8")
    public void downAndUnzip(String subjectId,String audTrm){

        try {
            compareTagService.computeTag(subjectId,audTrm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "compareTagByRule", produces = "text/json;charset=UTF-8")
    public void compareTagByRule(String subjectId,String audTrm){

        try {
            compareTagService.compareTagByRule(subjectId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
