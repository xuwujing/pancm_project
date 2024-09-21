package com.pancm.handler;



public interface QHandler extends Q{
    /**
     * 获取优先级
     *
     * @return
     */
    int getPriority();

    /**
     * 获取类型
     *
     * @return
     */
    Integer getType();


    String getName(String s,Integer type);


}
