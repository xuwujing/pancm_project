package com.zans.portal;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.zans.base.util.LicenseHelper;
import com.zans.base.util.SpringContextHolder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.LicenseVO;
import com.zans.base.vo.SelectVO;
import com.zans.portal.service.ISysConstantService;
import com.zans.portal.vo.user.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;

import static com.zans.base.config.BaseConstants.CODE_SUCCESS;

@SpringBootApplication
@EnableTransactionManagement
/*@EnableJdbcHttpSession*/
@EnableCaching
@ComponentScan(basePackages = {"com"})
@EnableScheduling
public class PortalApplication {

    private static final Logger log = LoggerFactory.getLogger(PortalApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(PortalApplication.class, args);
        /*exit(ctx);*/
    }

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        //1、先定义一个convert转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        //2、添加fastjson的配置信息，比如是否要格式化返回的json数据；
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // 渲染 null，避免前端bug
        fastJsonConfig.setSerializerFeatures();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.PrettyFormat,
                SerializerFeature.DisableCircularReferenceDetect);
        //附加：处理中文乱码
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        //3、在convert中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters(fastConverter);
    }

    public static void exit(ConfigurableApplicationContext context) {
        ISysConstantService constantService = SpringContextHolder.getBean(ISysConstantService.class);
        SelectVO license = constantService.findSelectVOByKey("license");
        SelectVO privateKey = constantService.findSelectVOByKey("publicKey");
        ApiResult result = LicenseHelper.verifyLocalLicense(privateKey.getItemValue(), license.getItemValue());
        if (result.getCode() != CODE_SUCCESS) {
            log.error("关闭服务,{}" + result.getMessage());
            int exitCode = SpringApplication.exit(context, (ExitCodeGenerator) () -> 0);
            System.exit(exitCode);
        }
        LicenseVO licenseVO = (LicenseVO) result.getData();
        List<MenuItem> menuItem = (List<MenuItem>) JSONArray.parseArray(licenseVO.getMenuList(), MenuItem.class);
        //todo 更新sys_module表的enable可用度

    }

}
