package com.ftp.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.QueryParam;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.json.JSONException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wmdaten.controller.FileReadFromWMDatenFTPController;

//import com.wmdaten.controller.FileReadFromWMDatenFTPController;

@RestController
public class ReadFtpFileController {

	
	FTPClient ftpClient = new FTPClient();
	FileOutputStream fos = null;
    boolean result;
    InetAddress ipaddress;
    String ip;
	String destinationPath="C:\\WORK STATION\\MicroservicesHrms\\FTPServerConnection\\downloadftp";
	FileReadFromWMDatenFTPController fileReadFromWMDatenFTPController;
	String extractedDestPath="C:\\WORK STATION\\MicroservicesHrms\\FTPServerConnection\\extracted";
	
    @PostConstruct
	void init() throws SocketException, IOException{
    	
    	ipaddress = InetAddress.getLocalHost(); 
    	ip = ipaddress.getHostAddress().toString();
    	ftpClient.connect(ip);
        result = ftpClient.login("testuser", "test");
        
	}
    
    @GetMapping
    String downloadFile(@QueryParam(value="fileName")String fileName) throws IOException, JSONException{
    	
	    	if (result == true) {
	    		
	                System.out.println("Successfully logged in!");
		    } else {
		            //System.out.println("Login Fail!");
		            return "Login Fail!";
		    }
	    	  
	    	
	    		File destDir=new File(destinationPath);
	    		if (!destDir.exists()) {
	    			destDir.mkdir();
	    		} 
	    		fos = new FileOutputStream(destDir +File.separator+fileName);

	            // Download file from the ftp server
	            result = ftpClient.retrieveFile(fileName, fos);

	            if (result == true) {
	                    System.out.println("File downloaded successfully !");
	            } else {
	                    System.out.println("File downloading failed !");
	            }
	            
	           
	           
	    	return fileReadFromWMDatenFTPController.read(destinationPath+File.separator+fileName, extractedDestPath);

	    	
    	
    }
	@PreDestroy
    void destroy() throws IOException{
		
		System.out.println("*********Destroy()********");
		try {
			
			 ftpClient.logout();
		} catch (FTPConnectionClosedException e) {
  		  
            e.printStackTrace();
            
      } catch (IOException e) {
    	  
			e.printStackTrace();
			
		} finally {
              try {
                      if (fos != null) {
                             
                    	  fos.close();
                      }
                      
                      ftpClient.disconnect();
              
              } catch (FTPConnectionClosedException e) {
                     
            	  System.err.println(e);
              }
      }
		
    }
	
}
