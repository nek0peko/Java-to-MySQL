import java.sql.*;

public class rmv {
    static int num;
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String URL = "jdbc:mysql://192.168.31.250:3306/DB1?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false";
    static final String userName = "alice";
    static final String passWord = "123456";
    
    public static void check(String[] args) {
        if(args.length != 3) {
            System.out.println("参数输入个数错误！");
            System.out.println("请使用 'rmv 出库数量 配件ID 仓库ID' 的格式。");
            System.exit(0);
        }

        try {
            num = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("出库数量类型错误！");
            System.exit(0);
        }

        if(num <= 0) {
            System.out.println("出库数量应大于0！");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        rmv.check(args);
        boolean pid_exist = false;
        boolean wid_exist = false;
        String PID = args[1];
        String WID = args[2];
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(URL, userName, passWord);
            stmt = conn.createStatement();
            String sql1 = "select pid, wid from partsrepertory";
            ResultSet rs = stmt.executeQuery(sql1);
            while(rs.next()) {
                if(PID.equals(rs.getString("pid"))) {
                    pid_exist = true;
                }
                if(WID.equals(rs.getString("wid"))) {
                    wid_exist = true;
                }
            }
            rs.close();
            if(!(pid_exist && wid_exist)) {
                System.out.println("配件号或仓库号不存在！");
            } else {
                String sql2 = "update partsrepertory set stock = stock - ? where pid = ? and wid = ? and (stock - ?) >= 0";
                pstmt = conn.prepareStatement(sql2);
                pstmt.setInt(1, num);
                pstmt.setString(2, PID);
                pstmt.setString(3, WID);
                pstmt.setInt(4, num);
                int n = pstmt.executeUpdate();
                if(n == 1) {
                    System.out.println("出库成功！");
                }
                else {
                    System.out.println("库存不足！");
                }
                pstmt.close();
            }
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if(pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException se2) {
                //Do nothing
            }
            try {
                if(stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                //Do nothing
            }
            try {
                if(conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
