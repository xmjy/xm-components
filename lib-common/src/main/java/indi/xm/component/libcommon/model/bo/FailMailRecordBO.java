package indi.xm.component.libcommon.model.bo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: albert.fang
 * @date: 2019-12-20
 * @description:
 */
@Data
public class FailMailRecordBO {
    /**
     * 出错的路径
     */
    private String failPath;
    /**
     * 出错的次数
     */
    private Integer failCount;
    /**
     * 是否已经发送告警邮件
     */
    private Boolean sendMailFlag;
    /**
     * 最后一次出错时间
     */
    private LocalDateTime lastFailTime;
    /**
     * 第一次出错时间
     */
    private LocalDateTime firstFailTime;
}
