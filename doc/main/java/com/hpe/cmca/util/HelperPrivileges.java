package com.hpe.cmca.util;

import java.util.ArrayList;
import java.util.List;

public class HelperPrivileges {

	private static List<String> adminPrivileges;

	public static List<String> getAdminPrivileges() {
		if (adminPrivileges == null || adminPrivileges.size() == 0) {
			adminPrivileges = new ArrayList<String>();
			adminPrivileges.add("/dami/lotList");
			adminPrivileges.add("/dami/getLotList");
			adminPrivileges.add("/dami/makeCard");
			adminPrivileges.add("/dami/doExportCard");
			adminPrivileges.add("/dami/doMakeCard");
			adminPrivileges.add("/dami/encrypt");
			adminPrivileges.add("/dami/decrypt");

		}
		return adminPrivileges;

	}

}
