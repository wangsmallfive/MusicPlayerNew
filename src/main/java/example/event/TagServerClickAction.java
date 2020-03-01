package example.event;

import example.Global.GlobalVariable;
import example.controller.Controller;
import example.entity.SongMenu;
import example.gui.GUI;
import example.gui.MusicUtils;
import example.service.SongMenuOperate;
import example.service.SongOperate;
import example.util.DownlodaUtils;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wangzhenze
 * @Description:
 * @Date: Created in 2020/2/28 4:20 下午
 */
public class TagServerClickAction implements EventHandler<MouseEvent> {



    public TagServerClickAction(MainAction ma, ListView<Button> listVessel) {
        super();
        this.ma = ma;
        this.listVessel = listVessel;
    }

    private ServerContextBox cb = new ServerContextBox(this);    //右键菜单对象

    public ServerContextBox getCb() {
        return cb;
    }

    private MainAction ma;

    public MainAction getMa() {
        return ma;
    }

    private ListView<Button> listVessel;

    public ListView<Button> getListVessel() {
        return listVessel;
    }

    private List<MusicUtils> ml;

    @Override
    public void handle(MouseEvent event) {
        Node source = (Node) event.getSource();
        double x = event.getScreenX(), y = event.getScreenY();
        ContextMenu cm;
        if (source instanceof Button) cm = cb.getListContext();
        else cm = cb.getSongContext();
        if (event.getButton() == MouseButton.SECONDARY) {
            cm.show(source, x, y);
            if (checkEmpty(cm, source, x, y))
                cm.hide();

        }
        if (event.getButton() == MouseButton.PRIMARY) {
            cm.hide();
            int count = event.getClickCount();
            if (count == 2 && !(source instanceof Button)) {
                cm.show(source, x, y);
                cm.hide();
                ContextBox.add.hide();
                if (!checkEmpty(cm, source, x, y)) {
                    ContextBox.play.fire();
                    if (MainAction.ps.getCurrent_state() == GlobalVariable.PAUSINGMUSIC)
                        ContextBox.play.fire();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private boolean checkEmpty(ContextMenu cm, Node source, double x, double y) {
        if (cm == cb.getSongContext() && cm.getOwnerNode() instanceof TableView) {
            ml = ((TableView<MusicUtils>) cm.getOwnerNode()).getSelectionModel().getSelectedItems();
            return ml.isEmpty();
        }
        return false;
    }

}

class ServerContextBox {
    private TagServerClickAction action;
    private List<MusicUtils> ml;
    private int mli;
    private ContextMenu listContext = new ContextMenu();
    private ContextMenu songContext = new ContextMenu();
    private ContextMenu serverContext = new ContextMenu();
    private boolean isPlayList;
    public static Menu add = new Menu("\u6dfb\u52a0\u5230\u6b4c\u5355");
    public static MenuItem play_all = new MenuItem("\u5168\u90e8\u64ad\u653e");
    public static MenuItem play_all_next = new MenuItem("\u7a0d\u540e\u64ad\u653e");
    public static MenuItem play = new MenuItem("\u64ad\u653e");
    public static MenuItem play_next = new MenuItem("\u4e0b\u4e00\u9996\u64ad\u653e");
    public static MenuItem path = new MenuItem("\u6253\u5f00\u6587\u4ef6\u76ee\u5f55");
    public static MenuItem remove_song = new MenuItem("\u79fb\u9664\u6b4c\u66f2");
    public static MenuItem remove_list = new MenuItem("\u79fb\u9664\u6b4c\u5355");
    public static MenuItem downlode = new MenuItem("下载");

    @SuppressWarnings({"rawtypes", "unchecked"})
    public ServerContextBox(TagServerClickAction action) {
        this.action = action;

        listContext.getItems().addAll(play_all, play_all_next, new SeparatorMenuItem(), remove_list);
        songContext.getItems().addAll(play,new SeparatorMenuItem(),downlode);
        listContext.setOnShowing(e -> {
            refreshMenuL();
        });
        songContext.setOnShowing(e -> {
            refreshMenuS(action.getMa().ishoverPlayList());
        });

        play_all.setOnAction(e -> {
            if (ml.size() == 0) return;
            MainAction.ps.setCurrent_songMenu(ml);
            MainAction.ps.setCurrent_song(ml.get(0));
            GlobalVariable.currentSong = ml.get(0);
            MainAction.setCurrentList(ml);
            MainAction.pause();
        });
        play_all_next.setOnAction(e -> {
            if (ml.size() == 0) return;
            List<MusicUtils> cl = MainAction.ps.getCurrent_songMenu();
            int i;
            if (MainAction.ps.getCurrent_song() != null)
                i = cl.indexOf(MainAction.ps.getCurrent_song());
            else i = -1;
            if (cl != null)
                cl.addAll(i + 1, ml);
            else
                cl = new ArrayList<>(ml);
            MainAction.setCurrentList(cl);
        });
        play.setOnAction(e -> {
            ObservableList<MusicUtils> ol = ((TableView<MusicUtils>) songContext.getOwnerNode()).getItems();
            List<MusicUtils> l = new ArrayList<>();
            for (MusicUtils m : ol)
                l.add(m);
            MainAction.ps.setCurrent_songMenu(l);
            MainAction.ps.setCurrent_song(ml.get(mli));
            MainAction.setCurrentList(l);
            try {
                MainAction.pause();
            } catch (Exception e2) {
                Alert _alert = new Alert(Alert.AlertType.INFORMATION);
                _alert.setTitle("警告");
                _alert.setHeaderText("播放失败啦  w(ﾟДﾟ)w");
                _alert.setContentText(e2.getMessage());
                _alert.show();
            }

            MainAction.changeHorn(GlobalVariable.currentMenu.get());
        });
        play_next.setOnAction(e -> {
            List<MusicUtils> cl = MainAction.ps.getCurrent_songMenu();
            int i;
            if (MainAction.ps.getCurrent_song() != null)
                i = cl.indexOf(MainAction.ps.getCurrent_song());
            else i = -1;
            if (cl != null)
                cl.addAll(i + 1, ml);
            else
                cl = new ArrayList<>(ml);
            MainAction.setCurrentList(cl);
        });
        remove_list.setOnAction(e -> {
            Button target = (Button) listContext.getOwnerNode();
            if (target == null) return;
            SongMenuOperate.deleteSongMenuByName(target.getText());
            action.getListVessel().getItems().remove(target);
        });
        remove_song.setOnAction(e -> {
            TableView<MusicUtils> target = (TableView<MusicUtils>) songContext.getOwnerNode();
            if (target == null) return;
            target.getItems().removeAll(ml);
            if (isPlayList) {
                List<MusicUtils> cl = new ArrayList<>();
                for (MusicUtils m : target.getItems())
                    cl.add(m);
                MainAction.setCurrentList(cl);
                return;
            }
            for (MusicUtils m : ml) {
                try {
                    SongOperate.deleteSong(GlobalVariable.currentMenu.get(), m.getPath());
                } catch (Exception e2) {
                    Alert _alert = new Alert(Alert.AlertType.INFORMATION);
                    _alert.setTitle("警告");
                    _alert.setHeaderText("歌曲还在播放中哦  w(ﾟДﾟ)w");
                    _alert.setContentText("[" + m.getMusicTitle() + "]" + e2.getMessage());
                    _alert.show();
                }
            }
        });
        downlode.setOnAction(e -> {
            try {
                DirectoryChooser fileChooser = new DirectoryChooser();
                fileChooser.setTitle("打开音乐文件夹");
                File selectedFile = fileChooser.showDialog(GUI.staticStage);
                DownlodaUtils.downloadFile(ml.get(mli).getPath(),selectedFile.getPath()+"/"+ml.get(mli).getMusicTitle());
            } catch (Exception ex) {
                Alert _alert = new Alert(Alert.AlertType.INFORMATION);
                _alert.setTitle("警告");
                _alert.setHeaderText("下载出现异常");
                _alert.setContentText( ex.getMessage());
                _alert.show();
            }
            Alert _alert = new Alert(Alert.AlertType.CONFIRMATION);
            _alert.setTitle("提示");
            _alert.setHeaderText("下载完成");
            _alert.show();

        });
        path.setOnAction(e -> {
            String p = ml.get(0).getPath();
            System.err.println(p);
            try {
                Runtime.getRuntime().exec("explorer /select,\"" + p + '"');
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

    }

    @SuppressWarnings("unchecked")
    private boolean refreshMenuS(boolean isPlayList) {
        TableView<MusicUtils> tv = (TableView<MusicUtils>) songContext.getOwnerNode();
        ObservableList<MusicUtils> l0 = tv.getSelectionModel().getSelectedItems();
        mli = l0.indexOf(l0.get(0));
        List<MusicUtils> l1 = new ArrayList<>();
        for (MusicUtils m : l0)
            l1.add(m);
        ml = l1;
        add.getItems().clear();
        for (SongMenu sm : SongMenuOperate.getAllSongMenu()) {
            String n = sm.getSongMenuName();
            MenuItem i = new MenuItem(n);
            i.setOnAction(ie -> {
                for (MusicUtils m : ml) {
                    String p = m.getPath();
                    SongOperate.addSong(p, n);
                }
            });
            add.getItems().add(i);
        }
        if (ml.size() == 1) {
            path.setDisable(false);
        } else {
            path.setDisable(true);
        }
        if (GlobalVariable.PageType == Controller.SEARCH) {
            remove_song.setDisable(true);
        } else {
            remove_song.setDisable(false);
        }
        this.isPlayList = isPlayList;
        return isPlayList;
    }

    private void refreshMenuL() {
        String menuName = ((Button) listContext.getOwnerNode()).getText();
        List<MusicUtils> l = SongMenuOperate.getSongsByMenuName(menuName);
        ml = l;
        if (menuName.equals("我喜欢的音乐")) {
            remove_list.setDisable(true);
        } else {
            remove_list.setDisable(false);
        }
    }

    void setMli(int index) {
        mli = index;
    }

    void setMl(List<MusicUtils> ml) {
        this.ml = ml;
    }

    public ContextMenu getListContext() {
        return listContext;
    }

    public ContextMenu getSongContext() {
        return songContext;
    }

    public TagServerClickAction getAction() {
        return action;
    }
}
