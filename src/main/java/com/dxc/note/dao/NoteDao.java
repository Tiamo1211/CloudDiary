package com.dxc.note.dao;

import cn.hutool.core.util.StrUtil;
import com.dxc.note.po.Note;
import com.dxc.note.vo.NoteVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ding
 * @data 2021/5/10 -19:18
 */
public class NoteDao {
    /**
     * 添加或修改云记
     * @param note
     * @return
     */
    public int addOrUpdate(Note note) {
        //sql
        String sql = "";
        //设置参数
        List<Object> params = new ArrayList<>();
        params.add(note.getTypeId());
        params.add(note.getTitle());
        params.add(note.getContent());


        //判断noteId是否为空
        if(note.getNoteId() == null){
            //添加操作
            sql = "insert into tb_note (typeId,title,content,pubTime,lon,lat) values (?,?,?,now(),?,?)";
            params.add(note.getLon());
            params.add(note.getLat());
        }else{
            sql = "update tb_note set typeId = ? , title = ? , content = ? ,pubTime = now() where noteId = ? ";
            params.add(note.getNoteId());
        }

        //
        int row = BaseDao.executeUpdate(sql,params);
        return row;
    }

    /**
     * 查询总数
     * @param userId
     * @return
     */
    public long findNoteCount(Integer userId,String title,String date,String typeId) {
        String sql = "select count(1) from tb_note n inner join tb_note_type t " +
                "on n.typeId = t.typeId where userId = ? ";
        List<Object> params = new ArrayList<>();
        params.add(userId);
        //判断title是否为空
        if(!StrUtil.isBlank(title)){
            //拼接sql
            sql += " and title like concat('%',?,'%') ";
            params.add(title);
        }else if(!StrUtil.isBlank(date)){
            //拼接sql
            sql += " and DATE_FORMAT(pubTime,'%Y年%m月') = ?  ";
            params.add(date);
        }else if(!StrUtil.isBlank(typeId)){
            //拼接sql
            sql += " and n.typeId = ?  ";
            params.add(typeId);
        }
        long count = (long)BaseDao.findSingleValue(sql,params);
        return count;
    }

    /**
     * 分页查询
     * @param userId
     * @param index
     * @param pageSize
     * @return
     */
    public List<Note> findNoteListByPage(Integer userId, Integer index, Integer pageSize,
                                         String title,String date,String typeId) {
        String sql = "select noteId,title,pubTime from tb_note n inner join tb_note_type t " +
                "on n.typeId = t.typeId where userId = ? ";
        List<Object> params = new ArrayList<>();
        params.add(userId);
        //判断title是否为空
        if(!StrUtil.isBlank(title)){
            //拼接sql
            sql += " and title like concat('%',?,'%') ";
            params.add(title);
        }else if(!StrUtil.isBlank(date)){
            //拼接sql
            sql += " and DATE_FORMAT(pubTime,'%Y年%m月') = ?  ";
            params.add(date);
        }else if(!StrUtil.isBlank(typeId)){
            //拼接sql
            sql += " and n.typeId = ?  ";
            params.add(typeId);
        }
        //最后拼接
        sql +=" order by pubTime desc limit ?,? ";
        params.add(index);
        params.add(pageSize);
        List<Note> noteList = BaseDao.queryRows(sql,params,Note.class);

        return noteList;
    }

    /**
     * 通过日期分组查询当前登录用户的云记数量
     * @param userId
     * @return
     */
    public  List<NoteVo>findNoteCountByDate(Integer userId) {
        String sql = "SELECT COUNT(1) noteCount ,DATE_FORMAT(pubTime,'%Y年%m月') groupName FROM tb_note n " +
                "INNER JOIN tb_note_type t " +
                "ON n.`typeId` = t.`typeId` " +
                "WHERE userId = ? " +
                "GROUP BY DATE_FORMAT(pubTime,'%Y年%m月') " +
                "ORDER BY DATE_FORMAT(pubTime,'%Y年%m月') DESC ";
        List<Object> params = new ArrayList<>();
        params.add(userId);
        List<NoteVo> list = BaseDao.queryRows(sql,params,NoteVo.class);
        return list;
    }

    public List<NoteVo> findNoteCountByType(Integer userId) {
        String sql = "SELECT COUNT(noteId) noteCount ,t.`typeId` typeId ,t.`typeName` groupName " +
                "FROM tb_note n " +
                "RIGHT JOIN tb_note_type t " +
                "ON n.`typeId` = t.`typeId` " +
                "WHERE userId = ? " +
                "GROUP BY t.typeId " +
                "ORDER BY COUNT(noteId) DESC ";
        List<Object> params = new ArrayList<>();
        params.add(userId);
        List<NoteVo> list = BaseDao.queryRows(sql,params,NoteVo.class);
        return list;
    }

    /**
     * 查询云记
     * @param noteId
     * @return
     */
    public Note findNoteById(String noteId) {
        String sql = "SELECT `noteId`,`title`,`content`,`pubTime`,`typeName`,n.typeId FROM tb_note n " +
                " INNER JOIN tb_note_type t ON n.`typeId`=t.`typeId` " +
                " WHERE noteId  = ? ";
        List<Object> params = new ArrayList<>();
        params.add(noteId);
        Note note = (Note)BaseDao.queryRow(sql,params,Note.class);
        return note;
    }

    /**
     * 删除云记
     * @param noteId
     * @return
     */
    public int deleteNode(String noteId) {
        String sql = "delete from tb_note where noteId = ? ";
        List<Object> params = new ArrayList<>();
        params.add(noteId);
        int row = BaseDao.executeUpdate(sql,params);
        return row;
    }

    /**
     * 查询坐标
     * @param userId
     * @return
     */
    public List<Note> queryNoteList(Integer userId) {
        String sql = "select lon,lat from tb_note n inner join tb_note_type t on n.typeId = t.typeId where userId = ? ";
        List<Object> params = new ArrayList<>();
        params.add(userId);
        List<Note> list = BaseDao.queryRows(sql,params,Note.class);
        return list;
    }
}
