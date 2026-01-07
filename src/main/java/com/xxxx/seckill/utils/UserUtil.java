package com.xxxx.seckill.utils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxxx.seckill.entity.User;
import com.xxxx.seckill.vo.RespBean;

public class UserUtil {

    // 数据库配置常量
    private static final String DB_URL = "jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai";
    private static final String DB_USERNAME = "kanye";
    private static final String DB_PASSWORD = "123456";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    // 用户生成配置常量
    private static final long USER_ID_START = 13000000000L;
    private static final String DEFAULT_SALT = "1a2b3c";
    private static final String DEFAULT_PASSWORD = "123456";

    // 登录和文件配置常量
    private static final String LOGIN_URL = "http://localhost:8080/login/doLogin";
    private static final String TOKEN_FILE_PATH = "/home/ubuntu/codes/javas/seckill-project/config.txt";

    /**
     * 创建用户对象列表
     * @param count 用户数量
     * @return 用户列表
     */
    private static List<User> createUserList(int count) {
        List<User> users = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setId(USER_ID_START + i);
            user.setNickname("User" + i);
            user.setSalt(DEFAULT_SALT);
            user.setPassword(MD5Util.inputPassToDBPass(DEFAULT_PASSWORD, user.getSalt()));
            user.setRegisterDate(LocalDateTime.now());
            user.setLoginCount(0);
            users.add(user);
        }
        System.out.println("create user");
        return users;
    }

    /**
     * 批量插入用户到数据库
     * @param users 用户列表
     * @throws Exception 数据库操作异常
     */
    private static void insertUsersToDatabase(List<User> users) throws Exception {
        String sql = "insert into t_user(login_count, nickname, register_date, salt, password, id)values(?,?,?,?,?,?)";

        try (Connection connection = getConn();
             java.sql.PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (User user : users) {
                preparedStatement.setInt(1, user.getLoginCount());
                preparedStatement.setString(2, user.getNickname());
                preparedStatement.setTimestamp(3, Timestamp.valueOf(user.getRegisterDate()));
                preparedStatement.setString(4, user.getSalt());
                preparedStatement.setString(5, user.getPassword());
                preparedStatement.setLong(6, user.getId());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
        System.out.println("inserted to db");
    }

    /**
     * 登录并获取用户token
     * @param user 用户对象
     * @return token字符串
     * @throws Exception HTTP请求或JSON解析异常
     */
    private static String loginAndGetToken(User user) throws Exception {
        URL url = new URL(LOGIN_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // 发送登录请求
        String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPassToFormPass(DEFAULT_PASSWORD);
        try (OutputStream out = connection.getOutputStream()) {
            out.write(params.getBytes());
            out.flush();
        }

        // 读取响应
        String response;
        try (InputStream in = connection.getInputStream();
             ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
            byte[] buff = new byte[1024];
            int len;
            while ((len = in.read(buff)) >= 0) {
                bout.write(buff, 0, len);
            }
            response = new String(bout.toByteArray());
        } finally {
            connection.disconnect();
        }

        // 解析token
        ObjectMapper mapper = new ObjectMapper();
        RespBean respBean = mapper.readValue(response, RespBean.class);
        String userTicket = (String) respBean.getObject();
        System.out.println("create userTicket : " + user.getId());
        return userTicket;
    }

    /**
     * 生成token并写入文件
     * @param users 用户列表
     * @throws Exception 文件操作或网络请求异常
     */
    private static void generateTokensToFile(List<User> users) throws Exception {
        File file = new File(TOKEN_FILE_PATH);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (User user : users) {
                String userTicket = loginAndGetToken(user);
                String row = user.getId() + "," + userTicket;
                writer.write(row);
                writer.newLine();
                System.out.println("write to file : " + user.getId());
            }
        }
        System.out.println("over");
    }

    /**
     * 创建用户并生成token文件（主协调方法）
     * @param count 用户数量
     * @throws Exception 操作异常
     */
    private static void createUser(int count) throws Exception{
        // 1. 创建用户对象列表
        List<User> users = createUserList(count);

        // 2. 批量插入数据库
        insertUsersToDatabase(users);

        // 3. 生成token并写入文件
        generateTokensToFile(users);
    }

    /**
     * 获取数据库连接
     * @return 数据库连接对象
     * @throws Exception 数据库连接异常
     */
    private static Connection getConn() throws Exception {
        Class.forName(DB_DRIVER);
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    public static void main(String[] args) throws Throwable {
        createUser(3);
    }
}
