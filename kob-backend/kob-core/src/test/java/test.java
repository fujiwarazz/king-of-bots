import com.kob.KobApplication;
import com.kob.mapper.BotMapper;
import com.kob.mapper.UserMapper;
import com.kob.model.entity.Bot;
import com.kob.model.entity.User;
import com.kob.service.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.annotation.security.RunAs;

/**
 * @Author peelsannaw
 * @create 06/01/2023 21:13
 */
@SpringBootTest(classes = KobApplication.class)
@RunWith(SpringRunner.class)
public class test {
    @Autowired
    private IUserService userMapper;
    @Resource
    private BotMapper botMapper;
    @Test
    public void test(){
//        Bot bot = new Bot();
//        bot.setBTitle("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//        botMapper.insert(bot);
    }
}
