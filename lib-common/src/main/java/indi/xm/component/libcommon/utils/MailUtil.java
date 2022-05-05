package indi.xm.component.libcommon.utils;

import indi.xm.component.libcommon.model.bo.FailMailRecordBO;
import indi.xm.component.libcommon.model.bo.InitMailUtilBO;
import indi.xm.component.libcommon.model.bo.SendEmailParamsBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.ObjectUtils;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author: albert.fang
 * @date: 2022/1/11 16:45
 */
public class MailUtil {
    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

    private static final String MAIL_ENCODING = "UTF-8";
    private static final String CRLF = "\n";
    private static final String PROJECT_NAME = "Ehire";
    private static final String BOUNDARY = "=_9ed30801b0c180c69b3c5d74fede848e";
    private static final int MAX_WRONG_COUNT = 10;
    private final String emlFilePath;
    private final String returnPath;
    private final String backupFilePath;
    private final String warningMailTo;
    private final String mailFrom;
    private final JavaMailSender mailSender;
    /**
     * 发送email的业务类型
     */
    private final List<String> emailFlagTypes;
    /**
     * 发送email的优先级
     */
    private final List<String> emailPriorities;

    public MailUtil(InitMailUtilBO initMailUtilBO) {
        this.emlFilePath = initMailUtilBO.getEmlFilePath();
        this.returnPath = initMailUtilBO.getReturnPath();
        this.backupFilePath = initMailUtilBO.getBackupFilePath();
        this.warningMailTo = initMailUtilBO.getWarningMailTo();
        this.mailFrom = initMailUtilBO.getMailFrom();
        this.mailSender = initMailUtilBO.getMailSender();
        this.emailFlagTypes = Arrays.asList(initMailUtilBO.getEmailFlagTypes().split(","));
        this.emailPriorities = Arrays.asList(initMailUtilBO.getEmailPriorities().split(","));
    }

    /**
     * 通过写文件的方式发送邮件
     *
     * @param sendEmailParamsBO:
     * @return :
     * @author: albert.fang
     * @date: 2022/1/12 17:49
     */
    public boolean sendMail(SendEmailParamsBO sendEmailParamsBO) {
        try {
            checkParam(sendEmailParamsBO);
            String emlData = toEml(sendEmailParamsBO);
            if (!EhireStringUtil.isEmpty(emlData) && !EhireStringUtil.isEmpty(emlFilePath)) {
                List<String> failPathList = new ArrayList<>();
                List<FailMailRecordBO> failMailRecordBOList = new ArrayList<>();
                boolean allPathIsWrong = true;
                String[] pathArr = emlFilePath.split(";");
                for (String s : pathArr) {
                    if (EhireStringUtil.isEmpty(s)) {
                        logger.error("邮件输出路径为空:emlFilePath:{}", emlFilePath);
                        throw new RuntimeException("邮件输出路径为空");
                    }
                }
                int seq;
                String path = "";
                while (failPathList.size() < pathArr.length) {
                    try {
                        //从文件数组中随机选择一个
                        seq = new Random().nextInt(pathArr.length);
                        path = pathArr[seq];
                        createEmlFile(emlData, path, sendEmailParamsBO.getEmailPriority());
                        allPathIsWrong = false;
                        break;
                    } catch (Exception e) {
                        // 如果失败，则读取下个目录
                        if (!failPathList.contains(path)) {
                            failPathList.add(path);
                        }
                        //添加某个路径出错10次以上，发送预警邮件
                        recordFail(failMailRecordBOList, path);
                        logger.error("创建邮件文件失败：emailfrom:{},emailto:{},path:{}", sendEmailParamsBO.getEmailFrom(), sendEmailParamsBO.getEmailTo(), path);
                    }
                }
                // 如果失败所有路径都非法，则备份到本地机器
                if (allPathIsWrong) {
                    try {
                        createEmlFile(emlData, backupFilePath, sendEmailParamsBO.getEmailPriority());
                    } catch (Exception e) {
                        logger.error("备份到本地机器失败：backupFilePath：" + backupFilePath + "，errormsg:", e);
                    }
                    //发送告警邮件
                    SendEmailParamsBO sendEmailParamsBo2 = new SendEmailParamsBO();
                    sendEmailParamsBo2.setEmailFrom(mailFrom);
                    sendEmailParamsBo2.setEmailTo(warningMailTo);
                    sendEmailParamsBo2.setEmailCopyTo(null);
                    sendEmailParamsBo2.setEmailReplyTo(null);
                    sendEmailParamsBo2.setAttachments(null);
                    sendEmailParamsBo2.setTitle("邮件系统发送失败");
                    sendEmailParamsBo2.setBody("无法写入文件服务器磁盘，请检查, 备份文件地址:" + backupFilePath);
                    sendEmailParamsBo2.setIsHtml(false);
                    sendMailDirect(sendEmailParamsBo2);
                    logger.info("所有路径都非法,发送告警邮件：mailFrom:{},warningMailTo：{}", mailFrom, warningMailTo);
                    throw new RuntimeException("所有路径都非法");
                }
            }
        } catch (Exception e) {
            logger.error("发送邮件异常：", e);
            return false;
        }
        return true;
    }

