package com.zans.netty;

import com.zans.config.ExecuteStatusEnum;
import com.zans.dao.AsynTaskRecordDao;
import com.zans.model.AsynTaskRecord;
import com.zans.vo.AsynReqVo;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.zans.config.AsynConstants.*;

/**
 *
 * @Title: NettyServerHandler
 * @Description: 服务端业务逻辑
 * @Version:1.0.0
 * @author pancm
 * @date 2017年10月8日
 */
@Service("nettyServerHandler")
@Slf4j
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

	/** 空闲次数 */
	private int idle_count = 1;
	/** 收到次数 */
	private int count = 0;


	@Resource
	private AsynTaskRecordDao asynTaskRecordDao;

	/**
	 * 建立连接时，发送一条消息
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("收到新连接的客户端,地址:" + ctx.channel().remoteAddress());
		super.channelActive(ctx);
	}

	/**
	 * 超时处理 如果5秒没有接受客户端的心跳，就触发; 如果超过两次，则直接关闭;
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {
		if (obj instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) obj;
			if (IdleState.READER_IDLE.equals(event.state())) { // 如果读通道处于空闲状态，说明没有接收到心跳命令
				log.info("已经5秒没有接收到客户端的信息了");
//				if (idle_count > 5) {
//					log.warn("关闭这个不活跃的channel");
//					ctx.channel().close();
//					idle_count=1;
//				}
				idle_count++;
			}
		} else {
			super.userEventTriggered(ctx, obj);
		}
	}

	/**
	 * 业务逻辑处理
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)  {
		if(!(msg instanceof AsynReqVo.AsynReqVoMsg)){
			log.warn("未知数据:{}!",msg);
			return;
		}
		try {
			AsynReqVo.AsynReqVoMsg reqVoMsg =(AsynReqVo.AsynReqVoMsg)msg;
			int state = reqVoMsg.getState();
			String name = reqVoMsg.getName();
			NettyChannelMap.addChannel(name,ctx.channel());
			//如果是心跳
			if(state == STATE_HEARTBEAT){
				log.debug("收到ip:{},name:{},客户端的心跳！",ctx.channel().remoteAddress(),name);
				if(count%100==0){
					log.info("收到ip:{},name:{},客户端的心跳！",ctx.channel().remoteAddress(),name);
				}
				count++;
				return;
			}
			if(state == STATE_RECEIVE){
				log.info("收到ip:{},name:{},客户端的数据已接收！",ctx.channel().remoteAddress(),name);
				String taskId = reqVoMsg.getTaskId();
				AsynTaskRecord asynTaskRecord = new AsynTaskRecord();
				asynTaskRecord.setTaskId(taskId);
				asynTaskRecord.setExecuteStatus(ExecuteStatusEnum.RECEIVE.getStatus());
				asynTaskRecord.setReceiveTime("");
				asynTaskRecordDao.update(asynTaskRecord);
				return;
			}
			if(state == STATE_HANDLER_SUC){
				log.info("收到ip:{},name:{},客户端的处理成功数据！{}",ctx.channel().remoteAddress(),name,reqVoMsg);
				updateAsynTask(reqVoMsg, ExecuteStatusEnum.RETURN_SUCCESS.getStatus());
				return;
			}
			if(state == STATE_HANDLER_ERR){
				log.info("收到ip:{},name:{},客户端的处理失败数据！{}",ctx.channel().remoteAddress(),name,reqVoMsg);
				updateAsynTask(reqVoMsg, ExecuteStatusEnum.RETURN_FAIL.getStatus());
				return;
			}

		} catch (Exception e) {
			log.error("netty业务逻辑处理失败！接受的msg:{}",msg,e);
		} finally {
			ReferenceCountUtil.release(msg);
		}

	}

	private void updateAsynTask(AsynReqVo.AsynReqVoMsg reqVoMsg, int i) {
		String taskId = reqVoMsg.getTaskId();
		AsynTaskRecord asynTaskRecord = new AsynTaskRecord();
		asynTaskRecord.setTaskId(taskId);
		asynTaskRecord.setExecuteStatus(i);
		asynTaskRecord.setFinishTime("");
		asynTaskRecord.setRespData(reqVoMsg.getData());
		asynTaskRecordDao.update(asynTaskRecord);
	}



	/**
	 * 异常处理
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
		log.error("Exception caught:{}",cause.getMessage());
	}
}
