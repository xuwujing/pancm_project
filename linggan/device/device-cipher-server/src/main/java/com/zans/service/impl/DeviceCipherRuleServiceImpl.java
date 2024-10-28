package com.zans.service.impl;

import com.zans.dao.DeviceCipherDao;
import com.zans.dao.DeviceCipherRuleDao;
import com.zans.dao.SysConstantDao;
import com.zans.model.DeviceCipher;
import com.zans.model.DeviceCipherRule;
import com.zans.model.SysConstant;
import com.zans.service.IDeviceCipherRuleService;
import com.zans.vo.ApiResult;
import com.zans.vo.DeviceCipherRuleVO;
import com.zans.vo.DeviceCipherVO;
import com.zans.vo.PwdRuleVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static com.zans.constant.SystemConstant.*;


/**
 * (DeviceCipherRule)表服务实现类
 *
 * @author beixing
 * @since 2021-08-23 16:15:56
 */
@Service("deviceCipherRuleService")
@Slf4j
public class DeviceCipherRuleServiceImpl implements IDeviceCipherRuleService {
    @Resource
    private SysConstantDao sysConstantDao;

    @Resource
    private DeviceCipherRuleDao deviceCipherRuleDao;

    @Resource
    private DeviceCipherDao deviceCipherDao;

    /**
     * 通过ID查询单条数据
     *
     * @param
     * @return 实例对象
     */
    @Override
    public DeviceCipherRule queryById() {
        return this.deviceCipherRuleDao.query();
    }

    /**
     * 重新设置密码规则
     * @param pwdRuleVO
     * @return
     */
    @Override
    public ApiResult resetPdw(PwdRuleVO pwdRuleVO) {
        SysConstant sysConstant = new SysConstant();
        sysConstant.setConstantKey("pwd_schedule");
        sysConstant = sysConstantDao.selectOne(sysConstant);
        if (sysConstant == null) {
            return ApiResult.error("数据不存在");
        }
        //修改动态定时任务cron表达式
        String cron = "";
        switch (pwdRuleVO.getPeriod()) {
            case 7:
                cron = CRON_SEVEN_DAY;
                break;
            case 30:
                cron = CRON_ONE_MOUTH;
                break;
            case 90:
                cron = CRON_THREE_MOUTH;
                break;
            case 180:
                cron = CRON_HALF_YEAR;
                break;
        }
        sysConstant.setConstantValue(cron);
        sysConstantDao.updateByPrimaryKeySelective(sysConstant);
        SysConstant sysConstantType = new SysConstant();
        sysConstantType.setConstantKey("pwd_type");
        sysConstantType = sysConstantDao.selectOne(sysConstantType);
        if (sysConstantType != null) {
            sysConstantType.setConstantValue(StringUtil.join(pwdRuleVO.getCharactersType().toArray(), ","));
        }
        sysConstantDao.updateByPrimaryKeySelective(sysConstantType);//修改密码类型

        SysConstant sysConstantPwdLength = sysConstantDao.selectOne(new SysConstant("pwd_length"));
        if (sysConstantPwdLength != null) {
            sysConstantPwdLength.setConstantValue(pwdRuleVO.getPwdLength() + "");
        }
        sysConstantDao.updateByPrimaryKeySelective(sysConstantPwdLength);//修改密码长度

        DeviceCipherRuleVO deviceCipherRule = new DeviceCipherRuleVO();
        StringBuilder regular = new StringBuilder();
        for (Integer integer : pwdRuleVO.getCharactersType()) {
            switch (integer) {
                case 1:
                    regular.append(PWD_REGEX_CAPITAL_LETTER);
                    break;
                case 2:
                    regular.append(PWD_REGEX_LOWERCASE_LETTER);
                    break;
                case 3:
                    regular.append(PWD_REGEX_NUM);
                    break;
                case 4:
                    regular.append(PWD_REGEX_SPECIAL_CHARACTER);
                    break;
            }
        }
        regular.append("{" + pwdRuleVO.getPwdLength() + ",10}");
        deviceCipherRule.setRuleChar(regular.toString());
        Integer pwdLength = pwdRuleVO.getPwdLength();
        deviceCipherRule.setRuleLength(pwdLength);
        deviceCipherRule.setRulePeriod(pwdRuleVO.getPeriod());
        deviceCipherRule.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        deviceCipherRule.setId(1);
        deviceCipherRuleDao.updateRule(deviceCipherRule);

        /**
         * 更新所有设备密码强弱状态
         */
        List<DeviceCipher> deviceCiphers = deviceCipherDao.queryAll(new DeviceCipherVO());
        for (DeviceCipher deviceCipher : deviceCiphers) {
            try {
                deviceCipher.setIsStrong(Pattern.compile(regular.toString().trim()).matcher(deviceCipher.getPassword()).find() ? 1 : 0);
            } catch (Exception e) {
                log.error("正则匹配error:{}",e);
            }
        }
        deviceCipherDao.insertOrUpdateBatch(deviceCiphers);

        return ApiResult.success();
    }
}
