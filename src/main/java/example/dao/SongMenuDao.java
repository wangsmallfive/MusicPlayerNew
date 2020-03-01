package example.dao;

import java.util.List;

import example.entity.Song;
import example.entity.SongMenu;

/**
 * @version 1.0
 * @date 2017年3月18日 上午12:16:41
 * @Description: 歌单的Dao接口
 */
public interface SongMenuDao {
    void addSongMenu(String songMenuName);

    void deleteSongMenu(String songMenuName);

    List<SongMenu> getAllSongMenu();

    List<Song> getSongByMenuName(String menuName);

    String getCreateDateBySongMenuName(String menuName);

    List<Song> getServerSongByMenuName(String 服务器音乐);
}
 