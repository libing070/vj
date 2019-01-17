package com.hpe.cmca.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;

@Controller
@Scope("prototype")
public class MainController extends BaseObject{


    @RequestMapping(value = "ywyj/index")
    public String ywyj() {

        return "jkyj/index";
    }

    @RequestMapping(value = "qfgl/index")
	public ModelAndView qfgl() {

		ModelAndView view =  new ModelAndView("jkyj/index");
		view.addObject("LV1",1);
		return view;
	}

	@RequestMapping(value = "zbgl/index")
	public ModelAndView zbgl() {
		ModelAndView view =  new ModelAndView("jkyj/index");
		view.addObject("LV1",2);
		return view;
	}

	@RequestMapping(value = "yxzygl/index")
	public ModelAndView yxzygl() {
		ModelAndView view =  new ModelAndView("jkyj/index");
		view.addObject("LV1",3);
		return view;
	}

	@RequestMapping(value = "kdgl/index")
	public ModelAndView kdgl() {
		ModelAndView view =  new ModelAndView("jkyj/index");
		view.addObject("LV1",4);
		return view;
	}

	@RequestMapping(value = "wlwgl/index")
	public ModelAndView wlwgl() {
		ModelAndView view =  new ModelAndView("jkyj/index");
		view.addObject("LV1",5);
		return view;
	}

	@RequestMapping(value = "ykgl/index")
	public ModelAndView ykgl() {
		ModelAndView view =  new ModelAndView("jkyj/index");
		view.addObject("LV1",6);
		return view;
	}

    @RequestMapping(value = "main")
    public String index() {
    	logger.error("suceess login. redirect url /cmca/main");
        return "index";
    }

    @CmcaAuthority(authorityTypes = { AuthorityType.khqfsjjg,AuthorityType.khqfpmhz,AuthorityType.khqfqtcd,AuthorityType.khqfsjzl,AuthorityType.khqfwjxz,AuthorityType.khqfzgwz })
    @RequestMapping(value = "khqf/index")
    public String khqf() {
    	logger.error("suceess login. redirect url /cmca/main");
        return "khqf/index";
    }

    @CmcaAuthority(authorityTypes = { AuthorityType.ygycsjjg,AuthorityType.ygycpmhz,AuthorityType.ygycqtcd,AuthorityType.ygycsjzl,AuthorityType.ygycwjxz,AuthorityType.ygyczgwz })
    @RequestMapping(value = "ygyccz/index")
    public String ygyccz() {
    	logger.error("suceess login. redirect url /cmca/main");
        return "ygyccz/index";
    }

}
