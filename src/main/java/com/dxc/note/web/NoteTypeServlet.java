package com.dxc.note.web;

import com.dxc.note.po.NoteType;
import com.dxc.note.po.User;
import com.dxc.note.service.NoteTypeService;
import com.dxc.note.util.JsonUtil;
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
 * @data 2021/5/9 -19:47
 */
@WebServlet("/type")
public class NoteTypeServlet extends HttpServlet {


    private NoteTypeService typeService = new NoteTypeService();


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //设置导航栏高亮
        request.setAttribute("menu_page", "type");

        String actionName = request.getParameter("actionName");
        
        if("list".equals(actionName)){
            //查询用户列表
            typeList(request,response);
        }else if("delete".equals(actionName)){
            //删除用户列表
            deleteType(request,response);
        }else if("addOrUpdate".equals(actionName)){
            //添加或修改用户列表
            addOrUpdate(request,response);
        }
    }

    /**
     *  1. 接受参数 类型名称 类型id
     *  2. 获取session作用域中的user对象 得到用户id
     *  3. 调用service层的更新方法 返回一个resultInfo对象
     *  4. 将resultinfo对象转换成json对象 响应给ajax的回调函数
     * @param request
     * @param response
     */
    private void addOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        String typeName = request.getParameter("typeName");
        String typeId = request.getParameter("typeId");

        User user = (User)request.getSession().getAttribute("user");

        ResultInfo<Integer> resultInfo = typeService.addOrUpdate(typeName,user.getUserId(),typeId);

        JsonUtil.toJson(response,resultInfo);
    }

    /**
     *  1. 接受参数 类型id
     *  2. 调用service的更新操作 返回resultInfo对象
     *  3. 将resuleInfo对象转换成json格式的字符串 响应个ajax的回调函数
     * @param request
     * @param response
     */
    private void deleteType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String typeId = request.getParameter("typeId");

        ResultInfo<NoteType> resultInfo = typeService.deleteType(typeId);

        JsonUtil.toJson(response, resultInfo);
    }

    /**
     * 1. 获取session作用域中的user对象
     * 2. 调用service层的查询方法 查询当前登录用户的类型集合，返回集合
     * 3. 将类型列表设置到request请求域中
     * 4. 设置首页动态包含的页面值
     * 5. 请求转发跳转到index.jsp页面
     * @param request
     * @param response
     */
    private void typeList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.
        User user = (User)request.getSession().getAttribute("user");
        //2.
        List<NoteType> typeList = typeService.findTypeList(user.getUserId());
        //3.
        request.setAttribute("typeList", typeList);
        //4.
        request.setAttribute("changePage", "type/list.jsp");
        //5.
        request.getRequestDispatcher("index.jsp").forward(request,response);
    }
}
