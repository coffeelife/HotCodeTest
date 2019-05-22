package com.hotcode.test.util;

import java.io.*;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javazoom.upload.MultipartFormDataRequest;
import javazoom.upload.UploadBean;
import javazoom.upload.UploadFile;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

public class FileUtil {
	private static String blackList = "*.exe,*.bat,*.com,*.jsp,*.php,*.asp,*.aspx,*.js";
	
	public static String encodeBase64File(File file) throws Exception {   
        FileInputStream inputFile = new FileInputStream(file);   
        byte[] buffer = new byte[(int)file.length()];
        
        inputFile.read(buffer);   
        inputFile.close();   
        return new BASE64Encoder().encode(buffer);
    }
	
	public static void decoderBase64File(String base64Code,String targetPath) throws Exception { 
        byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code); 
        FileOutputStream out = new FileOutputStream(targetPath);   
        out.write(buffer);   
        out.close();
    }

	//读取文件
	public static String readFile(String path,String encode) throws Exception{
		StringBuffer sb = new StringBuffer();
		FileInputStream fis = new FileInputStream(path);
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis,encode));
		String str = reader.readLine() ;
		while(str != null){
			sb.append(str).append("\r\n");
			str=reader.readLine();
		}
		fis.close();
		return sb.toString();
	}
	
	public static String readFile(String path) throws Exception{
		return readFile(path, "utf-8");
	}
	
	//判断文件是否存在
	public static boolean haveFile(String path) throws Exception{
		File file = new File(path);
		return file.exists();
	}
	
	//创建文件，并格式化内容
	public static void createFile(String path,String fileName,String content,String encode) throws Exception{
		path = path.replace("\\", "/");
		createFolder(path);
		
		OutputStreamWriter fos = new OutputStreamWriter(new FileOutputStream(new File(path + "/" + fileName)), encode);
		fos.write(content);
		fos.flush(); 
		fos.close();
 	}
	
	public static void createFile(String path,String fileName,String content) throws Exception{
		createFile(path, fileName, content, "utf-8");
 	}
	
	//创建目录
	public static void createFolder(String path) throws Exception{
		path = path.replace("\\", "/");
		String f[] = path.split("/");
		path = "";
		for(int i = 0; i < f.length; i++){
			path = path + "/" + f[i];
			File file = new File(path);
			if(!file.exists()){
				file.mkdir();	
			}
		}
	}
	
	//删除文件
	public static void deleteFile(String path){
		File file = new File(path);
		delete(file);
	}
	
	//删除文件的具体执行类
	public static void delete(File file){
		if(file.isDirectory()){
			for(File f : file.listFiles()){
				if(f.isFile())
					f.delete();
				else if(f.isDirectory())
					delete(f);
			}
		}
		file.delete();
	}
	
	//拷贝文件
	public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try{
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
        }finally{
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }
	
	//获取根路径
	public static String getRootPath(HttpServletRequest request){
		if(!PropertyUtils.getBooleanProperty("bAbsolute")){
			return Common.getRootPath(request);
		}else{
			return PropertyUtils.getProperty("absolutePath");
		}
	}
	
	//文件上传
	public static String save(MultipartFormDataRequest mrequest, String s, String rootPath, String uploadDir, String tmpDir, String whiteList) throws Exception{
		if(tmpDir == null)
			tmpDir = PropertyUtils.getProperty("tmpDir");
		
		if(uploadDir == null)
			uploadDir = PropertyUtils.getProperty("uploadDir");
		
		createFolder(rootPath + uploadDir);
		
		FileMover fileMover = new FileMover();
		UploadBean uploadBean = new UploadBean();
		
		uploadBean.setBlacklist(blackList);
		if(whiteList != null && !whiteList.equals(""))
			uploadBean.setWhitelist(whiteList);

		UploadFile file = (UploadFile) mrequest.getFiles().get(s);
		if(file != null && file.getFileSize() > 0){
			String fileType = file.getFileName().substring(file.getFileName().lastIndexOf(".")+1).toLowerCase();
				
			try {
				//将原始文件以覆盖形式上传到临时目录
				uploadBean.setOverwrite(true);
				uploadBean.setFolderstore(rootPath + tmpDir);
				uploadBean.addUploadListener(fileMover);
					
				//上传完毕后，以正式名移入正式目录
				fileMover.setAltFolder(rootPath + uploadDir);
				fileMover.setPrefix(UUID.randomUUID().toString());
				fileMover.setPostfix(fileType);
					
				uploadBean.store(mrequest,s);
			} catch (Exception e) {
				return "";
			}
		}
		return uploadDir + "/" + fileMover.getNewfilename();
	}
	
	public static String save(MultipartFormDataRequest mrequest,String s, String rootPath) throws Exception{
		return save(mrequest, s, rootPath, null, null, null);
	}
	
	public static void download(HttpServletRequest request,HttpServletResponse response,String sourceFile,String fileName) throws Exception{
		fileName = Common.formatUTF8(fileName);
			
		response.setContentType("application/x-msdownload");
		response.setHeader("Content-Disposition", "attachment;filename="+fileName);
		
		File f = new File(sourceFile);
		FileInputStream fileIn = new FileInputStream(f);
		long fileLen = f.length();

		int readBytes = 0;
        int totalRead = 0;
        int blockSize = 65000;
        byte b[] = new byte[blockSize];

        while((long)totalRead < fileLen){
            readBytes = fileIn.read(b, 0, blockSize);
            totalRead += readBytes;
            response.getOutputStream().write(b, 0, readBytes);
        }
        fileIn.close();
	}
}