    /**
     * 校验参数
     *
     * @param sendEmailParamsBO:
     * @return :
     * @author: albert.fang
     * @date: 2022/1/12 17:47
     */
    private void checkParam(SendEmailParamsBO sendEmailParamsBO) {
        String emailFlagType = sendEmailParamsBO.getEmailFlagType();
        if (EhireStringUtil.isEmpty(emailFlagType) || !emailFlagTypes.contains(emailFlagType)) {
            throw new IllegalArgumentException("emailFlagType输入错误");
        }
        String emailPriority = sendEmailParamsBO.getEmailPriority();
        if (EhireStringUtil.isEmpty(emailPriority) || !emailPriorities.contains(emailPriority)) {
            throw new IllegalArgumentException("emailPriority输入错误");
        }
    }

    private void recordFail(List<FailMailRecordBO> failMailRecordBOList, String failPath) {
        FailMailRecordBO failMailRecordBO = null;
        for (FailMailRecordBO f : failMailRecordBOList) {
            if (failPath.equals(f.getFailPath())) {
                failMailRecordBO = f;
                break;
            }
        }
        if (failMailRecordBO == null) {
            failMailRecordBO = new FailMailRecordBO();
            failMailRecordBO.setFailPath(failPath);
            failMailRecordBO.setFailCount(0);
            failMailRecordBO.setSendMailFlag(false);
            failMailRecordBO.setLastFailTime(null);
            failMailRecordBO.setFirstFailTime(LocalDateTime.now());
            failMailRecordBOList.add(failMailRecordBO);
        }
        //错误记录在一小时内已经满10次后，就不要再记录了
        if (!failMailRecordBO.getSendMailFlag()) {
            failMailRecordBO.setFailCount(failMailRecordBO.getFailCount() + 1);
            failMailRecordBO.setLastFailTime(LocalDateTime.now());
        }
        //检查是否在一个小时内错误记录满10次
        if (failMailRecordBO.getFailCount() >= MAX_WRONG_COUNT
                && failMailRecordBO.getFirstFailTime().plusHours(1).isAfter(failMailRecordBO.getLastFailTime())
                && !failMailRecordBO.getSendMailFlag()) {
            //发送告警邮件
            SendEmailParamsBO sendEmailParamsBo2 = new SendEmailParamsBO();
            sendEmailParamsBo2.setEmailFrom(mailFrom);
            sendEmailParamsBo2.setEmailTo(warningMailTo);
            sendEmailParamsBo2.setEmailCopyTo(null);
            sendEmailParamsBo2.setEmailReplyTo(null);
            sendEmailParamsBo2.setAttachments(null);
            sendEmailParamsBo2.setTitle("邮件系统预警");
            sendEmailParamsBo2.setBody("在一个小时内一个路径出错次数满了10次以上，出错路径是:" + failMailRecordBO.getFailPath());
            sendEmailParamsBo2.setIsHtml(false);
            sendMailDirect(sendEmailParamsBo2);
            logger.info("在一个小时内出错次数大于10,发送告警邮件：mailFrom:{},warningMailTo：{}", mailFrom, warningMailTo);
            failMailRecordBO.setSendMailFlag(true);
        }
        //清空出错次数,重置标志位
        if (LocalDateTime.now().minusHours(1).isAfter(failMailRecordBO.getLastFailTime())
                || LocalDateTime.now().minusHours(1).isEqual(failMailRecordBO.getLastFailTime())) {
            failMailRecordBOList.remove(failMailRecordBO);
        }
    }

