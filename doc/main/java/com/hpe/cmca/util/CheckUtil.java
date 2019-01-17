package com.hpe.cmca.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.hpe.cmca.common.Constants;


public class CheckUtil {

	
	private static Logger logger = Logger.getLogger(CheckUtil.class);
	
	
	/**
	 * 判断是否小于i位数字
	 * eg:8位数字可以是 8位整数，8位小数，4位整数+4位小数
	 * 
	 */
	public static Boolean ifNumber(String str,Integer i){
		if(str==null|| "".equals(str)){
			return true;
		}
		if(str.length()>(i+1)){
			return false;
		}
		String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
        Pattern pattern = Pattern.compile(regex); 
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false; 
        } 
        return true; 
	}
	
	/**
	 * 判断传入的省份名称是否是标准格式
	 * @param prvdName
	 * @return
	 */
	public static Boolean ifStandardPrvdName(String prvdName){
		if(Constants.LIST_PROVD_NAME.contains(prvdName)){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 判断传入的专题名称是否正确
	 * @param proje
	 * @return
	 */
	public static Boolean ifStandardProject(String subjectName){
		if(Constants.LIST_SUBJECT_NAME.contains(subjectName)){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 判断传入的时间是否为合法的时间格式
	 * @param str        需要校验的字符
	 *        formatStr  校验的标准格式
	 *        flag       此字段是否必填
	 * @return
	 */
	public static Boolean ifDateFormat(String str,Boolean flag,String formatStr){
		
		if(str==null || "".equals(str)){
			if(flag){
				return false;
			}else{
				return true;
			}
		}
		boolean convertSuccess=true;
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		    
		try {
			// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(false);
		    format.parse(str);
		} catch (ParseException e) {
		           // e.printStackTrace();
		 // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
		   convertSuccess=false;
		} 
		return convertSuccess;
	}
	
	/**
	 * 判断传入的值是否为符合条件的文本长度
	 * str 为需要判断的字符
	 * num 为字符的最大长度
	 * flag 为是否必填
	 * 
	 */
	public static Boolean ifLegalTest(String str,Integer num,Boolean flag){
		
		if( str == null|| "".equals(str)){
			if(flag){
				return false;
			}else{
				return  true;
			}
		}
		if(str.length()>num){
			return false;
		}
		return true;
	}
	/**
	 * 判断传入的值是否为符合条件的整数
	 */
	public static Boolean ifLegalInteger(String str,Integer num){
		if(num == 0 ){
			num=11;
		}
		if(str == null || "".equals(str)){
			return true;
		}
		if(str.length()>num){
			return false;
		}else{
			Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");    
			return pattern.matcher(str).matches(); 
		}
		
	}
	/**
	 * 判断传入的值是否为符合条件的数字类型
	 */
	public static Boolean ifLegalNumber(){
		return null;
	}
	/**
	 * 判断是否为正确的布尔值格式
	 * @param str
	 * @return
	 */
	public static Boolean ifLegalBoolean(String str){
		if(str== null || "".equals(str)){
			return true;
		}else if("是".equals(str)|| "否".equals(str)){
			return true;
		}else{
			return false;
		}
	}
}
