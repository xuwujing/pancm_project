# 部署
- supervisorctl stop job
# 简单数字
- scp -r ./target/job.jar root@27.10.26.206:/home/release/job
- scp -r ./target/job.jar root@10.0.6.212:/home/release/job
- scp -r ./target/job.jar root@27.10.27.253:/home/release/job
- supervisorctl start job
- tail -f /home/release/logs/job/job.out.log
