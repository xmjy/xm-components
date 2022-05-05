package indi.xm.component.libcommon.config;

import indi.xm.component.libcommon.model.bo.InitMailUtilBO;
import indi.xm.component.libcommon.utils.MailUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import javax.annotation.Resource;

/**
 * @author: albert.fang
 * @date: 2022/1/11 17:29
 */
@Configuration
@ConditionalOnProperty(name = "spring.mail.ehire.mailFrom")
public class MailUtilConfig {

    @Value("${spring.mail.ehire.mailFrom}")
    private String mailFrom;
    @Value("${spring.mail.ehire.returnPath}")
    private String returnPath;
    @Value("${spring.mail.ehire.emlFilePath}")
    private String emlFilePath;
    @Value("${spring.mail.ehire.backupFilePath}")
    private String backupFilePath;
    @Value("${spring.mail.ehire.warningMailTo}")
    private String warningMailTo;
    @Value("${spring.mail.ehire.emailFlagTypes}")
    private String emailFlagTypes;
    @Value("${spring.mail.ehire.emailPriorities}")
    private String emailPriorities;

    @Resource
    private JavaMailSender mailSender;

    @Bean
    public MailUtil mailUtil(InitMailUtilBO initMailUtilBO) {
        return new MailUtil(initMailUtilBO);
    }

    @Bean
    public InitMailUtilBO initMailUtilBO() {
        return InitMailUtilBO.builder()
                .emlFilePath(emlFilePath)
                .returnPath(returnPath)
                .backupFilePath(backupFilePath)
                .warningMailTo(warningMailTo)
                .mailFrom(mailFrom)
                .mailSender(mailSender)
                .emailFlagTypes(emailFlagTypes)
                .emailPriorities(emailPriorities)
                .build();
    }
}
