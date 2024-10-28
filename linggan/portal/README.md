Portal of Zans

# 部署
- supervisorctl stop portal
- scp -r ./target/portal.jar root@27.10.28.111:/home/release/portal
- supervisorctl start portal
- tail -f /home/release/logs/portal/portal.out.log


# dev部署
- scp -r ./target/portal.jar root@10.0.6.212:/home/release/portal

# 环境说明
- local(本地)	
- demo（演示环境）
- dev((交管)测试环境)	
- whjg-pre((交管)预发布环境)	
- whjg-prod((交管)正式环境)	
- xyga-pre((襄阳)测试环境)	
- xyga-prod((襄阳)正式环境)	


