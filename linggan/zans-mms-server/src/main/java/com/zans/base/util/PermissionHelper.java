package com.zans.base.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zans.mms.vo.perm.DataPermCacheVO;
import com.zans.mms.vo.user.MenuItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zans.mms.config.MMSConstants.MODULE_TYPE_APP;
import static com.zans.mms.config.MMSConstants.MODULE_TYPE_PC;

/**
* @Title: PermissionHelper
* @Description: 权限缓存帮助类
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/9/21
*/
@Slf4j
@Component
public class PermissionHelper {

    /**
     * 角色权限缓存
     */
    private Cache<String, List<DataPermCacheVO>> dataPermCache;

    /**
     * 角色菜单缓存
     */
    private Cache<String, List<MenuItem>> roleMenuItemCache;

    /**
     * 角色路由缓存
     */
    private Cache<String, List<String>> roleRouteCache;


    @PostConstruct
    private void init() {
        log.info("permission Cache init");
        dataPermCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.DAYS)
                .maximumSize(100_000)
                .build();

        roleMenuItemCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.DAYS)
                .maximumSize(100_000)
                .build();

        roleRouteCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.DAYS)
                .maximumSize(100_000)
                .build();

    }



    public DataPermCacheVO getRoleDataPerm(String jurisdictionId, Integer permId){
        if (StringUtils.isEmpty(jurisdictionId)) {
            return null;
        }
        List<DataPermCacheVO> vos = dataPermCache.getIfPresent(jurisdictionId);
        if (vos != null && vos.size()>0){
            for (DataPermCacheVO vo : vos) {
                if (permId.equals(vo.getPermId())){
                    return vo;
                }
            }
        }
        return null;
    }

    public void setRoleDataPerm(List<DataPermCacheVO> rolePermList){
        if (rolePermList != null && rolePermList.size() > 0){
            String key = rolePermList.get(0).getJurisdictionId();
            dataPermCache.put(key,rolePermList);
        }
    }

    public void removeRoleDataPerm(String key) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        dataPermCache.invalidate(key);
    }

    public void setRoleRoute(String jurisdictionId, List<String> routes){
        if (!StringUtils.isEmpty(jurisdictionId)) {
            roleRouteCache.put(jurisdictionId,routes);
        }
    }

    public List<String>  getRoleRoute(String jurisdictionId){
        if (StringUtils.isEmpty(jurisdictionId)){
            return null;
        }
        return roleRouteCache.getIfPresent(jurisdictionId);
    }

    public void removeRoleRoute(String key) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        roleRouteCache.invalidate(key);
    }

    public void setRoleMenuItem(String jurisdictionId, List<MenuItem> menuItems) {
        if (!StringUtils.isEmpty(jurisdictionId)) {
            roleMenuItemCache.put(jurisdictionId,menuItems);
        }
    }

    public List<MenuItem> getRoleMenuItem(String jurisdictionId) {
        if (StringUtils.isEmpty(jurisdictionId)){
            return null;
        }
        return roleMenuItemCache.getIfPresent(jurisdictionId);
    }

    public void removeRoleMenu(String key) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        //清理pc菜单
        roleMenuItemCache.invalidate(key+ MODULE_TYPE_PC);
        //清理app菜单
        roleMenuItemCache.invalidate(key+MODULE_TYPE_APP);
    }

}