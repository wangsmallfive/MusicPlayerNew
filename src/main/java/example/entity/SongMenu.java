package example.entity;


import java.util.List;

/**
 * @author ZTF
 * @version 1.0
 * @date 2017年3月18日 上午12:29:44
 * @Description: 歌单的实体类
 */
public class SongMenu {
    private String songMenuName;
    private List<Song> songList;
    private String createDate;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getSongMenuName() {
        return songMenuName;
    }

    public void setSongMenuName(String songMenuName) {
        this.songMenuName = songMenuName;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

}
 