package com.hpe.cmca.common;

import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.job.v2.Word0000FileGenProcessor;
import com.hpe.cmca.pojo.FileGenData;
import com.hpe.cmca.util.SpringContextHolder;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class FileGenThread extends Thread {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FileGenThread.class);
    private FileGenData fileGenData;
    private AsposeUtil asposeUtil;
    private MybatisDao mybatisDao;
    private FilePropertyPlaceholderConfigurer propertyUtil;
    private Word0000FileGenProcessor wfg;

    public FileGenThread(FileGenData fileGenData) {
        this.fileGenData=fileGenData;
        this.asposeUtil = SpringContextHolder.getBean("asposeUtil");
        this.mybatisDao = SpringContextHolder.getBean("mybatisDao");
        this.propertyUtil = SpringContextHolder.getBean("propertyUtil");
        this.wfg = SpringContextHolder.getBean("wfg");
    }

    public void run() {
//        try {
//            String fileFinalName = this.asposeUtil.work(fileGenData);
//            if (fileFinalName != null) {
//                wfg.uploadFile(new File(propertyUtil.getPropValue("tmpFile") + "/" + fileFinalName), propertyUtil.getPropValue("fileConfigFtpDir") + "/" + fileGenData.getAudTrm() + "/" + fileGenData.getSubjectId());
//                FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
//                Map<String, Object> paraMap = new HashMap<>();
//                paraMap.put("audTrm", fileGenData.getAudTrm());
//                paraMap.put("prvdId", fileGenData.getPrvdId());
//                paraMap.put("fileCode", fileGenData.getFileCode());
//                paraMap.put("fileName", fileFinalName);
//                paraMap.put("filePath", propertyUtil.getPropValue("fileConfigFtpUrl") + "/" + fileGenData.getAudTrm() + "/" + fileGenData.getSubjectId() + "/" + fileFinalName);
//                fileGenMapper.insertGenRecord(paraMap);
//                String dir = propertyUtil.getPropValue("tmpFile");
//                Map<String, Object> paramMap = new HashMap<String, Object>();
//                paramMap.put("subjectId", fileGenData.getSubjectId());
//                paramMap.put("fileName", fileGenData.getFileName());
//                List<Map<String, Object>> tpList = fileGenMapper.selectZipRule(paramMap);
//                for (Map<String, Object> tpMap : tpList) {
//                    String zipName = tpMap.get("zip_name").toString();
//                    Map<String, Object> tpParaMap = new HashMap<String, Object>();
//                    tpParaMap.put("audTrm", fileGenData.getAudTrm());
//                    tpParaMap.put("prvdName", Constants.MAP_PROVD_NAME.get(Integer.parseInt(fileGenData.getPrvdId())));
//                    zipName = wfg.replaceParam(zipName, tpParaMap);
//                    String folderName = zipName.replaceAll(".zip", "");
//                    synchronized (FileGenThread.class) {
//                        if (Files.exists(Paths.get(dir, zipName))) {
//                            if (ZipUtil.containsEntry(Paths.get(dir, zipName).toFile(), fileFinalName)) {
//                                ZipUtil.replaceEntry(Paths.get(dir, zipName).toFile(), fileFinalName, Paths.get(dir, fileFinalName).toFile(), Paths.get(dir, "tp.zip").toFile());
//                            } else {
//                                ZipUtil.addEntry(Paths.get(dir, zipName).toFile(), fileFinalName, Paths.get(dir, fileFinalName).toFile(), Paths.get(dir, "tp.zip").toFile());
//                            }
//                            Files.move(Paths.get(dir, "tp.zip"), Paths.get(dir, zipName), StandardCopyOption.REPLACE_EXISTING);
//                        } else {
//                            try {
//                                Files.createDirectory(Paths.get(dir, folderName));
//                            } catch (Exception e) {
//                                logger.error("DIR:" + dir + "/" + folderName + "has already existed");
//                            }
//                            Files.copy(Paths.get(dir, fileFinalName), Paths.get(dir + "/" + folderName, fileFinalName), StandardCopyOption.REPLACE_EXISTING);
//                            ZipUtil.pack(Paths.get(dir, folderName).toFile(), Paths.get(dir, zipName).toFile());
//                            delete(Paths.get(dir, folderName));
//                        }
//                        wfg.uploadFile(new File(propertyUtil.getPropValue("tmpFile") + "/" + zipName), propertyUtil.getPropValue("fileConfigFtpDir") + "/" + fileGenData.getAudTrm() + "/" + fileGenData.getSubjectId());
//                        paraMap.put("fileCode",  "0");
//                        paraMap.put("fileName", zipName);
//                        paraMap.put("filePath", propertyUtil.getPropValue("fileConfigFtpUrl") + "/" +  fileGenData.getAudTrm() + "/" +  fileGenData.getSubjectId() + "/" + zipName);
//                        fileGenMapper.deleteGenRecord(paraMap);
//                        fileGenMapper.insertGenRecord(paraMap);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            logger.error("Thread:" + this.getName() + "-Error");
//            e.printStackTrace();
//        }
    }

    public void delete(Path path) {
        final Path rootPath = path;
        try {
            Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    System.out.println("delete file: " + file.toString());
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    System.out.println("delete dir: " + dir.toString());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
