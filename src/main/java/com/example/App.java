package com.example;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.*;

public class App 
{
    public static void main( String[] args ) throws IOException
    {   
        while(true){
            ServerSocket ss = new ServerSocket(8080);
            System.out.println("Server ascolta sulla porta 8080\n");
            Socket s = ss.accept();

            BufferedReader   inDalClient = new BufferedReader(new InputStreamReader((s.getInputStream())));
            DataOutputStream outVersoClient = new DataOutputStream(s.getOutputStream());
            
            String linea1 = inDalClient.readLine();
            System.out.println(linea1);
            String sr = "";
            String [] filePath = linea1.split(" ");
            do{
                sr = inDalClient.readLine();
                System.out.println(sr);
            } while(!sr.isEmpty());

            File file = new File("htdocs" + filePath);
            String[] parti = linea1.split(" ");
            System.out.println(parti[1]);

            try{
                File myObj = new File(parti[1].substring(1));
                Scanner myRedear = new Scanner(myObj);

                outVersoClient.writeBytes(
                        "HTTP/1.1 200 Ok\n" + 
                        "Date: " + new Date() + "\n" +
                        "content-length:" + myObj.length() + "\n" + 
                        "Server: meucci-server\n" + 
                        "Content-Type: text/plain; charset=UTF-8\n" +
                        "\n");;

                while (myRedear.hasNextLine()) {
                    String data = myRedear.nextLine();
                    System.out.println(data + "\n");
                }
                myRedear.close();
            } catch(FileNotFoundException e){
                outVersoClient.writeBytes(
                        "HTTP/1.1 404 Not Found\n" + 
                        "Date: " + new Date() + "\n" +
                        "Server: meucci-server\n" + 
                        "Content-Type: text/plain; charset=UTF-8\n" + 
                        "Content-Length: 26\n" + 
                        ""
                        );
            }

            s.close();
            
        }
        
        
    }
    private static void sendBinaryFile (DataOutputStream out, File file) throws IOException{
        out.writeBytes("HTTP/1.1 200 OK");
        out.writeBytes("Content-lenth: " + file.length() + "\n");
        out.writeBytes("Content-Type: " + getContentType(file) + "\n");
        out.writeBytes("\n");
        InputStream in = new FileInputStream(file);
        byte[] buf = byte(8192);
        int n;
        while((n = in.read(buf)) != -1) {
            out.write(buf, 0, n);
        }
        in.close();
    }

    private static String getContentType (File file){
        String [] s = file.getName().split("\\.");
        String ext = s[s.length-1];
        switch (ext) {
            case "html":
                case "htm":
                    return "text/html";
                case "jpeg":
                    return "images/jpeg";
                case "css":
                    return "text/css";
        }
        return ext;
    }
}
