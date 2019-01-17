package com.hpe.cmca.controller;

/**
 * 为智慧审计提供有价卡链接
 */

import com.asiainfo.biframe.privilege.IUserPrivilegeService;
import com.asiainfo.biframe.privilege.webosimpl.UserPrivilegeServiceImpl;
import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/z4397563")
public class ZHSJController extends BaseController {
    IUserPrivilegeService service = new UserPrivilegeServiceImpl();

    @CmcaAuthority(authorityTypes = {AuthorityType.yjk_sjjg, AuthorityType.yjk_pmhz, AuthorityType.yjk_qtcd, AuthorityType.yjk_sjzl, AuthorityType.yjk_wjxz, AuthorityType.yjk_zgwz})
    @RequestMapping(value = "u83746193367")
    public String yjk(String userId) {
        logger.error("zhsj-yjk");
        return "zhsj/yjk";
    }

    @CmcaAuthority(authorityTypes = {AuthorityType.khqfsjjg, AuthorityType.khqfpmhz, AuthorityType.khqfqtcd, AuthorityType.khqfsjzl, AuthorityType.khqfwjxz, AuthorityType.khqfzgwz})
    @RequestMapping(value = "u46327263623")
    public String khqf(String userId) {
        logger.error("zhsj-khqf");
        return "zhsj/khqf";
    }

    @CmcaAuthority(authorityTypes = {AuthorityType.ygycsjjg, AuthorityType.ygycpmhz, AuthorityType.ygycqtcd, AuthorityType.ygycsjzl, AuthorityType.ygycwjxz, AuthorityType.ygyczgwz})
    @RequestMapping(value = "u19746352725")
    public String ygyccz(String userId) {
        logger.error("zhsj-ygyccz");
        return "zhsj/ygyccz";
    }

    @CmcaAuthority(authorityTypes = {AuthorityType.dzqpmhz, AuthorityType.dzqqtcd, AuthorityType.dzqsjbg, AuthorityType.dzqsjjg, AuthorityType.dzqzgwz})
    @RequestMapping(value = "u48264536712")
    public String dzqglwg(String userId) {
        logger.error("zhsj-dzqglwg");
        return "zhsj/dzqglwg";
    }

    @CmcaAuthority(authorityTypes = {AuthorityType.llglsjbg, AuthorityType.llglpmhz, AuthorityType.llglqtcd, AuthorityType.llglsjjg, AuthorityType.llglzgwz})
    @RequestMapping(value = "u37183635463")
    public String llglwg(String userId) {
        logger.error("zhsj-llglwg");
        return "zhsj/llglwg";
    }

}
