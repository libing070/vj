package com.hpe.cmca.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hpe.cmca.service.CompareTagService;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.asiainfo.biframe.privilege.IUser;
import com.hpe.cmca.common.FilePropertyPlaceholderConfigurer;
import com.hpe.cmca.pojo.BgxzData;
import com.hpe.cmca.service.BgxzService;
import com.hpe.cmca.service.SMSService;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.FtpUtil;
import com.hpe.cmca.webservice.sms.SSO_WebServiceLocator;
import com.hpe.cmca.webservice.sms.SSO_WebServiceSoap_PortType;
@Controller
@RequestMapping("/fileload")
public class FileUploadController extends MultiActionController {


	@Autowired
	public BgxzService bgxzService =null;
	@Autowired
	public SMSService smsService =null;

	@Autowired
	protected FilePropertyPlaceholderConfigurer propertyUtil = null;

	SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMdd HH:mm");

	  /**
     * 获取已上传的文件大小
     * @param request
     * @param response
     */
	 @RequestMapping(value = "getChunkedFileSize")
    public void getChunkedFileSize(HttpServletRequest request,HttpServletResponse response){
        //存储文件的路径，根据自己实际确定
        String currentFilePath = getLocalPath();
//        String currentFilePath = "D:/data1/hp_web/caUpFile/";// 记录当前文件的绝对路径
        PrintWriter print = null;
        try {
            request.setCharacterEncoding("utf-8");
            print = response.getWriter();
            String fileName = new String(request.getParameter("fileName").getBytes("ISO-8859-1"),"UTF-8");
            String lastModifyTime = request.getParameter("lastModifyTime");
            File file = new File(currentFilePath+fileName+"."+lastModifyTime);
            if(file.exists()){
                print.print(file.length());
            }else{
                print.print(-1);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


	/**
     *
     * 断点文件上传 1.先判断断点文件是否存在 2.存在直接流上传 3.不存在直接新创建一个文件 4.上传完成以后设置文件名称
     *
     */
    @RequestMapping(value = "appendUpload2Server")
    public  void appendUpload2Server(HttpServletRequest request,HttpServletResponse response,BgxzData bgxz) {
        PrintWriter print = null;
        try {
        	String currentFilePath = getLocalPath();// 记录当前文件的绝对路径
//        	String currentFilePath = "D:/data1/hp_web/caUpFile/";// 记录当前文件的绝对路径
            request.setCharacterEncoding("utf-8");
            String fileSize = request.getParameter("fileSize");
//            long totalSize = Long.valueOf(fileSize);
            RandomAccessFile randomAccessfile = null;
            String fn =request.getParameter("fileName");
            long currentFileLength = 0;// 记录当前文件大小，用于判断文件是否上传完成
            if(fn != null && !fn.equals("")){
            	 if(fn.equals(new String(fn.getBytes("iso8859-1"), "iso8859-1")))
                 {
                 	fn=new String(fn.getBytes("iso8859-1"),"utf-8");
                 }
            }

            String fileName = fn;
//          String fileName = java.net.URLDecoder.decode(filename_) ;
         // 不存在文件，根据文件标识创建文件
            randomAccessfile = new RandomAccessFile(currentFilePath+fileName, "rw");
            // 开始文件传输
            InputStream in = request.getInputStream();
            randomAccessfile.seek(randomAccessfile.length());
            byte b[] = new byte[1024];
            int n;
            while ((n = in.read(b)) != -1) {
                randomAccessfile.write(b, 0, n);
            }
            currentFileLength = randomAccessfile.length();
            // 关闭文件
            closeRandomAccessFile(randomAccessfile);

            randomAccessfile = null;
            // 整个文件上传完成,修改文件后缀
            String fileCommment =bgxz.getFileComment();
            if(fileCommment != null && !fileCommment.equals("")){
            	if(fileCommment.equals(new String(fileCommment.getBytes("iso8859-1"), "iso8859-1")))
                {
                	fileCommment=new String(fileCommment.getBytes("iso8859-1"),"utf-8");
                }
            }
            IUser user =(IUser)request.getSession().getAttribute("ssoUSER");

            String FilePath = FileUtil.buildFullFilePath(currentFilePath, fileName);
            String downUrl =buildDownloadUrl(fileName,bgxz.getAudTrm(),bgxz.getFocusCd());
			logger.error("start upload noti file to ftp>>"+FilePath+">"+getFtpPath(bgxz.getAudTrm(),bgxz.getFocusCd()));
    		bgxz.setAudTrm(bgxz.getAudTrm());
    		bgxz.setSubjectId(bgxz.getSubjectId());
    		bgxz.setFocusCd(bgxz.getFocusCd());
    		bgxz.setPrvdId(bgxz.getPrvdId());
    		bgxz.setFileType(bgxz.getFileType());
    		bgxz.setOperPerson(user.getUsername());
    		bgxz.setLoginAccount(user.getUserid());
    		bgxz.setFileComment(fileCommment);
    		bgxz.setFilePath(downUrl);
    		bgxz.setFileUploadDate(new Date());
    		bgxz.setStatus("wait");
    		bgxz.setFileName(fileName);
    		bgxzService.uploadReportLog(bgxz);
    		uploadFile(FilePath, getFtpPath(bgxz.getAudTrm(),bgxz.getFocusCd()));

    		print = response.getWriter();
            print.print(currentFileLength);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @Description:
     *             接受文件分片，合并分片
     * @param guid
     *             可省略；每个文件有自己唯一的guid，后续测试中发现，每个分片也有自己的guid，所以不能使用guid来确定分片属于哪个文件。
     * @param md5value
     *             文件的MD5值
     * @param chunks
     *             当前所传文件的分片总数
     * @param chunk
     *             当前所传文件的当前分片数
     * @param id
     *             文件ID，如WU_FILE_1，后面数字代表当前传的是第几个文件,后续使用此ID来创建临时目录，将属于该文件ID的所有分片全部放在同一个文件夹中
     * @param name
     *             文件名称，如07-中文分词器和业务域的配置.avi
     * @param type
     *             文件类型，可选，在这里没有用到
     * @param lastModifiedDate 文件修改日期，可选，在这里没有用到
     * @param size  当前所传分片大小，可选，没有用到
     * @param file  当前所传分片
     * @return
     * @author: xiangdong.she
     * @date: Aug 20, 2017 12:37:56 PM
     */
/*    @ResponseBody
    @RequestMapping(value = "/appendUpload2Server")
    public String fileUpload(HttpServletRequest request) {
    	String localfilepath =getLocalPath();
        JSONObject result=new JSONObject();
        try {
        	 System.out.println("进入后台...");
        	  // 1.创建DiskFileItemFactory对象，配置缓存用
        	  DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        	  // 2. 创建 ServletFileUpload对象
        	  ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
        	  // 3. 设置文件名称编码
        	  servletFileUpload.setHeaderEncoding("utf-8");
        	  // 4. 开始解析文件
        	  try {
        	   List<FileItem> items = servletFileUpload.parseRequest(request);
        	   for (FileItem fileItem : items) {
        	    if (fileItem.isFormField()) { // >> 普通数据
        	     String info = fileItem.getString("utf-8");
        	     System.out.println("info:" + info);
        	    } else { // >> 文件
        	     // 1. 获取文件名称
        	     String name = fileItem.getName();
        	     // 2. 获取文件的实际内容
        	     InputStream is = fileItem.getInputStream();
        	     // 3. 保存文件
        	     FileUtils.copyInputStreamToFile(is, new File(localfilepath  + name));
        	    }
        	   }
        	  } catch (Exception e) {
        	   e.printStackTrace();
        	  }
        } catch (Exception ex) {
            ex.printStackTrace();
            result.put("code", 0);
            result.put("msg", "上传失败");
            result.put("data", null);
            return result.toString();
        }
        result.put("code", 1);
        result.put("msg", "上传成功");
        return result.toString();
    }*/


    @RequestMapping(value = "sendMessage")
    public  void sendMessage(HttpServletRequest request,HttpServletResponse response) {

    	IUser user =(IUser)request.getSession().getAttribute("ssoUSER");
    	String isSendSms = propertyUtil.getPropValue("isSendSms");
		if("true".equals(isSendSms)){
			//上传完发送短信通知审批人 马煜静
    		String content="您好，持续审计系统"+user.getUsername()+"用户于"+sdf.format(new Date())+"提交了新文件，请及时进行系统审批操作";
//    		sendSms("13601174321",content);
    		BgxzData bgxz = new BgxzData();
    		bgxz.setFlag("review");
    		List<Map<String,Object>> plist = bgxzService.selReportPerson(bgxz);
    		for(Map<String,Object> d :plist){
    			if(d.get("phone") != null && !d.get("phone").equals("")){
    				if(sendSms(String.valueOf(d.get("phone")),content))
    					smsService.insertSmsSendTmp(String.valueOf(d.get("phone")), content, String.valueOf(d.get("user_id")));
    			}
    		}

		}
    }

/*    @RequestMapping(value = "webUploader")
    @ResponseBody
    public void webUploader(@RequestParam("file") MultipartFile file,HttpServletRequest request,
    		HttpServletResponse response) throws Exception {
    	String currentFilePath = getLocalPath();// 记录当前文件的绝对路径
    	String fileName = file.getOriginalFilename();
    	  if(fileName != null && !fileName.equals("")){
         	 if(fileName.equals(new String(fileName.getBytes("iso8859-1"), "iso8859-1")))
              {
         		fileName=new String(fileName.getBytes("iso8859-1"),"utf-8");
              }
         }
		File f = new File(currentFilePath+fileName);
		file.transferTo(f);
		f.createNewFile();
    }  */

  @RequestMapping(value = "webUploader")
    @ResponseBody
    public void webUploader(@RequestParam("file") MultipartFile file,HttpServletRequest request,
    		HttpServletResponse response,String fileComment,String audTrm,String prvdId,String focusCd,
    		String subjectId,String fileType) throws Exception {
    	String currentFilePath = getLocalPath();// 记录当前文件的绝对路径
    	String fileName = file.getOriginalFilename();
    	  if(fileName != null && !fileName.equals("")){
         	 if(fileName.equals(new String(fileName.getBytes("iso8859-1"), "iso8859-1")))
              {
         		fileName=new String(fileName.getBytes("iso8859-1"),"utf-8");
              }
         }
		File f = new File(currentFilePath+fileName);
		file.transferTo(f);
		f.createNewFile();
//		  String fileCommment =bgxz.getFileComment();
          if(fileComment != null && !fileComment.equals("")){
          	if(fileComment.equals(new String(fileComment.getBytes("iso8859-1"), "iso8859-1")))
              {
          		fileComment=new String(fileComment.getBytes("iso8859-1"),"utf-8");
              }
          }
          IUser user =(IUser)request.getSession().getAttribute("ssoUSER");

          String FilePath = FileUtil.buildFullFilePath(currentFilePath, fileName);
          String downUrl =buildDownloadUrl(fileName,"000000","0000");
			logger.error("start upload noti file to ftp>>"+FilePath+">"+getFtpPath(audTrm,focusCd));
			BgxzData bgxz=new BgxzData();
  		bgxz.setAudTrm(audTrm);
  		bgxz.setSubjectId(subjectId);
  		bgxz.setFocusCd(focusCd);
  		bgxz.setPrvdId(Integer.parseInt(prvdId));
  		bgxz.setFileType(fileType);
  		bgxz.setOperPerson(user.getUsername());
  		bgxz.setLoginAccount(user.getUserid());
  		bgxz.setFileComment(fileComment);
  		bgxz.setFilePath(downUrl);
  		bgxz.setFileUploadDate(new Date());
  		bgxz.setStatus("wait");
  		bgxz.setFileName(fileName);
  		bgxz.setFileUploadDate(new Date());
  		bgxzService.uploadReportLog(bgxz);
  		uploadFile(FilePath, getFtpPath("000000","0000"));

	  	deleteFile(currentFilePath+fileName);
	  	logger.error("上传完毕，已删除暂存文件");

    }

    /**
	 * <pre>
	 * Desc  调用接口发送短信
	 * @param phone
	 * @param content
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   Mar 9, 2015 6:27:08 PM
	 * </pre>
	 */
	private boolean sendSms(String phone, String content) {
		this.logger.debug("发送的短信:phone=" + phone + ",content=" + content);
		boolean issuccess=true;
		SSO_WebServiceLocator locator = new SSO_WebServiceLocator();
		String smsWebService = propertyUtil.getPropValue("smsWebService");
		locator.setSSO_WebServiceSoapEndpointAddress(smsWebService);

		try {
			SSO_WebServiceSoap_PortType smsServicePortType = locator.getSSO_WebServiceSoap();
			return smsServicePortType.sendSMS(phone, content);
		} catch (Exception e) {
			issuccess=false;
			logger.error("发送短信异常.phone=" + phone + ",content=" + content, e);
		}
		return issuccess;
	}

    /**
     * 关闭随机访问文件
     *
     * @param randomAccessfile
     */
    public  void closeRandomAccessFile(RandomAccessFile rfile) {
        if (null != rfile) {
            try {
                rfile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getLocalPath() {
		String tempDir = propertyUtil.getPropValue("tempDirUploadTmp");
		FileUtil.mkdirs(tempDir);
		return propertyUtil.getPropValue("tempDirUploadTmp");
	}

    private FtpUtil initFtp() {
		String isTransferFile = propertyUtil.getPropValue("isTransferFile");
		if (!"true".equalsIgnoreCase(isTransferFile)) {
			return null;
		}
		FtpUtil ftpTool = new FtpUtil();
		String ftpServer = StringUtils.trimToEmpty(propertyUtil
				.getPropValue("ftpServer"));
		String ftpPort = StringUtils.trimToEmpty(propertyUtil
				.getPropValue("ftpPort"));
		String ftpUser = StringUtils.trimToEmpty(propertyUtil
				.getPropValue("ftpUser"));
		String ftpPass = StringUtils.trimToEmpty(propertyUtil
				.getPropValue("ftpPass"));
		ftpTool.initClient(ftpServer, ftpPort, ftpUser, ftpPass);
		return ftpTool;
	}

	public void uploadFile(String filePath, String ftpPath){
		FtpUtil ftpTool = null;
		try {
			ftpTool = initFtp();
			if (ftpTool == null) {
				return;
			}
			ftpTool.uploadFile(new File(filePath), ftpPath);
		} catch (Exception e){
			e.printStackTrace();
			logger.error("NotiFile uploadFile>>>"+e.getMessage(),e);
		} finally {
			if (ftpTool != null) {
				ftpTool.disConnect();
			}
		}
	}
	public String getFtpPath(String audtrm, String focusCd){
		String tempDir = propertyUtil.getPropValue("ftpPath");
		String finalPath = FileUtil.buildFullFilePath(tempDir, buildRelativePath(audtrm, focusCd));
		FileUtil.mkdirs(finalPath);
		return finalPath;// + "/" + zipFileName;
	}

	public String getFtpPathTmp(String audtrm, String focusCd){
		String tempDir = propertyUtil.getPropValue("ftpPathTmp");
		String finalPath = FileUtil.buildFullFilePath(tempDir, buildRelativePath(audtrm, focusCd));
		FileUtil.mkdirs(finalPath);
		return finalPath;// + "/" + zipFileName;
	}

	protected String buildRelativePath(String audTrm, String focusCd) {
		String subjectId = focusCd.substring(0,1);
		StringBuilder path = new StringBuilder();
		path.append(audTrm).append("/").append(subjectId).append("/").append(focusCd);
		return path.toString();
	}

	protected String buildDownloadUrl(String zipFileName,String audtrm,String focusCd) {
		String ftpHttpUrlPrefix = propertyUtil.getPropValue("ftpHttpUrlPrefix");
		StringBuilder url = new StringBuilder(30);
		url.append(buildRelativePath(audtrm, focusCd)).append("/").append(zipFileName);
		return FileUtil.buildFullFilePath(ftpHttpUrlPrefix, url.toString());
	}

	protected String buildDownloadUrlTmp(String zipFileName,String audtrm,String focusCd) {
		String ftpHttpUrlPrefix = propertyUtil.getPropValue("ftpHttpUrlPrefixTmp");
		StringBuilder url = new StringBuilder(30);
		url.append(buildRelativePath(audtrm, focusCd)).append("/").append(zipFileName);
		return FileUtil.buildFullFilePath(ftpHttpUrlPrefix, url.toString());
	}



	/**MultipartFile类方法
	 * String getContentType()//获取文件MIME类型
		InputStream getInputStream()//后去文件流
		String getName() //获取表单中文件组件的名字
		String getOriginalFilename() //获取上传文件的原名
		long getSize()  //获取文件的字节大小，单位byte
		boolean isEmpty() //是否为空
		void transferTo(File dest)
		//保存到一个目标文件中。
	 */
	  @RequestMapping(value = "upload")
	    @ResponseBody
	  	public String  upload(HttpServletRequest request) {

		// 文件保存路径
          String filePath = request.getSession().getServletContext().getRealPath("/") + "upload";

		  		MultipartHttpServletRequest mhs =  (MultipartHttpServletRequest)request;

	            //根据input中存在的name来获取是否存在上传文件
	            MultipartFile mff = mhs.getFile("mfile");
	            //#这里可以用getFiles("file")的方式来处理多个文件。返回的是一个list.然后一个一个处理就可以了
	            List<MultipartFile> mfs = mhs.getFiles("mfile");
	            if(mfs != null && mfs.size() > 0){
	            	for(MultipartFile mf: mfs){
	            		 //创建文件保存名
	                    File file = new File(filePath+"/"+mf.getOriginalFilename());
	                    //上传文件
	                    try {
	        				mf.transferTo(file);
	        			} catch (IllegalStateException e) {
	        				e.printStackTrace();
	        			} catch (IOException e) {
	        				e.printStackTrace();
	        			}
	            	}
	            	return "success";
	            }else{
	            	return "fail";
	            }

	    }

	    @RequestMapping(value = "toupload")
	    public String toupload() {
		return "test/index";
	    }


	/**
	 * 删除单个文件
	 *
	 * @param fileName 要删除的文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	private boolean deleteFile(String fileName) {
		File file = new File(fileName);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				logger.error("删除单个文件" + fileName + "成功！");
				return true;
			} else {
				logger.error("删除单个文件" + fileName + "失败！");
				return false;
			}
		} else {
			logger.error("删除单个文件失败：" + fileName + "不存在！");
			return false;
		}
	}
	/**
	 * 删除目录及目录下的文件
	 *
	 * @param dir 要删除的目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	private boolean deleteDirectory(String dir) {
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(File.separator))
			dir = dir + File.separator;
		File dirFile = new File(dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
			logger.error("删除目录失败：" + dir + "不存在！");
			return false;
		}
		boolean flag = true;
		// 删除文件夹中的所有文件包括子目录
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
			// 删除子目录
			else if (files[i].isDirectory()) {
				flag = deleteDirectory(files[i]
						.getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag) {
			logger.error("删除目录失败！");
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			logger.error("删除目录" + dir + "成功！");
			return true;
		} else {
			return false;
		}
	}
}
