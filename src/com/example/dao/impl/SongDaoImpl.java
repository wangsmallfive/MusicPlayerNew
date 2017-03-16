package com.example.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.example.dao.SongDao;
import com.example.entity.Song;
import com.example.entity.Tag;
import com.example.util.XMLUtil;

/** 
* @author ZTF  
* @date 2017年3月16日 下午9:13:38 
* @version 1.0 
* @Description:   TODO(用一句话描述该文件做什么)
*/
public class SongDaoImpl implements SongDao{

	@Override
	public void addSong(Song song) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteSong(String songPath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteSongWithFile(String songPath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeSong(Song song) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Song> getSong(String songSheet) {
        Document document= XMLUtil.getDoc();
        List<Element> elements= document.getRootElement().elements(songSheet);
        List<Song> songList=new ArrayList<Song>();
        if(elements!=null){
            for(Element e:elements){
                Song song=new Song();
                song.setId(e.attributeValue("id"));
                song.setPath(e.elementText("path"));
                Element tagElement = e.element("tag");
                Tag tag=new Tag();
                tag.setAlbum(e.elementText("album"));
                tag.setComment(e.elementText("comment"));
                tag.setGetArtist(e.elementText("artist"));
                tag.setSongName(e.elementText("songName"));
                tag.setYear(e.elementText("year"));
                song.setTag(tag);
                songList.add(song);
            }
        }
        return songList;
	}
	
}
 