package com.dxc.note.web;

import com.dxc.note.po.Note;
import com.dxc.note.po.User;
import com.dxc.note.service.NoteService;
import com.dxc.note.util.Page;
import com.dxc.note.vo.NoteVo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ding
 * @data 2021/5/8 -20:05
 */
@WebServlet("/index")
public class IndexServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置导航栏高亮
        request.setAttribute("menu_page", "index");

        //--查询
        //得到用户行为 判断查询类别 标题 日期 类型
        String actionName = request.getParameter("actionName");

        //将用户行为设置到request作用域中
        request.setAttribute("actionName",actionName);

        if("searchTitle".equals(actionName)){
            String title = request.getParameter("title");
            //回显
            request.setAttribute("title",title);
            noteList(request,response,title,null,null);
        }else if("searchDate".equals(actionName)){
            //得到查询条件
            String date = request.getParameter("date");
            //回显
            request.setAttribute("date",date);
            noteList(request,response,null,date,null);
        }else if("searchType".equals(actionName)) {
            //得到查询条件
            String typeId = request.getParameter("typeId");
            //回显
            request.setAttribute("typeId", typeId);
            noteList(request, response, null, null,typeId);
        }else{
            //不做条件查询
            //分页查询云记方法
            noteList(request,response,null,null,null);
        }

        // 设置首页动态包含的页面
        request.setAttribute("changePage","note/list.jsp");
        // 请求转发到index.jsp
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    private void noteList(HttpServletRequest request, HttpServletResponse response,
                          String title,String date,String typeId) {
        //接收参数
        String pageNum = request.getParameter("pageNum");
        String pagSize = request.getParameter("pageSize");
        //获取user
        User user = (User) request.getSession().getAttribute("user");
        //调用service层方法    == 增加title查询
        Page<Note> page = new NoteService().findNoteListByPage(pageNum,pagSize,user.getUserId(),title,date,typeId);
        //返回结果
        request.setAttribute("page",page);
        //左侧---通过日期分组查询当前登录用户的云记数量
        List<NoteVo> dateInfo = new NoteService().findNoteCountByDate(user.getUserId());
        request.getSession().setAttribute("dateInfo",dateInfo);
        //通过类型分组查询当前登录用户的云记数量
        List<NoteVo> typeInfo = new NoteService().findNoteCountByType(user.getUserId());
        request.getSession().setAttribute("typeInfo",typeInfo);
    }

}