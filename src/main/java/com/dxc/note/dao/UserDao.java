package com.dxc.note.dao;

import com.dxc.note.po.User;
import com.dxc.note.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ding
 * @data 2021/5/8 -14:53
 */
public class UserDao {
    /**
     *  1.  通过用户id修改用户信息
     *      	String sql = "update tb_user set nick =  ? , mood = ? , head = ? where userId = ?";
     *  2. 设置参数集合
     *  3. 调用BaseDao的更新方法
     *  4. 返回受影响的行数
     * @param user
     * @return
     */
    public static int updateUser(User user) {
        //1.
        String sql = "update tb_user set nick =  ? , mood = ? , head = ? where userId = ?";
        //2.
        List<Object> params = new ArrayList<>();
        params.add(user.getNick());
        params.add(user.getMood());
        params.add(user.getHead());
        params.add(user.getUserId());
        //3.
        int row = BaseDao.executeUpdate(sql,params);
        return row;
    }

    /**
     * 通过用户名查询用户对象
     *  1. 定义sql语句
     *  2. 设置参数集合
     *  3. 调用BaseDao的查询方法
     * @param userName
     * @return
     */
    public User queryUserByName(String userName) {
        // 1. 定义sql语句
        String sql = "select * from tb_user where uname = ?";

        // 2. 设置参数集合
        List<Object> params = new ArrayList<>();
        params.add(userName);

        // 3. 调用BaseDao的查询方法
        User user = (User) BaseDao.queryRow(sql, params, User.class);

        return user;
    }

    /**
     *  1. 定义sql语句 通过用户id查询除了自己当前登录用户之外是否有其他用户占用了该昵称
     *
     *     指定昵称 nick (前台传递的参数)      当前用户 userId （session作用域中）
     *
     *     String sql = "select * from tb_user where nick = ? and userId != ?";
     *
     *  2. 设置参数集合
     *
     *  3. 调用BaseDao方法的查询方法
     * @param nick
     * @param userId
     * @return
     */
    public User queryUserByNickAndUserId(String nick, Integer userId) {
        //1.
        String sql = "select * from tb_user where nick = ? and userId != ?";
        //2.
        List<Object> params = new ArrayList<>();
        params.add(nick);
        params.add(userId);
        //3.
        User user = (User)BaseDao.queryRow(sql, params, User.class);
        return user;
    }


    /**
     通过用户名查询用户对象， 返回用户对象
     1. 获取数据库连接
     2. 定义sql语句
     3. 预编译
     4. 设置参数
     5. 执行查询，返回结果集
     6. 判断并分析结果集
     7. 关闭资源
     * @param userName
     * @return
     */
//    public User queryUserByName02(String userName) {
//        User user = null;
//        Connection connection = null;
//        PreparedStatement preparedStatement = null;
//        ResultSet resultSet = null;
//
//        try {
//            // 1. 获取数据库连接
//            connection = DBUtil.getConnetion();
//            // 2. 定义sql语句
//            String sql = "select * from tb_user where uname = ?";
//            // 3. 预编译
//            preparedStatement = connection.prepareStatement(sql);
//            // 4. 设置参数
//            preparedStatement.setString(1, userName);
//            // 5. 执行查询，返回结果集
//            resultSet = preparedStatement.executeQuery();
//            // 6. 判断并分析结果集
//            if (resultSet.next()) {
//                user = new User();
//                user.setUserId(resultSet.getInt("userId"));
//                user.setUname(userName);
//                user.setHead(resultSet.getString("head"));
//                user.setMood(resultSet.getString("mood"));
//                user.setNick(resultSet.getString("nick"));
//                user.setUpwd(resultSet.getString("upwd"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            // 7. 关闭资源
//            DBUtil.close(resultSet,preparedStatement,connection);
//        }
//
//        return user;
//    }


}
