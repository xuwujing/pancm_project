package com.zans.base.office.validator;

import com.zans.base.util.StringHelper;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.zans.base.config.BaseConstants.SEPARATOR_COMMA;

/**
 * @author xv
 * @since 2020/3/21 23:48
 */
@Builder
@Data
public class ValidateResult {

    private Boolean passed;

    private List<String> messageList;

    public ValidateResult addMessage(String message) {
        if (messageList == null) {
            messageList = new LinkedList<>();
        }
        if (StringHelper.isNotBlank(message)) {
            messageList.add(message);
        }
        return this;
    }

    public static ValidateResult passed() {
        return ValidateResult.builder().passed(true).build();
    }

    public String getMessages() {
        return StringHelper.joinCollection(Collections.singletonList(messageList), SEPARATOR_COMMA);
    }
}
