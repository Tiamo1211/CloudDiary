package com.dxc.note.service;

import cn.hutool.core.util.StrUtil;
import com.dxc.note.dao.NoteDao;
import com.dxc.note.po.Note;
import com.dxc.note.util.Page;
import com.dxc.note.vo.NoteVo;
import com.dxc.note.vo.ResultInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ding
 * @data 2021/5/10 -19:18
 */
public class NoteService {

    private NoteDao noteDao = new NoteDao();

    /**
     * 添加或修改云记
     * @param typeId
     * @param title
     * @param noteEditor
     * @return
     */
    public ResultInfo<Note> addOrUpdate(String typeId, String title, String noteEditor,
                                        String noteId,String lon,String lat) {
        ResultInfo<Note> resultInfo = new ResultInfo<>();

        //非空判断
        if(StrUtil.isBlank(typeId)){
            resultInfo.setCode(0);
            resultInfo.setMsg("请选择云记类型！");
            return resultInfo;
        }
        if(StrUtil.isBlank(title)){
            resultInfo.setCode(0);
            resultInfo.setMsg("云记标题不能为空！");
            return resultInfo;
        }
        if(StrUtil.isBlank(noteEditor)){
            resultInfo.setCode(0);
            resultInfo.setMsg("云记内容不能为空！");
            return resultInfo;
        }

        //设置经纬度默认值
        if(lon == null || lat==null){
            lon="115.883254";
            lat="28.746904";
        }

        //设置回显对象 Note对象
        Note note = new Note();
        note.setTypeId(Integer.parseInt(typeId));
        note.setTitle(title);
        note.setContent(noteEditor);
        note.setLon(Float.parseFloat(lon));
        note.setLat(Float.parseFloat(lat));
        resultInfo.setResult(note);

        if (!StrUtil.isBlank(noteId)){
            note.setNoteId(Integer.parseInt(noteId));
        }

        //调用dao层
        int row = noteDao.addOrUpdate(note);
        if(row > 0){
            resultInfo.setCode(1);
        }else{
            resultInfo.setCode(0);
            resultInfo.setResult(note);
            resultInfo.setMsg("更新失败！");
        }

        return resultInfo;
    }

    /**
     * 分页查询
     * @param pageNumStr
     * @param pageSizeStr
     * @param userId
     * @param title
     * @return
     */
    public Page<Note> findNoteListByPage(String pageNumStr, String pageSizeStr,
                                         Integer userId, String title,String date,String typeId) {
        //0.参数的默认值
        Integer pageNum = 1;
        Integer pageSize = 5;  //每页显示的数据量
        //1.参数的非空校验
        if(!StrUtil.isBlank(pageNumStr)){
            //不为空设置参数
            pageNum = Integer.parseInt(pageNumStr);
        }
        if(!StrUtil.isBlank(pageSizeStr)){
            //不为空设置参数
            pageSize = Integer.parseInt(pageSizeStr);
        }
        //2.查询总数量   ===>增加title...
        long count = noteDao.findNoteCount(userId,title,date,typeId);
        //3.判断
        if(count < 1){
            return null;
        }
        //4.得到page对象
        Page<Note> page = new Page<>(pageNum,pageSize,count);
        //得到数据库分页查询的开始下标
        /**
         * 2条一页
         * 例如: limit 0,2  index = (1-1)*2   0
         *       limit 2,2  index = (2-1)*2   2
         *       index = (当前页 - 1 ) * 2
         */
        Integer index = (pageNum - 1) * pageSize;
        //查询数据列表
        List<Note> noteList = noteDao.findNoteListByPage(userId,index,pageSize,title,date,typeId);
        //将note对象设置到page对象中
        page.setDataList(noteList);
        //返回对象
        return page;
    }

    /**
     * 通过日期分组查询当前登录用户的云记数量
     * @param userId
     * @return
     */
    public List<NoteVo> findNoteCountByDate(Integer userId) {
        return noteDao.findNoteCountByDate(userId);
    }

    /**
     * 通过类型分组查询当前登录用户的云记数量
     * @param userId
     * @return
     */
    public List<NoteVo> findNoteCountByType(Integer userId) {
        return noteDao.findNoteCountByType(userId);
    }

    /**
     * 查询云记详情
     * @param noteId
     * @return
     */
    public Note findNoteById(String noteId) {
        if(StrUtil.isBlank(noteId)){
            return null;
        }
        return noteDao.findNoteById(noteId);
    }

    /**
     * 删除云记
     * @param noteId
     * @return
     */
    public Integer deleteNode(String noteId) {
        if(StrUtil.isBlank(noteId)){
            return 0;
        }
        int row = noteDao.deleteNode(noteId);
        if(row > 0){
            return 1;
        }
        return 0;
    }

    /**
     * 通过月份查询云记数量
     * @param userId
     * @return
     */
    public ResultInfo<Map<String, Object>> queryNoteCountByMonth(Integer userId) {

        ResultInfo<Map<String, Object>> resultInfo = new ResultInfo<>();

        //通过月份分类查询
        List<NoteVo> noteVos = noteDao.findNoteCountByDate(userId);

        if(noteVos != null && noteVos.size() > 0){
            //得到月份
            List<String> monthList = new ArrayList<>();
            //的到云记集合
            List<Integer> noteCountList = new ArrayList<>();

            for(NoteVo noteVo : noteVos){
                monthList.add(noteVo.getGroupName());
                noteCountList.add((int)noteVo.getNoteCount());
            }

            //map对象
            Map<String,Object> map = new HashMap<>();
            map.put("monthArray", monthList);
            map.put("dataArray",noteCountList);
            //设置到resultinfo中
            resultInfo.setCode(1);
            resultInfo.setResult(map);
        }

        return resultInfo;
    }

    /**
     * 查询坐标
     * @param userId
     * @return
     */
    public ResultInfo<List<Note>> queryNoteLonAndLat(Integer userId) {
        ResultInfo<List<Note>> resultInfo = new ResultInfo<>();

        List<Note> noteList = noteDao.queryNoteList(userId);
        if(noteList !=null && noteList.size() >0){
            resultInfo.setCode(1);
            resultInfo.setResult(noteList);
        }

        return resultInfo;
    }
}
