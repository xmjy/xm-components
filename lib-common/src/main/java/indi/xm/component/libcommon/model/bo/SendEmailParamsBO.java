package indi.xm.component.libcommon.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: albert.fang
 * @date: 2019-11-27
 * @description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailParamsBO {
    /**
     * 发件人
     */
    private String emailFrom;
    /**
     * 收件人集合
     */
    private String emailTo;
    /**
     * 抄送人集合
     */
    private String emailCopyTo;
    /**
     * 回复人集合
     */
    private String emailReplyTo;
    /**
     * 附件集合
     */
    private List<String> attachments;
    /**
     * 主题
     */
    private String title;
    /**
     * 正文
     */
    private String body;
    /**
     * 是否是html
     */
    private Boolean isHtml;
    /**
     * 发送email的业务类型
     */
    private String emailFlagType;
    /**
     * 发送email的优先级
     */
    private String emailPriority;
}
