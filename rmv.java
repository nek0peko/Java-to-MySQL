import java.sql.*;

public class rmv {
    static int num;
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String URL = "jdbc:mysql://121.36.198.150:3306/GroupFour?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false";
    static final String userName = "root";
    static final String passWord = "123456";
    
    public static boolean check(String[] args) {
        if(args.length != 3) {
            System.out.printf("参数输入个数错误！\n请使用 'rmv 出库数量 配件ID 仓库ID' 的格式。\n");
            return true;
        }

        try {
            num = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.printf("出库数量类型错误！\n");
            return true;
        }

        if(num <= 0) {
            System.out.printf("出库数量应大于0！\n");
            return true;
        }

        return false;
    }

    public static void dosql(String[] args) {
        String PID = args[1];
        String WID = args[2];
        boolean pid_exist = false;
        boolean wid_exist = false;
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
                System.out.printf("配件号或仓库号不存在！\n");
            } else {
                String sql2 = "update partsrepertory set stock = stock - ? where pid = ? and wid = ? and (stock - ?) >= 0";
                pstmt = conn.prepareStatement(sql2);
                pstmt.setInt(1, num);
                pstmt.setString(2, PID);
                pstmt.setString(3, WID);
                pstmt.setInt(4, num);
                int n = pstmt.executeUpdate();
                if(n == 1) {
                    System.out.printf("出库成功！\n");
                }
                else {
                    System.out.printf("库存不足！\n");
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

    public static void main(String[] args) {
        if(rmv.check(args)) {
            System.exit(0);
        }
        rmv.dosql(args);
    }
}
