package com.dxc.note.web;

import com.dxc.note.po.User;
import com.dxc.note.service.UserService;
import com.dxc.note.vo.ResultInfo;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author ding
 * @data 2021/5/8 -14:57
 */
@WebServlet("/user")
@MultipartConfig
public class UserServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //设置首页导航高亮
        request.setAttribute("menu_page", "user");

        // 接收用户行为
        String actionName = request.getParameter("actionName");
        // 判断用户行为，调用对应的方法
        if ("login".equals(actionName)) {
            // 用户登录
            userLogin(request, response);
        }else if("logout".equals(actionName)){
            //用户退出
            userLogOut(request, response);
        }else if("userCenter".equals(actionName)){
            //进入个人中心
            useCenter(request, response);
        }else if("userHead".equals(actionName)){
            //加载头像
            userHead(request, response);
        }else if("checkNick".equals(actionName)){
            //验证nick的唯一性
            checkNick(request,response);
        }else if("updateUser".equals(actionName)){
            //修改用户信息
            updateUser(request,response);
        }
    }

    /**
     *  	1. 直接调用service方法 传递request对象作为参数 返回resultInfo对象
     *  	2. 将resultInfo对象存到request作用域中
     *  	3. 请求转发到个人中心页面 user?actionName=userCenter
     * @param request
     * @param response
     */
    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.
        ResultInfo<User> resultInfo = userService.updateUser(request);
        //2.
        request.setAttribute("resultInfo", resultInfo);
        //3.
        request.getRequestDispatcher("user?actionName=userCenter").forward(request, response);
    }

    /**
     * 1. 获取参数昵称
     * 2. 从session作用域获取对象 得到用户id
     * 3. 调用secvice方法 得到返回的结果
     * 4. 通过字符输出流将结果响应给前台的ajax的回调函数
     * 5. 关闭资源
     * @param request
     * @param response
     * @throws IOException
     */
    private void checkNick(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.
        String nick = request.getParameter("nick");
        //2.
        User user = (User) request.getSession().getAttribute("user");
        //3.
        Integer code = userService.checkNick(nick,user.getUserId());
        //4.
        response.getWriter().write(code+"");
        //5.
        response.getWriter().close();
    }

    /**
     * 1. 获取参数 图片名称
     * 2. 得到图片的存放路径 requesr.getServletContext().getealPathR("/")
     * 3. 通过图片的完整路径 得到file对象
     * 4. 通过截取得到图片的后缀
     * 5. 通过不同的后缀设置不同的响应类型
     * 6. 利用FileUtils的copyFile()方法，将图片拷贝给浏览器
     * @param request
     * @param response
     */
    private void userHead(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.
        String head = request.getParameter("imageName");
        //2.
        String realPath = request.getServletContext().getRealPath("WEB-INF/upload");
        //3.
        File file = new File(realPath+"/"+head);
        //4.
        String pic = head.substring(head.lastIndexOf(".")+1);
        //5.
        if("PNG".equalsIgnoreCase(pic)){
            response.setContentType("image/png");
        }else if("JPG".equalsIgnoreCase(pic) || "JPEG".equalsIgnoreCase(pic)){
            response.setContentType("image/jpeg");
        }else if("GIF".equalsIgnoreCase(pic)){
            response.setContentType("image/gif");
        }
        //6.
        FileUtils.copyFile(file, response.getOutputStream());
    }

    private void useCenter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置首页动态包含的页面值
        request.setAttribute("changePage", "user/info.jsp");
        //请求转发index
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    private void userLogOut(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //销毁Session对象
        request.getSession().invalidate();
        //删除Cookie对象
        Cookie cookie = new Cookie("user", null);
        cookie.setMaxAge(0); //设置0 表示删除
        response.addCookie(cookie);
        //重定向跳转
        response.sendRedirect("login.jsp");
    }


    /**
     * 用户登录
     1. 获取参数 （姓名、密码）
     2. 调用Service层的方法，返回ResultInfo对象
     3. 判断是否登录成功
     如果失败
     将resultInfo对象设置到request作用域中
     请求转发跳转到登录页面
     如果成功
     将用户信息设置到session作用域中
     判断用户是否选择记住密码（rem的值是1）
     如果是，将用户姓名与密码存到cookie中，设置失效时间，并响应给客户端
     如果否，清空原有的cookie对象
     重定向跳转到index页面
     * @param request
     * @param response
     */
    private void userLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1. 获取参数 （姓名、密码）
        String userName = request.getParameter("userName");
        String userPwd = request.getParameter("userPwd");

        // 2. 调用Service层的方法，返回ResultInfo对象
        ResultInfo<User> resultInfo = userService.userLogin(userName, userPwd);

        // 3. 判断是否登录成功
        if (resultInfo.getCode() == 1) { // 如果成功
            //  将用户信息设置到session作用域中
            request.getSession().setAttribute("user", resultInfo.getResult());
            //  判断用户是否选择记住密码（rem的值是1）
            String rem = request.getParameter("rem");
            // 如果是，将用户姓名与密码存到cookie中，设置失效时间，并响应给客户端
            if ("1".equals(rem)) {
                // 得到Cookie对象
                Cookie cookie = new Cookie("user",userName +"-"+userPwd);
                // 设置失效时间
                cookie.setMaxAge(3*24*60*60);
                // 响应给客户端
                response.addCookie(cookie);
            } else {
                // 如果否，清空原有的cookie对象
                Cookie cookie = new Cookie("user", null);
                // 删除cookie，设置maxage为0
                cookie.setMaxAge(0);
                // 响应给客户端
                response.addCookie(cookie);
            }
            // 重定向跳转到index页面
            response.sendRedirect("index");

        } else { // 失败
            // 将resultInfo对象设置到request作用域中
            request.setAttribute("resultInfo", resultInfo);
            // 请求转发跳转到登录页面
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }

    }
}