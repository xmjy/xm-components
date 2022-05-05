package indi.xm.component.dbsource;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author albert.fang
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataSourceConfig {
    @JsonAlias("url")
    private String url;

    @JsonAlias("port")
    private int port;

    @JsonAlias("username")
    private String username;

    @JsonAlias("password")
    private String password;

    @JsonAlias("vhost")
    private String vhost;

    @JsonAlias("max-redirects")
    private int maxRedirects;

    @JsonAlias("max-idle")
    private Integer maxIdle = 8;

    @JsonAlias("min-idle")
    private Integer minIdle = 0;

    @JsonAlias("max-active")
    private Integer maxActive = 8;

    @JsonAlias("max-wait")
    private Long maxWait = -1L;

    @JsonAlias("time-between-eviction-runs")
    private Long timeBetweenEvictionRuns = -1L;

    @JsonAlias("command-timeout")
    private Long commandTimeout = 60000L;

    @JsonAlias("shutdown-timeout")
    private Long shutdownTimeout = 100L;

    @JsonAlias("driver-class-name")
    private String driverClassName = "";

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getVhost() {
        return vhost;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }

    public int getMaxRedirects() {
        return maxRedirects;
    }

    public void setMaxRedirects(int maxRedirects) {
        this.maxRedirects = maxRedirects;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    public Long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(Long maxWait) {
        this.maxWait = maxWait;
    }

    public Long getTimeBetweenEvictionRuns() {
        return timeBetweenEvictionRuns;
    }

    public void setTimeBetweenEvictionRuns(Long timeBetweenEvictionRuns) {
        this.timeBetweenEvictionRuns = timeBetweenEvictionRuns;
    }

    public Long getCommandTimeout() {
        return commandTimeout;
    }

    public void setCommandTimeout(Long commandTimeout) {
        this.commandTimeout = commandTimeout;
    }

    public Long getShutdownTimeout() {
        return shutdownTimeout;
    }

    public void setShutdownTimeout(Long shutdownTimeout) {
        this.shutdownTimeout = shutdownTimeout;
    }
}
