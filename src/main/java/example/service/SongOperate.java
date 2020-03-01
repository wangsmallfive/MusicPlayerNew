package example.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import example.Global.GlobalVariable;
import example.Global.PlayState;
import example.dao.SongDao;
import example.dao.impl.SongDaoImpl;
import example.entity.Song;
import example.entity.SongMenu;
import example.entity.Tag;
import example.gui.MusicUtils;
import example.util.SongUtil;
import example.util.TagInfoUtil;
import com.sun.org.apache.bcel.internal.classfile.Field;

import javafx.scene.media.Media;
import org.apache.commons.net.ftp.FTPFile;
import org.dom4j.Element;

/**
 * @version 1.0
 * @date 2017年3月18日 上午1:19:59
 * @Description:
 */
public class SongOperate {
    static SongDao songDao;

    static {
        songDao = new SongDaoImpl();
    }

    // 选择了是否喜欢
    public static void setIsLike(String path, boolean isLike) throws RuntimeException {
        songDao.setAllIsLike(path, isLike);
        if (!isLike) {
            // 取消选择喜爱
            songDao.deleteSong("我喜欢的音乐", path);
        } else {
            addSong(path, "我喜欢的音乐");
        }
    }

    // 搜索名字关键字获得任意歌单下的歌曲
    public static List<Song> findSongByName(String key, String menuName, int Mode,String where ) {
        System.out.println(key + ":" + menuName);
        if (Mode != GlobalVariable.SEARCHMODE_HYBRID) {
            return songDao.getSong(key, menuName, Mode,where);
        } else {
            return songDao.getSongHYBRID(key, menuName,where);
        }

    }

    // 搜索名字关键字获得本地音乐下的歌曲
    public static List<Song> findLocalSong(String key, int Mode,String where ) {
        if (Mode != GlobalVariable.SEARCHMODE_HYBRID) {
            return songDao.getSong(key, "本地管理", Mode,where);
        } else {
            return songDao.getSongHYBRID(key, "本地管理",where);
        }
    }

    public static void deleteSong(String menuName, String songPath) throws RuntimeException {
        MusicUtils current_song = PlayState.getPlayState().getCurrent_song();
        if (current_song != null) {
            String path = current_song.getPath();
            if (PlayState.getPlayState().getCurrent_state() == GlobalVariable.PLAYINGMUSIC)
                if (path == songPath) {
                    throw new RuntimeException(",正在播放中!");
                }
        }
        songDao.deleteSong(menuName, songPath);
    }

    // 添加一个song，并返回实体
    public static Song addSong(String path, String menuName) throws RuntimeException {
        // 此处判断是否为合法路径以及合法音频格式
        Song song = new Song();
        song.setPath(path);
        if (menuName.equals("我喜欢的音乐")) {
            song.setYesLike();
        } else {
            if (songDao.checkPathIsLike(path)) {
                song.setYesLike();
            } else {
                song.setNoLike();
            }
        }
        File file = new File(path);
        song.setLength(SongUtil.getLengthToMb(file.length()));
        Tag tag;
        if (path.endsWith(".flac")) {
            tag = TagInfoUtil.FlacInfoRead(path);
            song.setTag(tag);
        } else if (path.endsWith(".mp3")) {
            tag = TagInfoUtil.Mp3InfoRead(path);
            song.setTag(tag);
        } else {
            tag = TagInfoUtil.getOtherTag(path);
            song.setTag(tag);
        }
        if (!songDao.checkMusicExist(path, menuName)) {
            songDao.addSong(song, menuName);
            if (!songDao.checkMusicExist(path, "本地音乐")) {
                songDao.addMusicToLocal(song);
            }
            return song;
        } else {
            throw new RuntimeException(path + "已经在" + menuName + "下啦！ ⊙﹏⊙‖∣° ");
        }
    }
    public static Song addServiceSong(String servicePath, FTPFile file, String menuName) throws RuntimeException {

        String songName = servicePath + file.getName();
        // 此处判断是否为合法路径以及合法音频格式
        Song song = new Song();
        song.setPath(songName);
        song.setLength(SongUtil.getLengthToMb(file.getSize()));
        Tag tag = new Tag();
        tag.setSongName(file.getName());
        song.setTag(tag);
        song.setLike("NO");
        songDao.addMusicToServer(song);
        return song;
    }

    // 添加某文件夹下的音乐
    public static void addSongWithFile(String filePath, String menuName) throws RuntimeException {
        File files = new File(filePath);
        List<Song> songList = new ArrayList<Song>();
        int count = 0;
        boolean isRepet = false;
        if (files.exists() && files.isDirectory()) {
            for (File file : files.listFiles()) {
                // Media media=new Media(file.toString());
                String absolutePath = file.getAbsolutePath();
                if (songDao.checkMusicExist(absolutePath, menuName)) {
                    count++;
                    isRepet = true;
                } else {
                    if (file.isFile()) {
                        if (absolutePath.endsWith(".flac")) {
                            Tag tag = null;
                            tag = TagInfoUtil.FlacInfoRead(absolutePath);
                            Song song = new Song();
                            song.setPath(absolutePath);
                            song.setLength(SongUtil.getLengthToMb(file.length()));
                            song.setTag(tag);
                            if (menuName.equals("我喜欢的音乐")) {
                                song.setYesLike();
                            } else {
                                if (songDao.checkPathIsLike(song.getPath())) {
                                    song.setYesLike();
                                } else {
                                    song.setNoLike();
                                }
                            }
                            songList.add(song);
                            if (!songDao.checkMusicExist(absolutePath, "本地音乐")) {
                                songDao.addMusicToLocal(song);
                            }
                        } else if (absolutePath.endsWith(".mp3")) {
                            Tag tag = null;
                            tag = TagInfoUtil.Mp3InfoRead(absolutePath);
                            Song song = new Song();
                            song.setPath(absolutePath);
                            song.setLength(SongUtil.getLengthToMb(file.length()));
                            song.setTag(tag);
                            if (menuName.equals("我喜欢的音乐")) {
                                song.setYesLike();
                            } else {
                                song.setNoLike();
                            }
                            songList.add(song);
                            if (!songDao.checkMusicExist(absolutePath, "本地音乐")) {
                                songDao.addMusicToLocal(song);
                            }
                        } else {
                            Tag tag = null;
                            tag = TagInfoUtil.getOtherTag(absolutePath);
                            Song song = new Song();
                            song.setPath(absolutePath);
                            song.setLength(SongUtil.getLengthToMb(file.length()));
                            song.setTag(tag);
                            if (menuName.equals("我喜欢的音乐")) {
                                song.setYesLike();
                            } else {
                                song.setNoLike();
                            }
                            songList.add(song);
                            if (!songDao.checkMusicExist(absolutePath, "本地音乐")) {
                                songDao.addMusicToLocal(song);
                            }
                        }

                    }
                }
            }
        }
        songDao.addSongWithFile(songList, menuName);
        if (isRepet) {
            throw new RuntimeException(count + "首歌添加失败，歌单下已经添加了某些歌曲！ ⊙﹏⊙‖∣° ");
        }
    }

}
