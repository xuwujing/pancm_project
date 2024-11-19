package com.pancm.test.design.log;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.pancm.test.design.log.strategy.EmailSensitiveStrategy;
import com.pancm.test.design.log.strategy.PhoneSensitiveStrategy;
import com.pancm.test.design.log.strategy.SensitiveStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class SensitiveMessageConverter extends MessageConverter {


    private static final List<SensitiveStrategy> patterns = new ArrayList<>();
    private volatile boolean init = false;

    @Override
    public String convert(ILoggingEvent event) {
        initRuleConfig();
        return doLayout(event);
    }
    private void initRuleConfig() {
        if (patterns.isEmpty()) {
            synchronized (SensitiveMessageConverter.class) {
                if (!init){
                    init = true;
                    //添加默认策略
                    addDefaultPatterns();
                }
            }
        }
    }

    public String doLayout(final ILoggingEvent event) {

        //String message = super.doLayout(event);
        String message = event.getFormattedMessage();
        for (SensitiveStrategy sensitiveStrategy : patterns) {
            Matcher matcher = sensitiveStrategy.pattern().matcher(message);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                String group = matcher.group(0);
                matcher.appendReplacement(sb, sensitiveStrategy.resolve(group));
            }
            matcher.appendTail(sb);
            message = sb.toString();
        }
        return message;

    }
    /**
     * 默认规则
     */
    private void addDefaultPatterns() {
        addPattern(new PhoneSensitiveStrategy());
        addPattern(new EmailSensitiveStrategy());
    }


    /**
     * 向后添加规则
     * @param sensitiveStrategy
     */
    public static void addPattern(SensitiveStrategy sensitiveStrategy) {
        patterns.add(sensitiveStrategy);
    }

    /**
     * 向前添加规则，优先级越高
     * @param sensitiveStrategy
     */
    public static void addFirstPattern(SensitiveStrategy sensitiveStrategy) {
        patterns.add(0,sensitiveStrategy);
    }


}
