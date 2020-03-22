package example.util;

import example.entity.Song;
import example.service.SongOperate;
import javafx.scene.control.Alert;
import org.apache.commons.net.ftp.FTPFile;

import java.util.List;

import static example.Global.GlobalVariable.currentMenu;

/**
 * @Author: wangzhenze
 * @Description:
 * @Date: Created in 2020/3/21 6:42 下午
 */
public class MusicThread extends Thread{
    public void run() {
        FtpUtils ftp = new FtpUtils();
        List<FTPFile> serverFileNames =  ftp.readFile();
        for (int i=0;i<serverFileNames.size();i++){
            Song s = SongOperate.addServiceSong("http://139.196.95.157/musicplayer/",serverFileNames.get(i), currentMenu.get());

        }
        Alert _alert = new Alert(Alert.AlertType.INFORMATION);
        _alert.setTitle("提示");
        _alert.setHeaderText("刷新成功");
        _alert.show();

    }  
}
