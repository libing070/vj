/**
 * com.hpe.cmca.controller.DemoController.java
 * Copyright (c) 2016 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.util.Json;

/**
* 将xxxx.geo.json文件中的所有地市的properties中添加code(通过中文名到数据库hpmgr.TB_SUM_PRVD_NAME表中匹配，获取其ID)
* @param 
* 	mapJsonDir : map使用的省市信息的json文件目录（地市不含Code）
* @return 
*	mapJsonDirWithCode - map使用的省市信息的json文件目录（地市含Code）
* 		prvdJsonFileWithCode : 处理后的各省json文件（地市含Code）
* 		matchCtysLogFile : 处理过程中在数据库中查询到的地市（正常处理）
* 		noMatchCtysLogFile : 处理过程中在数据库中未查询到的地市（无法处理）
* 
* @author GuoXY
* @date 20160918
*/
@Controller
@RequestMapping("/temp")
public class MapCtyJsonProcessController extends BaseController {

	@Autowired
	private MybatisDao	mybatisDao;


    // map使用的省市信息的json文件目录（地市不含Code）
	private static String mapJsonDirWithPure = "D://Myeclipse10Bling//cmca//src//main//webapp//WEB-INF//resource//js//highcharts//maps//";
	// map使用的省市信息的json文件目录（地市含Code）
	private static String mapJsonDirWithCode = "D://Myeclipse10Bling//cmca//src//main//webapp//WEB-INF//resource//js//highcharts//mapsWithCode//";
	// 存放mapJsonDirWithPure目录中要处理省json文件列表
	private static ArrayList<String> toProcessFileList = new ArrayList<String>();

	// 无法通过地市json文件中的地市名从数据库中like到的地市
	public static final Map<String,Integer> noMatchCty = new HashMap<String, Integer>();
	static {
		// guangxi.geo.json
		noMatchCty.put("防城港", 12510); // 防城
		// hainan.geo.json
		noMatchCty.put("乐东黎族自治县", 11517); // 乐东
		noMatchCty.put("琼中黎族苗族自治县", 11512); // 琼中
		noMatchCty.put("澄迈县", 11518); // 澄迈
		noMatchCty.put("白沙黎族自治县", 11516); // 白沙
		noMatchCty.put("昌江黎族自治县", 11510); // 昌江
		noMatchCty.put("临高县", 11511); // 临高
		noMatchCty.put("陵水黎族自治县", 11514); // 陵水
		noMatchCty.put("屯昌县", 11508); // 屯昌
		noMatchCty.put("定安县", 11507); // 定安
		noMatchCty.put("保亭黎族苗族自治县", 11515); // 保亭
		// hunan.geo.json
		noMatchCty.put("湘西", 12608); // 自治州
		noMatchCty.put("阿拉善盟", 12812); // 阿拉善
		// 其他：港澳台及直辖市下属单位均为-1
	}
	
	
	/**
	 * 将xxxx.geo.json文件中的所有地市的properties中添加code(通过中文名到数据库hpmgr.TB_SUM_PRVD_NAME表中匹配，获取其ID)
	 * @author GuoXY
	 */
    @RequestMapping(value = "MapJsonProcess")
    public String MapJsonProcess() {
    //public static void main(String[] args) throws Exception {
        try {
        	// 获取所有带处理省json文件列表
            traverseFolder(mapJsonDirWithPure);
            // 遍历所有省json文件进行处理生成含Code的文件
            for (String jsonFilePath : toProcessFileList) {
            	// 原省json文件
                File prvdJsonFilePure = new File(jsonFilePath); //"D://Myeclipse10Bling//cmca//src//main//webapp//WEB-INF//resource//js//highcharts//maps//beijing.geo.json"
                System.out.println("---------Start process : " + prvdJsonFilePure.getName());
                // 新生成的且含有code属性的json文件
                String prvdJsonFileWithCode = mapJsonDirWithCode + prvdJsonFilePure.getName();
                
                // 处理过程中在数据库中查询到的地市（正常处理）
                String matchCtysLogFile = mapJsonDirWithCode + "matchCtysFile.log";
                // 处理过程中在数据库中未查询到的地市（无法处理）
                String noMatchCtysLogFile = mapJsonDirWithCode + "noMatchCtysFile.log";
              
                // 打开省json文件，读取全部信息（因为都写在一行中所以，readLine一次即可）
                // , "UTF-8"放入到cmca WEB工程中需要转码，否则，json工具解析时会失败；使用java控制台方式读取时，不用转码
                InputStreamReader read = new InputStreamReader(new FileInputStream(prvdJsonFilePure), "UTF-8"); 
                BufferedReader bufferedReader = new BufferedReader(read);
                String jsonStr = bufferedReader.readLine();
                // 将json传转化成map对象，并获取当前省的城市列表
                //System.out.println(" jsonStr:" + jsonStr);
                Map<String, Object> mapPrvd = parseJSON2Map(jsonStr);
                bufferedReader.close();
                read.close();
                ArrayList ctyList = (ArrayList)mapPrvd.get("features");

                // 无论当前省是否有“匹配or不匹配”地市，都在“matchCtysFile和noMatchCtysFile”写入当前处理省，以便后续追加地市名
                fileAppendContent(matchCtysLogFile, System.getProperty("line.separator", "\n") + prvdJsonFilePure.getName() + System.getProperty("line.separator", "\n"));
                fileAppendContent(noMatchCtysLogFile, System.getProperty("line.separator", "\n") + prvdJsonFilePure.getName() + System.getProperty("line.separator", "\n"));
                
                // 遍历城市列表，用地市名到数据库中匹配获取其ID，如果存在则将当前“地市名”追加到matchCtysFile，否则追加到noMatchCtysFile
                for(Object cty : ctyList){
                	ListOrderedMap mapCty = (ListOrderedMap) cty;
                	//ListOrderedMap mapCtyDetail = mapCty.get("properties");
                	//Map<String, Object> mapCtyDetail = parseJSON2Map( ((JSONObject)mapCty.get("properties")).toString() );
                	//System.out.println(" cty:" + mapCty.get("id") + ","+ mapCty.get("properties") 
                	//		+ "," + ((JSONObject) mapCty.get("properties")).get("name"));//  + "," + mapCtyDetail.get("name")
                	
                	// 获取当前"省的一个地市"中文名，并到数据库中查询其ID
                	String cityName = (String) ((JSONObject) mapCty.get("properties")).get("name");
            		Map<String, Object> params = new HashMap<String, Object>();
            		params.put("cityName", cityName);
                	List<Map<String, Object>> matchCtylist = mybatisDao.getList("commonMapper.selectCityByName", params);
                	
                	// 找到匹配中文名
                	if (1 == matchCtylist.size()) {
                		Map<String, Object> matchCity = matchCtylist.get(0);
                		// 注意：属性添加过了，就不用重复添加了！！！
                		//((JSONObject) mapCty.get("properties")).put("code", matchCity.get("cityCode"));
                		((JSONObject) mapCty.get("properties")).put("trueValue", -1);
                        fileAppendContent(matchCtysLogFile, "'" + cityName + "',");
                	}
                	// 数据库中差不到的但在常量map中配置好了
                	else if (null != noMatchCty.get(cityName)) {
                		// 注意：属性添加过了，就不用重复添加了！！！
                		//((JSONObject) mapCty.get("properties")).put("code", noMatchCty.get(cityName));
                		((JSONObject) mapCty.get("properties")).put("trueValue", -1);
                        fileAppendContent(matchCtysLogFile, "'" + cityName + "',");
                	}
                	// 未找到匹配中文名(港澳台及直辖市下属单位均为-1) 
                	else if (0 == matchCtylist.size()) {
                		// 注意：属性添加过了，就不用重复添加了！！！
                		//((JSONObject) mapCty.get("properties")).put("code", -1);
                		((JSONObject) mapCty.get("properties")).put("trueValue", -1);
                        fileAppendContent(noMatchCtysLogFile, "'" + cityName + "',");
                	}
                	// 匹配异常（多个匹配项）
                	else {
                    	System.out.println("\n\n\n!!!!!!!!!!!!!!!!!!" + prvdJsonFilePure.getName() + "：" + ((JSONObject) mapCty.get("properties")).get("name") + "\n\n\n!!!!!!!!!!!!!!!!!!" );
                	}
                }
                
                // 将添加好地市code信息的json串写入到相应省的新文件内
                System.out.println(" jsonMap:" + mapPrvd);
                fileAppendContent(prvdJsonFileWithCode, Json.Encode(mapPrvd));
                
                System.out.println("---------End process : " + prvdJsonFilePure.getName());
                read.close();
            }
	    } catch (Exception e) {
	        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!读取文件内容出错");
	        e.printStackTrace();
	        logger.error(e.getMessage(),e);
	    }
    	
        return "home/index";
    }
    

