package indi.xm.component.dynamic.datasource.config.db;

import indi.xm.component.dynamic.datasource.util.DynamicDataSourceUtil;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 实现spring事务处理接口
 *
 * @author: albert.fang
 * @date: 2022/3/9 17:30
 */
public class MultiDataSourceTransaction implements Transaction {
    /**
     * 动态数据源
     */
    private final DataSource dataSource;
    /**
     * 主数据库连接（第一个获取连接的数据库）
     */
    private Connection mainConnection;
    /**
     * 主数据库连接标识符
     */
    private String mainDatabaseIdentification;
    /**
     * 其他数据库连接集合
     */
    private final ConcurrentMap<String, Connection> otherConnectionMap;
    /**
     * 是否自动提交，自动提交必须设置为false
     */
    private boolean autoCommit;

    public MultiDataSourceTransaction(DataSource dataSource) {
        this.dataSource = dataSource;
        otherConnectionMap = new ConcurrentHashMap<>();
        mainDatabaseIdentification = DynamicDataSourceUtil.getDataSource();
    }

    /**
     * 获取连接。主数据库->其他数据库集合->datasource
     *
     * @return :
     * @author: albert.fang
     * @date: 2021/12/13 11:01
     */
    @Override
    public Connection getConnection() throws SQLException {
        String databaseIdentification = DynamicDataSourceUtil.getDataSource();
        if (databaseIdentification.equals(mainDatabaseIdentification)) {
            if (mainConnection == null) {
                openMainConnection();
                mainDatabaseIdentification = databaseIdentification;
            }
            return mainConnection;
        } else {
            if (!otherConnectionMap.containsKey(databaseIdentification)) {
                try {
                    Connection conn = dataSource.getConnection();
                    otherConnectionMap.put(databaseIdentification, conn);
                } catch (SQLException ex) {
                    throw new CannotGetJdbcConnectionException("Could not get JDBC Connection", ex);
                }
            }
            return otherConnectionMap.get(databaseIdentification);
        }

    }

    private void openMainConnection() throws SQLException {
        this.mainConnection = this.dataSource.getConnection();
        this.autoCommit = this.mainConnection.getAutoCommit();
    }

    /**
     * 提交。主数据库提交->其他数据库集合提交
     *
     * @return :
     * @author: albert.fang
     * @date: 2021/12/13 11:08
     */
    @Override
    public void commit() throws SQLException {
        if (this.mainConnection != null && !this.autoCommit) {
            this.mainConnection.commit();
            for (Connection connection : otherConnectionMap.values()) {
                connection.commit();
            }
        }
    }

    /**
     * 回滚。主数据库回滚->其他数据库回滚
     *
     * @return :
     * @author: albert.fang
     * @date: 2021/12/13 11:09
     */
    @Override
    public void rollback() throws SQLException {
        if (this.mainConnection != null && !this.autoCommit) {
            this.mainConnection.rollback();
            for (Connection connection : otherConnectionMap.values()) {
                connection.rollback();
            }
        }
    }

    /**
     * 关闭连接。主数据库关闭连接->其他数据库关闭连接
     *
     * @return :
     * @author: albert.fang
     * @date: 2021/12/13 11:10
     */
    @Override
    public void close() {
        DataSourceUtils.releaseConnection(this.mainConnection, this.dataSource);
        for (Connection connection : otherConnectionMap.values()) {
            DataSourceUtils.releaseConnection(connection, this.dataSource);
        }
    }

    @Override
    public Integer getTimeout() {
        return null;
    }
}

