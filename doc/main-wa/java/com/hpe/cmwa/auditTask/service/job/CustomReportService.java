package com.hpe.cmwa.auditTask.service.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.dao.MybatisDao;

public class CustomReportService extends BaseObject {

	@Autowired
	private MybatisDao	mybatisDao;
}
