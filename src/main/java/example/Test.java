package example;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.tulskiy.musique.audio.player.Player;
import example.service.PlayOperate;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;

/**
 * @Author: wangzhenze
 * @Description:
 * @Date: Created in 2020/2/25 7:47 下午
 */
public class Test {
     static MediaPlayer mediaPlayer2;
     static PlayOperate playOperate;
    public static void main(String[] args) {
        mediaPlayer2 = new MediaPlayer(new Media("/Volumes/扩容/网易云音乐/第一层/第二层/第三层/LETTER.mp3"));
        mediaPlayer2.setOnReady(() -> {
            mediaPlayer2.setVolume(0.5);
        });
        mediaPlayer2.play();
    }
}
