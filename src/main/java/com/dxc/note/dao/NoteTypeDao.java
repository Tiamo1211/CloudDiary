package com.dxc.note.dao;

import com.dxc.note.po.NoteType;
import com.dxc.note.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ding
 * @data 2021/5/9 -19:46
 */
public class NoteTypeDao {

    /**
     * 查询类型列表
     * @param userId
     * @return
     */
    public List<NoteType> findTypeListByUserId(Integer userId){

        String sql = "select typeId,typeName,userId from tb_note_type where userId = ?";

        List<Object> params = new ArrayList<>();
        params.add(userId);

        List<NoteType> list = BaseDao.queryRows(sql,params,NoteType.class );

        return list;
    }

    /**
     * 查询对应类型关联的云记数量
     * @param typeId
     * @return
     */
    public long findNoteCountByTypeId(String typeId) {

        String sql = "select count(1) from tb_note where typeId = ?";

        List<Object> params = new ArrayList<>();
        params.add(typeId);

        long count = (long)BaseDao.findSingleValue(sql,params);

        return count;
    }

    /**
     * 删除类型列表
     * @param typeId
     * @return
     */
    public int deleteTypeById(String typeId) {

        String sql = "delete from tb_note_type where typeId = ?";

        List<Object> params = new ArrayList<>();
        params.add(typeId);

        int row = (int)BaseDao.executeUpdate(sql,params);

        return row;
    }

    /**
     * 检查类型名称
     * @param typeName
     * @param userId
     * @param typeId
     * @return 1成功 0失败
     */
    public Integer checkTypeName(String typeName, Integer userId, String typeId) {
        String sql = "select * from tb_note_type where userId = ? and typeName = ?";

        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.add(typeName);

        NoteType noteType = (NoteType)BaseDao.queryRow(sql,params,NoteType.class);

        if(noteType == null){
            return 1;
        }else{
            if(typeId.equals(noteType.getTypeId().toString())){
                return 1;
            }
        }

        return 0;
    }

    /**
     * 添加类型名称
     * @param typeName
     * @param userId
     * @return
     */
    public Integer addType(String typeName, Integer userId) {
        Integer key = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnetion();
            String sql = "insert into tb_note_type (typeName,userId) values (?,?)";
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,typeName);
            preparedStatement.setInt(2,userId);
            int row = preparedStatement.executeUpdate();
            if(row > 0 ){
                //获取主键
                resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()){
                    key = resultSet.getInt(1);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUtil.close(resultSet,preparedStatement,connection);
        }

        return key;
    }

    /**
     * 修改类型名称
     * @param typeName
     * @param typeId
     * @return
     */
    public Integer updateType(String typeName, String typeId) {
        String sql = "update tb_note_type set typeName = ? where typeId = ?";

        List<Object> params = new ArrayList<>();

        params.add(typeName);
        params.add(typeId);

        int row = BaseDao.executeUpdate(sql,params);

        return row;
    }
}
