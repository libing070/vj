package com.hpe.cmca.filter;

import org.apache.log4j.Logger;

import com.asiainfo.biframe.privilege.IUserPrivilegeService;
import com.asiainfo.biframe.privilege.webosimpl.UserPrivilegeServiceImpl;

/**
 * 将判断权限的操作放到子查询中
 * 
 * @author Tian Yue
 * 
 */
public class RightThread extends Thread {

	IUserPrivilegeService service = new UserPrivilegeServiceImpl();
	private Logger logger = Logger.getLogger(RightThread.class);

	private String txt;
	private String userId;
	private int index;

	public int HaveRight = 0;

	public RightThread(String txt, String userId, int i) {
		this.txt = txt;
		this.userId = userId;
		this.index = i;
	}

	public void run() {
		try {
			Boolean havRight = service.haveRightByUserId(userId, index, txt);
			logger.error(this.currentThread().getName() + "," + userId + "," + txt + "," + havRight);
			if (havRight) {
				this.HaveRight += 2;
			} else {
				this.HaveRight++;
			}
		} catch (Exception e) {

			// e.printStackTrace();
			logger.error("RightThread error,try again...userId=" + userId + ",right=" + txt);
			// this.HaveRight++;
			//如果请求发生异常，再尝试一次。
			try {
				Boolean havRight = service.haveRightByUserId(userId, index, txt);
				logger.error(this.currentThread().getName() + "," + userId + "," + txt + "," + havRight);
				if (havRight) {
					this.HaveRight += 2;
				} else {
					this.HaveRight++;
				}
			} catch (Exception er) {

				er.printStackTrace();
				logger.error("RightThread error:userid=" + userId + ",right=" + txt + ",errormsg=" + e.getMessage(), e);
				this.HaveRight++;
			}
		}
	}
}