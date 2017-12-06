package com.ftp.server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;

public class ReadFTPFILE {

	public static void main(String[] args) throws SocketException, IOException {
		
		
		FTPClient ftpClient = new FTPClient();
        FileOutputStream fos = null;
        boolean result;
        try {
                // Connect to the localhost
                ftpClient.connect("10.144.2.164");

                // login to ftp server
                result = ftpClient.login("testuser", "test");
                if (result == true) {
                        System.out.println("Successfully logged in!");
                } else {
                        System.out.println("Login Fail!");
                        return;
                }
                String fileName = "Travelbook-Mobiles-Internet-2016-08-04-V1.0.pdf";
                fos = new FileOutputStream(fileName);

                // Download file from the ftp server
                result = ftpClient.retrieveFile(fileName, fos);

                if (result == true) {
                        System.out.println("File downloaded successfully !");
                } else {
                        System.out.println("File downloading failed !");
                }
                
                ftpClient.logout();
        } catch (FTPConnectionClosedException e) {
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

