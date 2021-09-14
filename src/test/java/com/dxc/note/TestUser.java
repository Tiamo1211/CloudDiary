package com.dxc.note;



import cn.hutool.crypto.digest.DigestUtil;
import com.dxc.note.dao.BaseDao;
import com.dxc.note.dao.UserDao;
import com.dxc.note.po.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestUser {

    @Test
    public void testQueryUserByName() {
        UserDao userDao = new UserDao();
        User user = userDao.queryUserByName("admin");
        System.out.println(user.getUpwd());
//        String userPwd = DigestUtil.md5Hex(user.getUpwd());
//        System.out.println(userPwd);
    }

    @Test
    public void testAdd() {
       String sql = "insert into tb_user (uname,upwd,nick,head,mood) values (?,?,?,?,?)";
       List<Object> params = new ArrayList<>();
       params.add("admin");
       params.add("123");
       params.add("admin");
       params.add("10.jpg");
       params.add("Hello");
       int row = BaseDao.executeUpdate(sql,params);
        System.out.println(row);

    }
}
