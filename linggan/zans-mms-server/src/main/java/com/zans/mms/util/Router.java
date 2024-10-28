package com.zans.mms.util;

import com.zans.base.util.StringHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author xv
 * @since 2020/3/6 13:57
 */
@Data
@Slf4j
public class Router {

    /**
     * 常规route
     */
    private Set<String> normalRouteSet;

    /**
     * 带路径参数的route
     */
    private Set<String> pathVariableRouteSet;

    public Router() {
        this.normalRouteSet = new HashSet<>();
        this.pathVariableRouteSet = new TreeSet<>();
    }

    public Router route(String input) {
        if (StringHelper.isBlank(input)) {
            return this;
        }
        String route = input.trim();
        if (isPathVariable(route)) {
            log.warn("not support path variable#" + route);
            this.pathVariableRouteSet.add(input);
        } else {
            this.normalRouteSet.add(input);
        }
        return this;
    }

    public boolean isValid(String route) {
        if (route == null) {
            return false;
        }
        if (isPathVariable(route)) {
            // TODO
            log.error("not support path variable#" + route);
            return false;
        } else {
            return this.normalRouteSet.toString().contains(route);
        }
    }

    private boolean isPathVariable(String route) {
        return route != null && route.contains(":");
    }

    public List<String> toList() {
        List<String> list = new LinkedList<>();
        list.addAll(this.normalRouteSet);
        list.addAll(this.pathVariableRouteSet);
        Collections.sort(list);
        return list;
    }
}