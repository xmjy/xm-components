# common组件

## 更新日志
20220222更新
1.个人信息加密工具类开发测试环境读key和iv值

20220218更新
1.编码插件扫描修改

注：MD5Util不符合驼峰命名规则，类名和方法名修改了，引用到的项目需要修改下



## 工具介绍

注：启动类加上@SpringBootApplication(scanBasePackages = {"com.job51.ehire"})

### 1.邮件工具类

添加配置

```
spring:
  #邮件配置
  mail:
    host: 10.100.1.10
    port: 25
    ehire:
      # 邮件发送人
      mailFrom: services@51job.com
      # 回馈邮件地址
      returnPath: services@51job.com
      # 邮件输出地址
      emlFilePath: /www/service/web/filedoc/mail/
      # 邮件发送备份路径
      backupFilePath: /www/service/web/filedoc/mailbackup/
      # 告警邮件收件人
      warningMailTo: xxx@51job.com
      # 发送email的业务类型
      emailFlagTypes: system,cyclone,wyhour,bgservey,other,interviewer,sharehr,bigdata,unreadedm,smartinvitation,directional,facetofaceinterview,qrlogin,export,transmit,feedback,evaluation,product,salary,recommend,warning,notice,custom,exchangevip,qualificationapply
      # 发送email的优先级
      emailPriorities: 1,2,3,4,5
```

调用示例

```
    @Resource
    private MailUtil mailUtil;
```



```
    public void testSendEmail(){
        SendEmailParamsBO sendEmailParamsBO = SendEmailParamsBO.builder()
                .emailFrom("xxx@51job.com")
                .emailTo("albert.fang@51job.com")
                .emailCopyTo("")
                .emailReplyTo("")
                .attachments(null)
                .title("testSendEmail")
                .body("hello 你好")
                .isHtml(false)
                .emailFlagType("system")
                .emailPriority("1")
                .build();
        mailUtil.sendMail(sendEmailParamsBO);
    }
```

### 2.短信工具类

添加配置

```
ehire:
  #短信配置
  sms:
    url:
      #短信中心接口域名
      domain: http://10.100.2.184
    #被禁用的手机号码段，逗号分隔
    banMobiles:
```

调用示例

```
    @Resource
    private SmsUtil smsUtil;
```



```
    public void testSendMsg(){
        boolean ret = smsUtil.sendMsg("37", "18083015043", "testSendSms", "0");
        log.info(String.valueOf(ret));
    }
```



### 3.个人信息加密工具类

添加配置

```
ehire:
  #数据源匹配值
  datasource:
    encrypt:
      enable: true
      key: jScjb0fOf2ZsV00ljfmVWS9O2Hp8YRmS
      iv: FKTNr9uFfqyfe8Lg
      file-path: /www/common/config/aes256.key
```

开发测试环境配置：key和iv

生产环境配置：file-path

调用示例

```
    @Resource
    private EncryptInfoUtil encryptInfoUtil;
```

```
    @Test
    void testEncryptInfoUtil() {
        log.info("testEncryptInfoUtil");
        String content = "中文english1234";
        log.info("原始字符串：" + content);
        String encryptContent = "";
        try{
            encryptContent = encryptInfoUtil.encrypt(content);
        }catch (Exception e){
            log.error("aes 加密异常：", e);
        }
        log.info("加密后的字符串：" + encryptContent);
        String decryptContent = "";
        try{
            decryptContent = encryptInfoUtil.decrypt(encryptContent);
        }catch (Exception e){
            log.error("aes 解密异常：", e);
        }
        log.info("解密后的字符串：" + decryptContent);
    }
```



### 4.ID加密工具类

调用示例

```
    @Test
    public void testEncryptId(){
        log.info("testEncryptId");
        String encryptNum = EncryptIdUtil.encryptNum("123", "111");
        log.info("encryptNum: " + encryptNum);
        String decryptNum = EncryptIdUtil.decryptNum(encryptNum, "111");
        log.info("decryptNum: " + decryptNum);
    }
```

