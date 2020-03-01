package example;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

/**
 * @Author: wangzhenze
 * @Description:
 * @Date: Created in 2020/2/25 7:47 下午
 */
public class Test {
    public static void main(String[] args) throws Exception{
        File file = new File("/Users/wangzhenze/Desktop/文字识别截图.png");
        FTPClient ftpClient = new FTPClient();
        String upFile = "文字识别截图.png";
        ftpClient.setControlEncoding("GBK");
        String hostname = "139.196.95.157";
        int port = 21;
        String username = "ftpuser";
        String password = "ftpuser";

        try {
            //链接ftp服务器
            ftpClient.connect(hostname, port);
            //登录ftp
            ftpClient.login(username, password);
            int  reply = ftpClient.getReplyCode();
            System.out.println(reply);
            //如果reply返回230就算成功了
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return ;
            }
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            String remoteFileName = "upload/"+upFile;
            InputStream input = new FileInputStream(file);
            boolean upload =ftpClient.storeFile(remoteFileName, input);//文件你若是不指定就会上传到根目录下，此时文件目录是   根目录/upload/66666.txt
            boolean rename = ftpClient.rename("/root/musicplayer/文字识别截图.png","/root/musicplayer/文字识别截图.png");//看起来只是改名，实际上可以实现文件位置移动，类似于剪切粘贴比如这里文件位置从upload/66666.txt移动到了dir/77777.txt，原有66666.txt文件会消失
            System.out.println("upload~~~~~~~~~~~~~~~~"+upload);
            System.out.println("rename~~~~~~~~~~~~~~~~"+rename);
            input.close();
            ftpClient.logout();

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally
        {
            if (ftpClient.isConnected())
            {
                try
                {
                    ftpClient.disconnect();
                } catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }

        }
    }
}
