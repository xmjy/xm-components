package indi.xm.component.libcommon.model.bo;

import lombok.Builder;
import lombok.Data;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @author: albert.fang
 * @date: 2022/1/11 17:18
 */
@Data
@Builder
public class InitMailUtilBO {
    /**
     * 邮件输出地址
     */
    private String emlFilePath;
    /**
     * 回退邮件地址
     */
    private String returnPath;
    /**
     * 邮件发送备份路径
     */
    private String backupFilePath;
    /**
     * 告警邮件收件人
     */
    private String warningMailTo;
    /**
     * 邮件发送人
     */
    private String mailFrom;
    /**
     * 直发邮件客户端
     */
    private JavaMailSender mailSender;
    /**
     * 发送email的业务类型（逗号分隔）
     */
    private String emailFlagTypes;
    /**
     * 发送email的优先级（逗号分隔）
     */
    private String emailPriorities;
}
