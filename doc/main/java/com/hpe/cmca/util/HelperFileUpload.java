package com.hpe.cmca.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
 
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 文件上传工具类
 *
 */
public class HelperFileUpload {

	/**
	 * 上传文件公共方法
	 * @param request	请求request
	 * @param uploadPath	文件保存的路径
	 * @param aliasNamesMap	文件对应的别名，可以为空，如果为空，就用上传文件的原名保存
	 * @throws IOException 
	 */
	public static void saveFile(MultipartHttpServletRequest request,String uploadPath,Map<String,String> aliasNamesMap) throws IOException{
		File dir = new File(uploadPath);
		if(!dir.exists()){
			dir.mkdirs();
		}
		if(request!=null && request instanceof MultipartHttpServletRequest){
			//获取文件的name属性集合
			Iterator<String> fileNames = request.getFileNames();
			if(fileNames!=null){
				//循环保存文件
				for(;fileNames.hasNext();){
					//当前文件的name值
					String fileName = fileNames.next();
					//获取上传文件对象
					MultipartFile file = ((MultipartHttpServletRequest) request).getFile(fileName);
					if (file==null || file.isEmpty()) continue;
					//定义文件保存的别名，如果别名存在，就用别名保存，否则用原文件名保存
					String aliasName = "";
					//从文件属性name值与保存文件的别名对应关系查找当前保存的文件是否存在别名
					if(aliasNamesMap!=null){
						//获取当前文件的别名
						aliasName = aliasNamesMap.get(fileName);
					}
					//定义保存文件的文件路径+文件名
					String outputFileName = uploadPath+File.separator;
					//如果别名存在，就用别名保存
					if(!HelperString.isNullOrEmpty(aliasName)){
						outputFileName += aliasName;
						//截取上传文件的文件后缀
						if(file.getOriginalFilename().indexOf(".")!=-1){
							outputFileName += file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
						}
					}else{
						//别名不存在，就用文件原名
						outputFileName +=file.getOriginalFilename();
					}
					//定义输出文件
					File outFile = new File(outputFileName);
					BufferedInputStream inBuff = null;
			        BufferedOutputStream outBuff = null;
	            	// 新建文件输入流并对它进行缓冲
					inBuff = new BufferedInputStream(file.getInputStream());
					// 新建文件输出流并对它进行缓冲
					outBuff = new BufferedOutputStream(new FileOutputStream(outFile));
					// 缓冲数组
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = inBuff.read(b)) != -1) {
						outBuff.write(b, 0, len);
					}
					// 刷新此缓冲的输出流
					outBuff.flush();
		            // 关闭流
		            if (inBuff != null)
							inBuff.close();
		            if (outBuff != null)
							outBuff.close();
				}
			}
		}
	}
}
