package com.kob.config.stp;

import cn.dev33.satoken.strategy.SaStrategy;
import cn.dev33.satoken.util.SaFoxUtil;
import com.kob.util.Base64Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

/**
 * @Author peelsannaw
 * @create 08/01/2023 12:50
 */
@Configuration
public class SaTokenConfigure {
    @Autowired
    public void rewriteSaStrategy() {
        // 重写 Token 生成策略

        SaStrategy.me.createToken = (loginId, loginType) -> {
            String encode = Base64Utils.encode(loginId.toString().getBytes(StandardCharsets.UTF_8));
            return SaFoxUtil.getRandomString(50)+encode;    // 随机60位长度字符串
        };
    }
}