    /**
     * 邮件服务器直发邮件（当前只有发告警邮件时才需要直发邮件，其他场景都是通过写文件的方式发送邮件）
     *
     * @param sendEmailParamsBO:
     * @return :
     * @author: albert.fang
     * @date: 2022/1/12 17:49
     */
    public boolean sendMailDirect(SendEmailParamsBO sendEmailParamsBO) {
        try {
            MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(sendEmailParamsBO.getEmailFrom());
            if (EhireStringUtil.isEmpty(sendEmailParamsBO.getEmailTo())) {
                logger.error("收件人不能为空");
                throw new RuntimeException("收件人不能为空");
            }
            messageHelper.setTo(sendEmailParamsBO.getEmailTo().split(","));
            if (!EhireStringUtil.isEmpty(sendEmailParamsBO.getEmailCopyTo())) {
                String[] ccs = sendEmailParamsBO.getEmailCopyTo().split(",");
                for (String recipient : ccs) {
                    messageHelper.addCc(recipient);
                }
            }
            messageHelper.setSubject(sendEmailParamsBO.getTitle());
            messageHelper.setText(sendEmailParamsBO.getBody(), sendEmailParamsBO.getIsHtml());
            this.mailSender.send(mimeMessage);
        } catch (Exception e) {
            logger.error("直发邮件异常:", e);
            return false;
        }
        return true;
    }

