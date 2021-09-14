package com.dxc.note.web;

import com.dxc.note.po.Note;
import com.dxc.note.po.User;
import com.dxc.note.service.NoteService;
import com.dxc.note.util.JsonUtil;
import com.dxc.note.vo.ResultInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ding
 * @data 2021/5/17 -16:45
 */
@WebServlet("/report")
public class ReportServlet extends HttpServlet {

    private NoteService noteService = new NoteService();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //设置导航高亮
        request.setAttribute("menu_page", "report");
        // 接收用户行为
        String actionName = request.getParameter("actionName");
        if("info".equals(actionName)){
            //进入报表页面
            reportInfo(request,response);
        }else if("month".equals(actionName)){
            queryNoteCountByMonth(request,response);
        }else if("location".equals(actionName)){
            //查询坐标
            queryNoteLonAndLat(request,response);
        }
    }

    /**
     * 查询坐标
     * @param request
     * @param response
     */
    private void queryNoteLonAndLat(HttpServletRequest request, HttpServletResponse response) {
        User user = (User)request.getSession().getAttribute("user");
        ResultInfo<List<Note>> resultInfo = noteService.queryNoteLonAndLat(user.getUserId());
        //转换成json对象
        JsonUtil.toJson(response,resultInfo);
    }

    /**
     * 通过月份查询云记数量
     * @param request
     * @param response
     */
    private void queryNoteCountByMonth(HttpServletRequest request, HttpServletResponse response) {
        User user = (User)request.getSession().getAttribute("user");
        ResultInfo<Map<String,Object>> resultInfo = noteService.queryNoteCountByMonth(user.getUserId());
        //转换成json对象
        JsonUtil.toJson(response,resultInfo);
    }

    /**
     * 进入报表页面
     * @param request
     * @param response
     */
    private void reportInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置首页动态包含的页面
        request.setAttribute("changePage", "report/info.jsp");
        //请求转发
        request.getRequestDispatcher("index.jsp").forward(request,response);
    }
}
