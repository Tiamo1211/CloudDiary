package com.dxc.note.web;

import cn.hutool.core.util.StrUtil;
import com.dxc.note.po.Note;
import com.dxc.note.po.NoteType;
import com.dxc.note.po.User;
import com.dxc.note.service.NoteService;
import com.dxc.note.service.NoteTypeService;
import com.dxc.note.vo.ResultInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author ding
 * @data 2021/5/10 -19:18
 */
@WebServlet("/note")
public class NoteServlet extends HttpServlet {

    private NoteService noteService = new NoteService();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置导航栏高亮
        request.setAttribute("menu_page", "note");

        String actionName = request.getParameter("actionName");

        if("view".equals(actionName)){
            //进入发布云记页面
            noteView(request,response);
        }else if("addOrUpdate".equals(actionName)){
            addOrUpdate(request,response);
        }else if("detail".equals(actionName)){
            //云记详情
            noteDetail(request,response);
        }else if("delete".equals(actionName)){
            //删除云记
            deleteNote(request,response);
        }
    }

    /**
     * 删除云记
     * @param request
     * @param response
     */
    private void deleteNote(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //接受参数
        String noteId = request.getParameter("noteId");
        //删
        Integer code = noteService.deleteNode(noteId);
        //转
        response.getWriter().write(code+"");
        response.getWriter().close();
    }

    /**
     * 云记详情
     * @param request
     * @param response
     */
    private void noteDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接受参数
        String noteId = request.getParameter("noteId");
        //查询
        Note note = noteService.findNoteById(noteId);
        //存
        request.setAttribute("note",note);
        //设置首页动态包含的页面值
        request.setAttribute("changePage", "note/detail.jsp");
        //请求转发到
        request.getRequestDispatcher("index.jsp").forward(request,response);
    }

    /**
     * 添加或修改
     * @param request
     * @param response
     */
    private void addOrUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //接受参数
        String typeId = request.getParameter("typeId");
        String title = request.getParameter("title");
        String noteEditor = request.getParameter("noteEditor");

        //获取经纬度
        String lon = request.getParameter("lon");
        String lat = request.getParameter("lat");

        //如果是修改操作 接受noteId
        String noteId = request.getParameter("noteId");

        //调用service层方法
        ResultInfo<Note> resultInfo = noteService.addOrUpdate(typeId,title,noteEditor,noteId,lon,lat);
        if(resultInfo.getCode() == 1){
            response.sendRedirect("index");
        }else{
            request.setAttribute("resultInfo", resultInfo);

            //
            String url = "note?actionName=view";
            //如果是修改操作
            if(!StrUtil.isBlank(noteId)){
                url +="&noteId"+noteId;
            }

            request.getRequestDispatcher(url).forward(request,response);
        }
    }

    /**
     * 进入发布云记
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void noteView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //修改操作
        String noteId = request.getParameter("noteId");
        //通过id查到云记对象
        Note note = noteService.findNoteById(noteId);
        //存
        request.setAttribute("noteInfo",note);


        //1.从session对象中获取用户对象
        User user = (User)request.getSession().getAttribute("user");
        //2.通过用户id查到对应的类型列表
        List<NoteType> typeList = new NoteTypeService().findTypeList(user.getUserId());
        //3.将类型列表设置到request请求域中
        request.setAttribute("typeList",typeList);
        //4.设置首页动态包含的页面值
        request.setAttribute("changePage", "note/view.jsp");
        //5.请求转发到
        request.getRequestDispatcher("index.jsp").forward(request,response);
    }
}
