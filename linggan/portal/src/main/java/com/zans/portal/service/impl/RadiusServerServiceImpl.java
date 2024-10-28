package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.PageResult;
import com.zans.portal.dao.RadiusServerMapper;
import com.zans.portal.model.RadiusServer;
import com.zans.portal.service.IRadiusServerService;
import com.zans.portal.vo.device.DeviceResponseVO;
import com.zans.portal.vo.device.DeviceSearchVO;
import com.zans.portal.vo.radius.ServerRespVO;
import com.zans.portal.vo.radius.ServerSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

/**
 * @author yhj
 * @since 2020/4/17 12:02
 */
@Service
@Slf4j
public class RadiusServerServiceImpl extends BaseServiceImpl<RadiusServer> implements IRadiusServerService {

    RadiusServerMapper serverMapper;

    @Resource
    public void setServerMapper(RadiusServerMapper serverMapper) {
        super.setBaseMapper(serverMapper);
        this.serverMapper = serverMapper;
    }

    @Override
    public PageResult<ServerRespVO> getRadiusServerPage(ServerSearchVO reqVO) {
        String orderBy = reqVO.getSortOrder();
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<ServerRespVO> list = serverMapper.getRadiusServerPage(reqVO);
        return new PageResult<ServerRespVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }

    public static Connection getConnection(String diver, String username, String password, String dbname, String iphost, Integer port) {
        // 定义连接
        Connection connection = null;
        try {
            // 加载驱动 com.mysql.jdbc.Driver
            Class.forName(diver);
            connection = DriverManager.getConnection("jdbc:mysql://" + iphost + ":" + port + "/" + dbname + "", username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }


}
