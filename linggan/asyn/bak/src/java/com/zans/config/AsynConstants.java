package com.zans.config;

import com.zans.vo.AsynReqVo;
import io.netty.channel.Channel;

import java.util.PriorityQueue;

/**
 * @author pancm
 * @Title:
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/21
 */
public class AsynConstants {

   public static final int STATE_HEARTBEAT = 0;
   public static final int STATE_RECEIVE = 1;
   public static final int STATE_HANDLER_SUC = 2;
   public static final int STATE_HANDLER_ERR = 3;

   public static Channel channel;

   public static PriorityQueue<AsynReqVo.AsynReqVoMsg> PRIORITY_QUEUE =new PriorityQueue<>();

   public static void writeAndFlush( String name,int state,String taskId)
   {
      if(channel!=null&& channel.isActive()){
         AsynReqVo.AsynReqVoMsg build = AsynReqVo.AsynReqVoMsg.newBuilder()
                 .setName(name)
                 .setState(state)
                 .setTaskId(taskId)
                 .build();
         channel.writeAndFlush(build);
      }
   }

   public static void writeAndFlush(String name,int state,String taskId,String data){
      if(channel!=null&& channel.isActive()){
         AsynReqVo.AsynReqVoMsg build = AsynReqVo.AsynReqVoMsg.newBuilder()
          .setName(name)
         .setState(state)
         .setTaskId(taskId)
         .setData(data).build();
         channel.writeAndFlush(build);
      }
   }
}
