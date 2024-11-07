package dev.zeronelab.mybatis.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

import dev.zeronelab.mybatis.util.MediaUtils;
import dev.zeronelab.mybatis.util.UploadFileUtils;
import dev.zeronelab.mybatis.vo.BoardEntity;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {

  private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

  @Value("${dev.popup.upload.path}")
  private String uploadPath;

  @RequestMapping(value = "/uploadForm", method = RequestMethod.GET)
  public void uploadForm() {
  }

  @RequestMapping(value = "/uploadForm", method = RequestMethod.POST)
  public String uploadForm(MultipartFile file, Model model) throws Exception {

    logger.info("originalName: " + file.getOriginalFilename());
    logger.info("size: " + file.getSize());
    logger.info("contentType: " + file.getContentType());

    String savedName = uploadFile(file.getOriginalFilename(), file.getBytes());

    model.addAttribute("savedName", savedName);

    return "uploadResult";
  }

  @RequestMapping(value = "/uploadAjax", method = RequestMethod.GET)
  public void uploadAjax() {
  }

  private String uploadFile(String originalName, byte[] fileData) throws Exception {

    UUID uid = UUID.randomUUID();

    String savedName = uid.toString() + "_" + originalName;

    File target = new File(uploadPath, savedName);

    FileCopyUtils.copy(fileData, target);

    return savedName;

  }
  
  @ResponseBody
  @RequestMapping(value ="/uploadAjax", method=RequestMethod.POST, 
                  produces = "text/plain;charset=UTF-8")
  public ResponseEntity<String> uploadAjax(MultipartFile file)throws Exception{
    
    logger.info("originalName: " + file.getOriginalFilename());
    
   
    return 
      new ResponseEntity<>(
          UploadFileUtils.uploadFile(uploadPath,
                file.getOriginalFilename(), 
                file.getBytes()), 
          HttpStatus.CREATED);
  }
  
  
  @ResponseBody
  @RequestMapping("/displayFile")
  public ResponseEntity<byte[]>  displayFile(String fileName)throws Exception{
    
    InputStream in = null; 
    ResponseEntity<byte[]> entity = null;
    
    logger.info("FILE NAME: " + fileName);
    
    try{
      
      String formatName = fileName.substring(fileName.lastIndexOf(".")+1);
      
      MediaType mType = MediaUtils.getMediaType(formatName);
      
      HttpHeaders headers = new HttpHeaders();
      
      in = new FileInputStream(uploadPath+fileName);
      
      if(mType != null){
        headers.setContentType(mType);
      }else{

        fileName = fileName.substring(fileName.indexOf("_")+1);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add("Content-Disposition", "attachment; filename=\""+
          new String(fileName.getBytes("UTF-8"), "ISO-8859-1")+"\"");
      }

        entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), 
          headers, 
          HttpStatus.CREATED);
    }catch(Exception e){
      e.printStackTrace();
      entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
    }finally{
      in.close();
    }
      return entity;    
  }
    
  @ResponseBody
  @RequestMapping(value="/deleteFile", method=RequestMethod.POST)
  public ResponseEntity<String> deleteFile(@RequestBody String fileName){

    logger.info("delete file: "+ fileName);

    String formatFileName = fileName.substring(fileName.indexOf("/"), fileName.lastIndexOf('"'));

    logger.info("formatFileName: "+formatFileName);
    
    String formatTypeName = formatFileName.substring(formatFileName.lastIndexOf(".")+1);

    logger.info("formatName: "+formatTypeName);
    
    MediaType mType = MediaUtils.getMediaType(formatTypeName);

    logger.info("mTpye: "+mType);

    logger.info(uploadPath + formatFileName.replace('/', File.separatorChar));
    if(mType != null){
      String front = formatFileName.substring(0,12);
      String end = formatFileName.substring(14);
      new File(uploadPath + (front+end).replace('/', File.separatorChar)).delete();
    }
    
    new File(uploadPath + formatFileName.replace('/', File.separatorChar)).delete();

    
    return new ResponseEntity<String>("deleted", HttpStatus.OK);
  }  
  
  @ResponseBody
  @RequestMapping(value="/deleteAllFiles", method=RequestMethod.POST)
  public ResponseEntity<String> deleteFile(@RequestBody BoardEntity allFile){

    String[] files = allFile.getFiles();

    logger.info("delete all files: "+ files);

    if(files == null || files.length == 0) {
      return new ResponseEntity<String>("deleted", HttpStatus.OK);
    }

    for (String fileName : files) {
      String formatName = fileName.substring(fileName.lastIndexOf(".")+1);
      
      MediaType mType = MediaUtils.getMediaType(formatName);
      
      if(mType != null){      
        
        String front = fileName.substring(0,12);
        String end = fileName.substring(14);
        new File(uploadPath + (front+end).replace('/', File.separatorChar)).delete();
      }
      
      new File(uploadPath + fileName.replace('/', File.separatorChar)).delete();
      
    }
    return new ResponseEntity<String>("deleted", HttpStatus.OK);
  }  

}
//  @ResponseBody
//  @RequestMapping(value = "/uploadAjax", 
//                 method = RequestMethod.POST, 
//                 produces = "text/plain;charset=UTF-8")
//  public ResponseEntity<String> uploadAjax(MultipartFile file) throws Exception {
//
//    logger.info("originalName: " + file.getOriginalFilename());
//    logger.info("size: " + file.getSize());
//    logger.info("contentType: " + file.getContentType());
//
//    return 
//        new ResponseEntity<>(file.getOriginalFilename(), HttpStatus.CREATED);
//  }

// @RequestMapping(value = "/uploadForm", method = RequestMethod.POST)
// public void uploadForm(MultipartFile file, Model model) throws Exception {
//
// logger.info("originalName: " + file.getOriginalFilename());
// logger.info("size: " + file.getSize());
// logger.info("contentType: " + file.getContentType());
//
// String savedName = uploadFile(file.getOriginalFilename(), file.getBytes());
//
// model.addAttribute("savedName", savedName);
//
// }
//
// private String uploadFile(String originalName, byte[] fileData)throws
// Exception{
//
// UUID uid = UUID.randomUUID();
//
// String savedName = uid.toString() + "_"+ originalName;
//
// File target = new File(uploadPath,savedName);
//
// FileCopyUtils.copy(fileData, target);
//
// return savedName;
//
// }