    private void createEmlFile(String emlData, String rootFolder, String priority) {
        if (!rootFolder.endsWith(File.separator)) {
            rootFolder = rootFolder + File.separator;
        }
        String fileDirectory = rootFolder + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                File.separator +
                priority +
                File.separator +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH")) +
                File.separator;
        File file = new File(fileDirectory);
        if (!file.exists()) {
            boolean mkdirsResult = file.mkdirs();
            if (!mkdirsResult) {
                logger.error("创建文件目录失败:fileDirectory：{}", fileDirectory);
                throw new RuntimeException("创建文件目录失败");
            }
        }
        String fileName = fileDirectory + UUID.randomUUID().toString().replace("-", "");
        File newFile = new File(fileName + ".temp");
        OutputStream os = null;
        try {
            if (!newFile.exists()) {
                boolean newFileResult = newFile.createNewFile();
                if (!newFileResult) {
                    logger.error("创建文件失败:fileName：{}", fileName + ".temp");
                    throw new RuntimeException("创建文件失败");
                }
            }
            os = new FileOutputStream(newFile, true);
            byte[] bytes = emlData.getBytes(MAIL_ENCODING);
            os.write(bytes, 0, bytes.length);
            os.flush();
        } catch (Exception e) {
            logger.error("写文件异常:", e);
            throw new RuntimeException("写文件异常");
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error("关闭文件流异常");
                }
            }
        }
        //移动文件
        try {
            Files.move(Paths.get(fileName + ".temp"), Paths.get(fileName + ".mail"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("移动文件异常", e);
            throw new RuntimeException("移动文件异常");
        }
    }

    private String toEml(SendEmailParamsBO sendEmailParamsBO) {
        try {
            StringBuilder mailHeader = new StringBuilder();
            //from
            String mailFromName = "前程无忧（51job）";
            mailFromName = "=?" + MAIL_ENCODING + "?B?" + Base64.getEncoder().encodeToString(mailFromName.getBytes(MAIL_ENCODING)) + "?=";
            mailHeader.append("From:").append(mailFromName).append("<").append(sendEmailParamsBO.getEmailFrom()).append(">").append(CRLF);
            //to
            mailHeader.append("To:").append(sendEmailParamsBO.getEmailTo()).append(CRLF);
            //REPLY-TO
            if (!EhireStringUtil.isEmpty(sendEmailParamsBO.getEmailReplyTo())) {
                mailHeader.append("Reply-To:").append(sendEmailParamsBO.getEmailReplyTo()).append(CRLF);
            } else {
                mailHeader.append("Reply-To:").append(returnPath).append(CRLF);
            }
            //copy-to
            if (!EhireStringUtil.isEmpty(sendEmailParamsBO.getEmailCopyTo())) {
                mailHeader.append("Cc: ").append(sendEmailParamsBO.getEmailCopyTo()).append(CRLF);
            }
            //return path
            mailHeader.append("Return-Path:").append(returnPath).append("\n");
            //subject
            mailHeader.append("Subject: " + "=?" + MAIL_ENCODING + "?B?")
                    .append(Base64.getEncoder().encodeToString(sendEmailParamsBO.getTitle().getBytes(MAIL_ENCODING)))
                    .append("?=").append(CRLF);
            //body
            StringBuilder mailBody = buildMailBody(sendEmailParamsBO);
            mailHeader.append(mailBody);
            return mailHeader.toString();
        } catch (Exception e) {
            logger.error("转换为eml字符串失败:", e);
            throw new RuntimeException("转换为eml字符串失败");
        }
    }

    private StringBuilder buildMailBody(SendEmailParamsBO sendEmailParamsBO) throws Exception {
        StringBuilder mailBody = new StringBuilder("MIME-Version:1.0" + CRLF);
        mailBody.append("X-51JOB-FLAG:").append(PROJECT_NAME).append("_").append(sendEmailParamsBO.getEmailFlagType()).append("_")
                .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))).append("_")
                .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"))).append("88").append(CRLF);
        if (ObjectUtils.isEmpty(sendEmailParamsBO.getAttachments())) {
            //没有附件
            if (!sendEmailParamsBO.getIsHtml()) {
                //文本格式
                mailBody.append("Content-Type:text/plain;");
                mailBody.append("charset=\"").append(MAIL_ENCODING).append("\"").append(CRLF);
                mailBody.append("Content-Transfer-Encoding:base64" + CRLF + CRLF + CRLF);
                // 内容
                if (!EhireStringUtil.isEmpty(sendEmailParamsBO.getBody())) {
                    String context = Base64.getEncoder().encodeToString(sendEmailParamsBO.getBody().getBytes(MAIL_ENCODING));
                    context = addNewLine(context);
                    mailBody.append(context);
                }
                mailBody.append(CRLF).append(".").append(CRLF);
            } else {
                //Html格式
                mailBody.append("Content-Type:multipart/alternative;");
                mailBody.append("boundary=\"").append(BOUNDARY).append("\"").append(CRLF).append(CRLF);
                // 开始
                mailBody.append("--").append(BOUNDARY).append(CRLF);
                mailBody.append("Content-Type:text/html; " + "charset=\"");
                mailBody.append(MAIL_ENCODING.toLowerCase()).append("\"" + CRLF);
                mailBody.append("Content-Transfer-Encoding:base64").append(CRLF).append(CRLF);
                // 内容
                if (!EhireStringUtil.isEmpty(sendEmailParamsBO.getBody())) {
                    String context = Base64.getEncoder().encodeToString(sendEmailParamsBO.getBody().getBytes(MAIL_ENCODING));
                    context = addNewLine(context);
                    mailBody.append(context).append(CRLF).append(CRLF).append(CRLF);
                }
                // 结束
                mailBody.append("--").append(BOUNDARY).append("--").append(CRLF + "." + CRLF);
            }
        } else {
            //有附件
            if (!sendEmailParamsBO.getIsHtml()) {
                //文本格式
                mailBody.append("Content-Type:multipart/mixed;boundary=\"" + BOUNDARY + "\"" + CRLF + CRLF);
                // 开始
                mailBody.append("--" + BOUNDARY + CRLF);
                mailBody.append("Content-Type:text/plain;" + "charset=\"").append(MAIL_ENCODING.toLowerCase()).append("\"").append(CRLF);
            } else {
                //Html格式
                mailBody.append("Content-Type:multipart/mixed;" + "boundary=\"" + BOUNDARY + "\"");
                mailBody.append(CRLF).append(CRLF);
                // 开始
                mailBody.append("--" + BOUNDARY + CRLF);
                mailBody.append("Content-Type:text/html;" + "charset=\"").append(MAIL_ENCODING.toLowerCase()).append("\"").append(CRLF);
            }
            mailBody.append("Content-Transfer-Encoding:base64" + CRLF + CRLF + CRLF);
            // 内容
            if (!EhireStringUtil.isEmpty(sendEmailParamsBO.getBody())) {
                String context = Base64.getEncoder().encodeToString(sendEmailParamsBO.getBody().getBytes(MAIL_ENCODING));
                context = addNewLine(context);
                mailBody.append(context).append(CRLF);
            }
            // 结束
            mailBody.append(CRLF + "--" + BOUNDARY + "--" + CRLF + "." + CRLF);
        }
        return mailBody;
    }

    private String addNewLine(String context) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < context.length(); i++) {
            if ((i + 1) % 76 == 0) {
                stringBuilder.append(context.charAt(i)).append(CRLF);
            } else {
                stringBuilder.append(context.charAt(i));
            }
        }
        return stringBuilder.toString();
    }
}
