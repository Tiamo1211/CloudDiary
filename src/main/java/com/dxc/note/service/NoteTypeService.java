package com.dxc.note.service;

import cn.hutool.core.util.StrUtil;
import com.dxc.note.dao.NoteTypeDao;
import com.dxc.note.po.NoteType;
import com.dxc.note.vo.ResultInfo;

import java.util.List;

/**
 * @author ding
 * @data 2021/5/9 -19:46
 */
public class NoteTypeService {

    private NoteTypeDao typeDao = new NoteTypeDao();

    /**
     * 查询类型
     * @param userId
     * @return
     */
    public List<NoteType> findTypeList(Integer userId){
        List<NoteType> typeList = typeDao.findTypeListByUserId(userId);
        return typeList;
    }

    /**
     * 删除类型
     * @param typeId
     * @return
     */
    public ResultInfo<NoteType> deleteType(String typeId) {
        ResultInfo<NoteType> resultInfo = new ResultInfo<>();

        if(StrUtil.isBlank(typeId)){
            resultInfo.setCode(0);
            resultInfo.setMsg("系统异常，请重试！");
            return resultInfo;
        }

        long noteCount = typeDao.findNoteCountByTypeId(typeId);

        if(noteCount > 0){
            resultInfo.setCode(0);
            resultInfo.setMsg("该类型存在子记录，不可删除！");
            return resultInfo;
        }

        int row = typeDao.deleteTypeById(typeId);
        if(row > 0){
            resultInfo.setCode(1);
        }else{
            resultInfo.setCode(0);
            resultInfo.setMsg("删除失败！");
        }

        return resultInfo;
    }

    /**
     * 添加或修改
     * @param typeName
     * @param userId
     * @param typeId
     * @return
     */
    public ResultInfo<Integer> addOrUpdate(String typeName, Integer userId, String typeId) {
        ResultInfo<Integer> resultInfo = new ResultInfo<>();
        /**
         * 1. 判断参数是否为空 （类型名称）
         *    - 为空  code == 0 ; msg = " xxx"
         *    - 不为空  调用dao层 查询当前用户下 类型名称是否唯一 返回0或1 （查询数量 为0 可用 >0 不可用）
         *    - 返回resultinfo对象
         * 2. 判断类型id是否为空
         *    - 如果为空 调用dao的添加方法 返回主键 （前台页面需要显示成功添加之后的类型id）
         *    - 如果不为空 调用dao的修改方法 返回受影响的行数
         * 3. 判断返回的数据
         *    1. 大于0 更新成功 code=1 result=主键
         *    2. 不大于0 更新失败 code=0 msg=xxx；
         */
        if(StrUtil.isBlank(typeName)){
            resultInfo.setCode(0);
            resultInfo.setMsg("类型名称不能为空！");
            return resultInfo;
        }

        Integer code = typeDao.checkTypeName(typeName,userId,typeId);

        if(code == 0){
            resultInfo.setCode(0);
            resultInfo.setMsg("类型名称已存在，请重新输入！");
            return resultInfo;
        }

        Integer key = null;

        if(StrUtil.isBlank(typeId)){
            key = typeDao.addType(typeName,userId);
        }else {
            key = typeDao.updateType(typeName,typeId);
        }

        if(key > 0){
            resultInfo.setCode(1);
            resultInfo.setResult(key);
        }else {
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败！");
        }

        return resultInfo;
    }
}
