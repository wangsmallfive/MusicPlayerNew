package example.util;

import example.gui.GUI;
import example.gui.MusicUtils;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wangzhenze
 * @Description:
 * @Date: Created in 2020/3/21 6:47 下午
 */
public class ServerUploadThread extends Thread {
    private List<File> selectedFile;
    public ServerUploadThread(List<File> selectedFile) {
        this.selectedFile = selectedFile;
    }
        public void run() {

        List<MusicUtils> ml = new ArrayList<>();
        if (selectedFile != null)
            for (File file : selectedFile) {
                    FtpUtils ftp = new FtpUtils();
                    ftp.uploadFile("", file.getName(), file.getAbsolutePath());
            }
    }
}