	/**
	* json转换map.
	* @param jsonStr json字符串
	* @return Map<String,Object> 集合
	* @author GuoXY
    */
    public static Map<String, Object> parseJSON2Map(String jsonStr){
        ListOrderedMap map = new ListOrderedMap();
        //最外层解析
        JSONObject json = JSONObject.fromObject(jsonStr);
        for(Object k : json.keySet()){
            Object v = json.get(k); 
            //如果内层还是数组的话，继续解析
            if(v instanceof JSONArray){
                List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
                Iterator<JSONObject> it = ((JSONArray)v).iterator();
                while(it.hasNext()){
                    JSONObject json2 = it.next();
                    list.add(parseJSON2Map(json2.toString()));
                }
                map.put(k.toString(), list);
            } else {
                map.put(k.toString(), v);
            }
        }
        return map;
    }

	/**
	* 向目标文件中追加字符串
	* @param fileName 目标文件（含全路径）
	* @return content 追加到文件中的内容
	* @author GuoXY
    */
	public static void fileAppendContent(String fileName, String content) {
		try {
			//打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			//FileWriter中写出中文乱码
			//FileWriter writer = new FileWriter(fileName, true);  
			//writer.write(content);
			//writer.close();
			
			// true 代表追加写入
			FileOutputStream fis = new FileOutputStream(fileName, true);
			OutputStreamWriter osw = new OutputStreamWriter(fis, "UTF-8");
			osw.write(content);
			osw.flush();
		    osw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	* 遍历指定目录获取所有“符合条件”的文件
	* @param path 待处理目录
	* @return toProcessFileList 所有“符合条件”的文件
	* @author GuoXY
    */
    public static void traverseFolder(String path) {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                System.out.println("文件夹是空的!");
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
//                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        traverseFolder(file2.getAbsolutePath());
                    } else {
//                        System.out.println("文件:" + file2.getAbsolutePath());
                    	// 过滤掉原始目录中非省json文件
                    	if (file2.getName().contains("json")
                    			&& !file2.getName().contains("china")) {
                        	toProcessFileList.add(file2.getAbsolutePath());
                    	}
//                    	System.out.println("filelist.size:" + filelist.size() + " ,curFileName: " + file2.getName());
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
    }
    

}
