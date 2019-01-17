package com.hpe.cmwa.util;



public class StringUtils extends org.apache.commons.lang.StringUtils{
	public static String getFocusCdKey(Object o){
		String rString = "";
		if(o!=null){		
			if(o instanceof String[]){
				rString = StringUtils.join((String[])o, ",");
			}else if(o instanceof String){
				String s = (String)o;
				s = s.replace("('","");
				s = s.replace("')","");
				rString =  s ;
			}
		}
		return "''".equals(rString)?null: rString;
	}
	public static String getWgTypeKey(Object o){
		return getFocusCdKey(o);
	}
	public static String getStringArr2STR(Object o){
		String rString = "'-1'";
		if(o!=null){		
			if(o instanceof String[]){
				for(String s : (String[])o){
					rString = rString + ",'" + s + "'";
				}
			}else if(o instanceof String){
				String s = (String)o;
				s = s.replace("('","");
				s = s.replace("')","");
				rString = "'" + s + "'";
			}
		}
		return "'-1'".equals(rString)?null:"(" + rString + ")";
	}
	public static String[] getStringArr(Object o){
		String[] rString = null;
		if(o!=null){		
			if(o instanceof String[]){
				rString = (String[])o;
			}else if(o instanceof String){
				String s = (String)o;
				if(!StringUtils.isEmpty(s)){
					rString = new String[1];
					rString[0] = (String)o;
				}
			}
		}
		return rString;
	}
}
