package com.dxc.note.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.dxc.note.dao.UserDao;
import com.dxc.note.po.User;
import com.dxc.note.vo.ResultInfo;

import javax.print.attribute.standard.PrinterState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

/**
 * @author ding
 * @data 2021/5/8 -14:56
 */
public class UserService {
    private UserDao userDao = new UserDao();

    /**
     * 用户登录
     1. 判断参数是否为空
     如果为空
     设置ResultInfo对象的状态码和提示信息
     返回resultInfo对象
     2. 如果不为空，通过用户名查询用户对象
     3. 判断用户对象是否为空
     如果为空
     设置ResultInfo对象的状态码和提示信息
     返回resultInfo对象
     4. 如果用户对象不为空，将数据库中查询到的用户对象的密码与前台传递的密码作比较 （将密码加密后再比较）
     如果密码不正确
     设置ResultInfo对象的状态码和提示信息
     返回resultInfo对象
     5. 如果密码正确
     设置ResultInfo对象的状态码和提示信息
     6. 返回resultInfo对象
     * @param userName
     * @param userPwd
     * @return
     */
    public ResultInfo<User> userLogin(String userName, String userPwd) {
        ResultInfo<User> resultInfo = new ResultInfo<>();

        // 数据回显：当登录实现时，将登录信息返回给页面显示
        User u = new User();
        u.setUname(userName);
        u.setUpwd(userPwd);
        // 设置到resultInfo对象中
        resultInfo.setResult(u);

        //  1. 判断参数是否为空
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(userPwd)) {
            // 如果为空 设置ResultInfo对象的状态码和提示信息
            resultInfo.setCode(0);
            resultInfo.setMsg("用户姓名或密码不能为空！");
            // 返回resultInfo对象
            return resultInfo;
        }

        // 2. 如果不为空，通过用户名查询用户对象
        User user = userDao.queryUserByName(userName);

        // 3. 判断用户对象是否为空
        if (user == null) {
            // 如果为空,设置ResultInfo对象的状态码和提示信息
            resultInfo.setCode(0);
            resultInfo.setMsg("该用户不存在！");
            // 返回resultInfo对象
            return resultInfo;
        }

        //  4. 如果用户对象不为空，将数据库中查询到的用户对象的密码与前台传递的密码作比较 （将密码加密后再比较）
        // 将前台传递的密码按照MD5算法的方式加密
//        userPwd = DigestUtil.md5Hex(userPwd);
        // 判断加密后的密码是否与数据库中的一致
        if (!userPwd.equals(user.getUpwd())) {
            // 如果密码不正确
            resultInfo.setCode(0);
            resultInfo.setMsg("用户密码不正确！");
            return resultInfo;
        }

        resultInfo.setCode(1);
        resultInfo.setResult(user);
        return resultInfo;
    }

    /**
     *      1. 判断昵称是否为空 如果为空 返回0
     *    	2. 调用dao层 通过用户id和昵称查询用户对象
     *    	3. 判断用户对象 存在返回0 不存在返回1
     * 验证昵称的唯一性
     * @param nick
     * @param userId
     * @return
     */
    public Integer checkNick(String nick, Integer userId) {
        //1.
        if(StrUtil.isBlank(nick)){
            return 0;
        }
        //2.
        User user = userDao.queryUserByNickAndUserId(nick,userId);
        //3.
        if(user!=null){
            return 0;
        }
        return 1;
    }

    /**
     *  1. 获取参数
     *  2. 参数的非空校验
     *     如果昵称为空 将状态码设置到resulInfo对象中 并返回
     * 	3. 从session作用域中获取用户对象（获取对象中默认的头像）
     * 	4. 实现上传文件
     *     - 获取Part对象 request.getPart("file文件域的属性值")；
     *     - 通过Part对象获取上传文件的文件名
     *     - 判断文件名是否为空
     *     - 获取文件存放的位置 WEB-INF/upload 目录
     *     - 上传文件到指定目录
     * 	5. 更新用户头像（更改上传的头像的文件名）
     * 	6. 调用dao的更新方法 返回受影响的行数
     * 	7. 判断行数 大于0 修改成功
     * 	8. 返回resultInfo对象
     * @param request
     * @return
     */
    public ResultInfo<User> updateUser(HttpServletRequest request) {
        ResultInfo<User> resultInfo = new ResultInfo<>();
        //1.
        String nick = request.getParameter("nick");
        String mood = request.getParameter("mood");
        //2.
        if(StrUtil.isBlank(nick)){
            resultInfo.setCode(0);
            resultInfo.setMsg("用户昵称不能为空！");
            return resultInfo;
        }
        //3.
        User user = (User) request.getSession().getAttribute("user");

        user.setNick(nick);
        user.setMood(mood);
        //4.
        try{
            Part part = request.getPart("img");
            String header = part.getHeader("Content-Disposition");
            String str = header.substring(header.lastIndexOf("=")+2);
            String fileName = str.substring(0,str.length()-1);
            if(!StrUtil.isBlank(fileName)){
                user.setHead(fileName);
                String filePath = request.getServletContext().getRealPath("/WEB-INF/upload");
                part.write(filePath+"/"+fileName);
            }
        }catch (Exception  e){
            e.printStackTrace();
        }

        int row = UserDao.updateUser(user);
        if(row > 0){
            resultInfo.setCode(1);
            //更新session
            request.getSession().setAttribute("user", user);
        }else{
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败！");
        }

        return resultInfo;
    }
}
