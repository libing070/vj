package com.hpe.cmca.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.hpe.cmca.finals.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.util.FtpUtil;



@Service
public class BaseFileService extends BaseObject{
    private String allowSuffix = "jpg,png,gif,jpeg,txt,text,html,bak,war,zip,docx,doc,xlsx,xls,pdf";//允许文件格式
	private long allowSize = 2L;//允许文件大小
	private String fileName;
	private String[] fileNames;
	private String destDir;
	private String ftpHttpUrlPrefix;
//	@Autowired
//	private BaseFileService		baseFileService;
	
	//protected FilePropertyPlaceholderConfigurer	propertyUtilRMS=new FilePropertyPlaceholderConfigurer();
	
	public String getAllowSuffix() {
		return allowSuffix;
	}
	public String getdestDir() {
		return StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpPath"));
	}
	public String getftpHttpUrlPrefix() {
		return StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpHttpUrlPrefix"));
	}

	public void setAllowSuffix(String allowSuffix) {
		this.allowSuffix = allowSuffix;
	}

	public long getAllowSize() {
		return allowSize*1024*1024;
	}

	public void setAllowSize(long allowSize) {
		this.allowSize = allowSize;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String[] getFileNames() {
		return fileNames;
	}

	public void setFileNames(String[] fileNames) {
		this.fileNames = fileNames;
	}

	private FtpUtil initFtp() {
	    
		FtpUtil ftpTool = new FtpUtil();
		String ftpServer = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpServer"));
		String ftpPort = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpPort"));
		String ftpUser = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpUser"));
		String ftpPass = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpPass"));
		ftpTool.initClient(ftpServer, ftpPort, ftpUser, ftpPass);
		return ftpTool;
	}
//	public void testClient() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
//
//	    FtpUtil ftpRMS=new BaseFileController().initFtp();
//	    //ftpRMS.uploadDir(str1, "/hp/data");
//	}
	/**
	 * 
	 * <p class="detail">
	 * 功能：重新命名文件
	 * </p>
	 * @author wangsheng
	 * @date 2014年9月25日 
	 * @return
	 */
	private String getFileNameNew(){
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return fmt.format(new Date());
	}
	
	/**
	 * 
	 * <p class="detail">
	 * 功能：文件上传
	 * </p>
	 * @author wangsheng
	 * @date 2014年9月25日 
	 * @param files
	 * @param destDir
	 * @throws Exception
	 */
	public void upload(MultipartFile file, String prvoid,HttpServletRequest request) throws Exception {
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
		try {
				String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
				int length = getAllowSuffix().indexOf(suffix);
				if(length == -1){
					throw new Exception("请上传允许格式的文件");
				}
				if(file.getSize() > getAllowSize()){
					throw new Exception("您上传的文件大小已经超出范围");
				}
				
				String realPath = request.getSession().getServletContext().getRealPath("/");
				destDir=this.getdestDir()+"/"+prvoid+"/";
				File destFile = new File(realPath+destDir);
				if(!destFile.exists()){
					destFile.mkdirs();
				}
				//String fileNameNew = getFileNameNew()+"."+suffix;
				String fileNameNew = file.getOriginalFilename();
				File f = new File(destFile.getAbsoluteFile()+"/"+fileNameNew);
				file.transferTo(f);
				f.createNewFile();
				fileName = basePath+destDir+fileNameNew;
				FtpUtil ftpRMS=initFtp();
//				FtpUtil ftpRMS = new FtpUtil();
//				ftpRMS.initClient("127.0.0.1", "21", "hp", "1234.com");
				ftpRMS.uploadFile(f, destDir);
//				new SJZLProcessor().uploadFile(f.getAbsolutePath(), "/hp/data");
				
		} catch (Exception e) {
			throw e;
	}
}
}