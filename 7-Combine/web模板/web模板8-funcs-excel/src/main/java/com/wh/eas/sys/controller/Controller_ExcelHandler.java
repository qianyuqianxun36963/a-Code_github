package com.wh.eas.sys.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wh.eas.sys.service.FileProcessService;
import com.wh.eas.sys.utils.FileUtils;
import com.wh.eas.sys.utils.PropertiesUtil;
import com.wh.eas.sys.utils.UUIDTool;

@Controller
@RequestMapping("/excel")
public class Controller_ExcelHandler extends Controller_Base
{
	public static Logger logger = Logger.getLogger(Controller_FileProcess.class);
	
	/**默认上传用户的ID*/
	private static String uploadUserId = "-1";
	/**
	 * 
	 * 功能描述：获取已上传的文件大小
	 *
	 *
	 * @param [javax.servlet.http.HttpServletRequest] request <请求参数>
	 *
	 * @return [javax.servlet.http.HttpServletResponse] response <返回参数>
	 * 
	 * @author chenjunjin
	 *
	 * @date 2017年3月21日上午10:03:59
	 */
	@RequestMapping("/getChunkedFileInfo")
    public void getChunkedFileInfo(HttpServletRequest request,HttpServletResponse response) throws IOException
    {	

		/**
		 * 后期可能不同的来源文件，存储路径不同
		 * 存储文件的路径，根据自己实际确定
		 * 暂时所有的文件存储在固定目录下
		 */
        String currentFilePath = propertiesUtil.get("uploadFilePath");
        RandomAccessFile randomAccessfile = null;
	    JSONObject json = new JSONObject();
        PrintWriter print = null;
        try 
        {
            request.setCharacterEncoding("utf-8");
            print = response.getWriter();
            String fileName = request.getParameter("fileName");
            String fileSize = request.getParameter("fileSize");
            String lastModifyTime = request.getParameter("lastModifyTime");
            
            String fileType = fileName.substring(fileName.lastIndexOf(".")+1);                  
            String fileUploadType = request.getParameter("fileUploadType");
            String saveFilePathKey = request.getParameter("saveFilePathKey");
            String uploadFilePath = FileUtils.getSavaFilePathByType(currentFilePath,fileUploadType,saveFilePathKey);
            
            /**
             * 注：此处的文件验证，为了配合断点续传。
             * 当文件上传类型，用途，大小，文件名称，创建时间，上传用户均为同一个人的时候，我们认为这是一个文件。 
             * 因为项目做了单用户登录限制，所以不存在同一个用户同时上传一个文件的情况。
             */
            Map<String, Object> SaveFile = this.fileProcessService.getFileNameByInfo(fileName,fileSize,lastModifyTime,fileUploadType,uploadUserId);
            
            if(SaveFile!=null)
            {
            	 //数据库存在文件
            	 File file = new File(uploadFilePath+"/"+SaveFile.get("file_new_name"));
            	 /**
            	  * 此处资源库验证文件是否存在
            	  * 防止资源文件被删
            	  */
            	 if(file.exists())//资源库确实存在
            	 {
            		 json.put("status", "success");
            		 json.put("size", file.length());
                 	 json.put("name", SaveFile.get("file_new_name")); 
                 	 json.put("id", SaveFile.get("id"));
  	   	             json.put("downLoadPath", propertiesUtil.get("downLoadPath") + "/"+ propertiesUtil.get(saveFilePathKey) +"/"+ SaveFile.get("id") +"");
  	   	             json.put("resourcePath", propertiesUtil.get("resourcePath") + "/"+ propertiesUtil.get(fileUploadType) +"/" + propertiesUtil.get(saveFilePathKey) +"/"+ SaveFile.get("file_new_name")); 
                 }
            	 else
            	 {
            		 //资源库文件并不存在 删除数据库信息
            		 if(fileProcessService.deleteFileByInfo(fileName, fileSize, lastModifyTime, fileUploadType,"-1"))
            		 {//重新新增并上传
            			  String uuid = UUIDTool.randomUUID();//随机生成文件uuid  
            			  String Id = UUIDTool.randomUUID();//随机生成主键uuid  
	   	               	  File files = new File(uploadFilePath + "/" + uuid+ "." + fileType);  
	   	               	  randomAccessfile = new RandomAccessFile(files, "rw");
	   	               	  //insert 文件信息
	   	               	  this.fileProcessService.addFileByInfo(fileName, fileSize, lastModifyTime, fileUploadType,uploadUserId,uuid+"."+fileType,Id);
	   	               	  json.put("status", "success");
	            		  json.put("size", Integer.valueOf(propertiesUtil.get("uploadTypeFileSize").trim()));
	   	   	              json.put("name", uuid + "." + fileType);
	   	   	              json.put("id", Id);
	   	   	              json.put("downLoadPath", propertiesUtil.get("downLoadPath") + "/"+ propertiesUtil.get(saveFilePathKey) +"/"+ Id +"");
	   	   	              json.put("resourcePath", propertiesUtil.get("resourcePath") + "/"+ propertiesUtil.get(fileUploadType) +"/" + propertiesUtil.get(saveFilePathKey) +"/"+ uuid + "." + fileType);
	   	   	              randomAccessfile.close(); 
            		 }
            		 else
            		 {
            			  json.put("status", "fail");
            			  json.put("msg", "删除资源库文件异常");
            		 }
            		 
                 }
            }
            else
            {
            	  //数据库不存在文件
            	  String uuid = UUIDTool.randomUUID();//随机生成uuid   
            	  String Id = UUIDTool.randomUUID();//随机生成主键uuid  
            	  File file = new File(uploadFilePath + "/" + uuid + "." + fileType);  
            	  randomAccessfile = new RandomAccessFile(file, "rw");
            	  //insert 文件信息
            	  this.fileProcessService.addFileByInfo(fileName, fileSize, lastModifyTime, fileUploadType, uploadUserId,uuid+"."+fileType,Id);
            	  json.put("status", "success");
        		  json.put("size", Integer.valueOf(propertiesUtil.get("uploadTypeFileSize").trim()));
	   	          json.put("name", uuid + "." + fileType);
	   	          json.put("id", Id);
	   	          json.put("downLoadPath", propertiesUtil.get("downLoadPath") + "/" + propertiesUtil.get(saveFilePathKey) +"/"+ Id +"");
	   	          json.put("resourcePath", propertiesUtil.get("resourcePath") + "/" + propertiesUtil.get(fileUploadType) +"/" + propertiesUtil.get(saveFilePathKey) +"/"+ uuid + "." + fileType);
	              randomAccessfile.close();
            }
            //设置可以访问的域区间，可以是多个地址。
            
            print.write(json.toString());          
        } 
        catch (Exception e) 
        {	System.out.println(e.getStackTrace());
            logger.error("[获得文件名称和文件大小异常-ErrorMsg:]", e);
            json.put("status", "error");
			json.put("msg", "资源验证出错");
        }
        finally
        {
        	print.close();//关闭流
        }
        
    }
	
	
	/**
	 * 
	 * 功能描述： 断点文件上传 
	 *
	 *
	 * @param [javax.servlet.http.HttpServletRequest] request <请求参数>
	 *
	 * @return [javax.servlet.http.HttpServletResponse] response <返回参数>
	 * 
	 * @author chenjunjin
	 *
	 * @date 2017年3月21日上午10:17:08
	 */
	@RequestMapping("/appendUploadServer")
    public  void appendUploadServer(HttpServletRequest request,HttpServletResponse response) 
	{
        PrintWriter print = null;
        try 
        {
            request.setCharacterEncoding("utf-8");
            print = response.getWriter();
            String fileSize = request.getParameter("fileSize");
            String Fileurl = request.getParameter("Fileurl");
            String fileUploadType = request.getParameter("fileUploadType");
            String saveFilePathKey = request.getParameter("saveFilePathKey");
            long totalSize = Long.parseLong(fileSize);
            RandomAccessFile randomAccessfile = null;
            long currentFileLength = 0;// 记录当前文件大小，用于判断文件是否上传完成
            String currentFilePath = propertiesUtil.get("uploadFilePath");
            String uploadFilePath = FileUtils.getSavaFilePathByType(currentFilePath,fileUploadType,saveFilePathKey);
            File file = new File(uploadFilePath+"/"+Fileurl);
            // 存在文件
            if(file.exists())
            {
                randomAccessfile = new RandomAccessFile(file, "rw");
            }
            else 
            {
                // 不存在文件，根据文件标识创建文件
                randomAccessfile = new RandomAccessFile(uploadFilePath+"/"+Fileurl, "rw");
            }
            // 开始文件传输
            InputStream in = request.getInputStream();
            randomAccessfile.seek(randomAccessfile.length());//设置文件偏移量，该方法设置文件下次读取或写入位置
            byte b[] = new byte[1024*1024];//文件缓存区
            int n;
            while ((n = in.read(b)) != Integer.valueOf(propertiesUtil.get("uploadTypeFileSize").trim())) 
            {
                randomAccessfile.write(b, 0, n);//写入文件 0 为偏移量
            }

            currentFileLength = randomAccessfile.length();//获得写入之后的长度

            // 关闭文件
            closeRandomAccessFile(randomAccessfile);
            randomAccessfile = null;
            // 整个文件上传完成
            if (currentFileLength == totalSize) 
            {            
            	
            }
            print.print(currentFileLength);
            
            //设置可以访问的域区间，可以是多个地址。
        } 
        catch (Exception e) 
        {
        	 logger.error("[上传文件异常-ErrorMsg:]", e);
        }
       
    }
	
	/**
	 * 
	 * 功能描述：关闭随机访问文件
	 *
	 *
	 * @param [RandomAccessFile] rfile <需要关闭随机访问文件>
	 *
	 * 
	 * @author chenjunjin
	 *
	 * @date 2017年3月21日上午10:16:08
	 */
    public static void closeRandomAccessFile(RandomAccessFile rfile) 
    {
        if (null != rfile) 
        {
            try 
            {
                rfile.close();
            } 
            catch (Exception e) 
            {
            	logger.error("[关闭流异常-ErrorMsg:]", e);
            }
        }
    }
	
    
    /**业务层注入*/
    @Resource
	private FileProcessService fileProcessService;
    
    /**业务层注入*/
    @Resource
	private PropertiesUtil propertiesUtil;
    
    
    
}
