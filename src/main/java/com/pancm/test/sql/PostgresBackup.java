package com.pancm.test.sql;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

/**
 * @Author pancm
 * @Description Postgres备份还原
 * navicat可以远程连接ssh(外网ssh登录)，然后通过ssh登录到内网数据库服务器，通过navicat连接内网数据库服务器，
 * @Date  2024/1/25
 * @Param
 * @return
 **/
public class PostgresBackup {
    public static void main(String[] args) {
        String dbName = "store_charge";
        String dbUser = "postgres";
        String dbPassword = "123456";
        String exportPath = dbName+"_backup.sql";
        String pgPath = "D:\\Program Files\\PostgreSQL\\13\\bin\\";
        extracted(dbName, dbUser, dbPassword, pgPath, exportPath);
        dbName = "test_2";
        restoreDatabase(dbName, dbUser, dbPassword, pgPath, exportPath);

    }

    private static void extracted(String dbName, String dbUser, String dbPassword, String pgPath, String exportPath) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost/" + dbName;
            Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            ProcessBuilder pb = new ProcessBuilder(pgPath +"pg_dump", "-U", dbUser, "-f", exportPath, dbName);
            pb.environment().put("PGPASSWORD", dbPassword);
            pb.redirectError(new File("error.log"));
            pb.redirectOutput(new File("output.log"));
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("备份成功！");
            } else {
                System.out.println("备份失败！");
            }
            conn.close();
        } catch (ClassNotFoundException | SQLException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void restoreDatabase (String dbName, String dbUser, String dbPassword, String pgPath, String exportPath) {
        String pgRestorePath = pgPath+ "pg_restore";
        String databaseName = dbName;
        String username = dbUser;
        String password = dbPassword;
        String backupFilePath = exportPath;

        ProcessBuilder processBuilder = new ProcessBuilder(
                pgRestorePath,
                "-U", username,
                "-d", databaseName,
                "-w",
                backupFilePath
        );

        Map<String, String> env = processBuilder.environment();
        env.put("PGPASSWORD", password);
        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("还原成功!");
            } else {
                System.out.println("还原失败!");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost/" + dbName;
            Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            ProcessBuilder pb = new ProcessBuilder(pgPath +"pg_restore", "-U", dbUser, "-f", exportPath, dbName);
            pb.environment().put("PGPASSWORD", dbPassword);
            pb.redirectError(new File("error.log"));
            pb.redirectOutput(new File("output.log"));
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("备份成功！");
            } else {
                System.out.println("备份失败！");
            }
            conn.close();
        } catch (ClassNotFoundException | SQLException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


   
}
