package com.zans.schedule;

import com.zans.dao.DeviceCipherDao;
import com.zans.dao.DeviceCipherRuleDao;
import com.zans.dao.SysConstantDao;
import com.zans.model.DeviceCipher;
import com.zans.model.DeviceCipherRule;
import com.zans.model.SysConstant;
import com.zans.vo.DeviceCipherVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Configuration
@EnableScheduling
@Slf4j
public class PwdReset implements SchedulingConfigurer {

    @Resource
    private SysConstantDao sysConstantDao;

    @Resource
    private DeviceCipherDao deviceCipherDao;

    @Resource
    private DeviceCipherRuleDao deviceCipherRuleDao;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(
                () -> {
                    List<DeviceCipher> deviceCiphers = deviceCipherDao.queryAll(new DeviceCipherVO());
                    DeviceCipherRule query = deviceCipherRuleDao.query();
                    SysConstant sysConstant = sysConstantDao.selectOne(new SysConstant("pwd_type"));
                    String[] split = sysConstant.getConstantValue().split(",");
                    List<Integer> data = new ArrayList<>();
                    for (String s : split) {//将对应字符的
                        switch (Integer.parseInt(s)){
                            case 1:
                                for (int i = 65; i <= 90; i++) {
                                    data.add(i);
                                }
                                break;
                            case 2:
                                for (int i = 97; i <= 122; i++) {
                                    data.add(i);
                                }
                                break;
                            case 3:
                                for (int i = 48; i < 57; i++) {
                                    data.add(i);
                                }
                                break;
                            case 4:
                                for (int i = 33; i < 38; i++) {
                                    data.add(i);
                                }
                                break;
                        }
                    }
                    Collections.shuffle(data);
                    int size = data.size();
                    int[] ints = data.stream().mapToInt(x -> x).toArray();
                    for (DeviceCipher deviceCipher : deviceCiphers) {
                        StringBuilder newPwd = new StringBuilder();
                        //根据规则生成密码
                        for (Integer i = 0; i < query.getRuleLength(); i++) {
                            int randomNum = new Random().nextInt(size);
                            int num = ints[randomNum];
                            newPwd.append((char)num);
                        }
                        deviceCipher.setPassword(newPwd.toString());
                    }
                    deviceCipherDao.insertOrUpdateBatch(deviceCiphers);
                },
                triggerContext -> {
                    SysConstant sysConstant = new SysConstant();
                    sysConstant.setConstantKey("pwd_schedule");
                    sysConstant = sysConstantDao.selectOne(sysConstant);
                    String cron = sysConstant.getConstantValue();
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }

}
