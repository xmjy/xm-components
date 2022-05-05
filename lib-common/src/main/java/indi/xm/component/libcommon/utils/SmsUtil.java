package indi.xm.component.libcommon.utils;

import indi.xm.component.libcommon.model.bo.sms.InitSmsUtilBO;
import indi.xm.component.libcommon.model.bo.sms.SendSmsParamBO;
import indi.xm.component.libcommon.model.dto.sms.SendSmsDTO;
import indi.xm.component.libcommon.service.api.SmsCenterFeignClient;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: albert.fang
 * @date: 2022/2/10 13:59
 */
public class SmsUtil {
    private static final Logger log = LoggerFactory.getLogger(SmsUtil.class);
    public static final String ERROR_NUM_0 = "0";
    public static final String ERROR_NUM_NEGATIVE_107 = "-107";

    private final String banMobiles;
    private final SmsCenterFeignClient smsCenterFeignClient;

    public SmsUtil(InitSmsUtilBO initSmsUtilBO) {
        this.banMobiles = initSmsUtilBO.getBanMobiles();
        this.smsCenterFeignClient = initSmsUtilBO.getSmsCenterFeignClient();
    }

    /**
     * 发送国际消息
     *
     * @return :
     * @author: albert.fang
     * @date: 2022/2/10 14:50
     */
    public boolean sendInternationalMsg(String smsType, String mobile, String sign, String content, String msgType) {
        String mobileWithSign = sign.concat("-").concat(mobile);
        if (isDisabledMobileNumber(mobileWithSign)) {
            log.error("电话号码被禁用：" + mobileWithSign);
            return false;
        }
        SendSmsParamBO sendSmsParamBO = new SendSmsParamBO();
        sendSmsParamBO.setSystemType(smsType);
        sendSmsParamBO.setMobileNum(mobile);
        sendSmsParamBO.setSign(sign);
        sendSmsParamBO.setMsgType(msgType);
        sendSmsParamBO.setContent(content);
        sendSmsParamBO.setKeyNum("");
        sendSmsParamBO.setCustomer("");
        return sendInternationalMsgByFeign(sendSmsParamBO);
    }

    /**
     * 发送国内消息
     *
     * @param smsType:
     * @param mobile:
     * @param content:
     * @param msgType:
     * @return :
     * @author: albert.fang
     * @date: 2022/2/10 14:50
     */
    public boolean sendMsg(String smsType, String mobile, String content, String msgType) {
        if (isDisabledMobileNumber(mobile)) {
            log.error("电话号码被禁用：" + mobile);
            return false;
        }
        SendSmsParamBO sendSmsParamBO = new SendSmsParamBO();
        sendSmsParamBO.setSystemType(smsType);
        sendSmsParamBO.setMobileNum(mobile);
        sendSmsParamBO.setMsgType(msgType);
        sendSmsParamBO.setContent(content);
        sendSmsParamBO.setKeyNum("0");
        return sendSmsByFeign(sendSmsParamBO);
    }

    /**
     * 判断电话号码是否被禁
     */
    private boolean isDisabledMobileNumber(String mobile) {
        if (EhireStringUtil.isEmpty(mobile)) {
            return false;
        }
        if (!isMobilePhone(mobile)) {
            return false;
        }
        if (EhireStringUtil.isEmpty(banMobiles)) {
            return false;
        } else {
            String[] disMobiles = banMobiles.split(",");
            for (String dis : disMobiles) {
                String substring = mobile.substring(0, 3);
                if (!EhireStringUtil.isEmpty(substring) && substring.equals(dis.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否11位手机号
     */
    private boolean isMobilePhone(String mobile) {
        //判断手机号(移动和联通和电信)
        String mobilePattern = "^(1[0-9]{10}$)";
        Pattern pattern = Pattern.compile(mobilePattern);
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    private boolean sendSmsByFeign(SendSmsParamBO dto) {
        SendSmsDTO sendSmsDTO = new SendSmsDTO();
        BeanUtils.copyProperties(dto, sendSmsDTO);
        try {
            String result = smsCenterFeignClient.sendMsg(sendSmsDTO);
            return parseResult(result);
        } catch (Exception e) {
            log.error("send sms error:" + "smsType=" + dto.getSystemType() + "mobile=" + dto.getMobileNum()
                    + "content=" + dto.getContent(), e);
            return false;
        }
    }

    private boolean sendInternationalMsgByFeign(SendSmsParamBO dto) {
        SendSmsDTO sendSmsDTO = new SendSmsDTO();
        BeanUtils.copyProperties(dto, sendSmsDTO);
        try {
            String result = smsCenterFeignClient.sendInternationalMsg(sendSmsDTO);
            return parseResult(result);
        } catch (Exception e) {
            log.error("send sms error:" + "smsType=" + dto.getSystemType() + "mobile=" + dto.getMobileNum()
                    + "content=" + dto.getContent(), e);
            return false;
        }
    }

    private boolean parseResult(String result) throws DocumentException {
        assert result != null;
        Document doc = DocumentHelper.parseText(result);
        Element rootElement = doc.getRootElement();
        String errorNo = rootElement.element("SendResult").element("ErrorNo").getText();
        if (!ERROR_NUM_0.equals(errorNo)) {
            if (ERROR_NUM_NEGATIVE_107.equals(errorNo)) {
                return true;
            }
            return false;
        }
        return true;
    }

}
