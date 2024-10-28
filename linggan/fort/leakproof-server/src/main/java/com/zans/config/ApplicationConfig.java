package com.zans.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;


@Configuration
@Order(1)
public class ApplicationConfig {

    @Value("${guacamole.videoUrl:/home/video}")
    private String videoUrl;

    @Value("${guacamole.hostname:192.168.10.32}")
    private String hostname;

    @Value("${guacamole.port:4822}")
    private Integer port;

    @Value("${guacamole.username:root}")
    private String username;

    @Value("${guacamole.pwd:lgwy@2020}")
    private String pwd;

    @Value("${guacamole.addr:http://192.168.10.32:8008}")
    private String addr;

    @Value("${expire.time:6000}")
    private Integer expireTime;

    @Value("${user.id:43}")
    private Integer id;

    @Value("#{'${video.resolution:}'.split(',')}")
    private List<String> resolution;

    @Value("${video.transcribe}")
    private Integer transcribe;

    @Value("${video.decode:0}")
    private Integer decode;

    @Value("${video.decode_server:http://192.168.10.91:8081/api/decode}")
    private String decodeServerUrl;

    @Value("${sftp.video_path}")
    private String videoPath;

    public  String getUsername() {
        return username;
    }

    public  String getPwd() {
        return pwd;
    }

    public String getHostname() {
        return hostname;
    }

    public Integer getPort() {
        return port;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Integer getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Integer expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getResolution() {
        return resolution;
    }

    public void setResolution(List<String> resolution) {
        this.resolution = resolution;
    }

    public Integer getTranscribe() {
        return transcribe;
    }

    public void setTranscribe(Integer transcribe) {
        this.transcribe = transcribe;
    }

    public Integer getDecode() {
        return decode;
    }

    public void setDecode(Integer decode) {
        this.decode = decode;
    }

    public String getDecodeServerUrl() {
        return decodeServerUrl;
    }

    public void setDecodeServerUrl(String decodeServerUrl) {
        this.decodeServerUrl = decodeServerUrl;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
}
