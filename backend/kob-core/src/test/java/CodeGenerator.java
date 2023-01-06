import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

/**
 * 逆向工程
 */
public class CodeGenerator {
    public static void main(String[] args) {
        generate();
    }

    private static void generate() {
        FastAutoGenerator.create("jdbc:mysql://rm-bp10xwcd92e56fsfsmo.mysql.rds.aliyuncs.com:3306/kob?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8",
                        "peelsannaw", "Zzhzzhzzh1")
                .globalConfig(builder -> {
                    builder.author("peelsannaw") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\courseDesign\\hmtt-workspace\\kob\\backend\\kob-core\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {

                    builder.parent("com.kob") // 设置父包名
                            .moduleName(null) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D:\\courseDesign\\hmtt-workspace\\kob\\backend\\kob-core\\src\\main\\resources\\mapper\\")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok();
//                    builder.controllerBuilder().enableHyphenStyle()  // 开启驼峰转连字符
//                            .enableRestStyle();  // 开启生成@RestController 控制器
                    builder.addInclude("k_user") // 设置需要生成的表名
                            .addTablePrefix("k_", "sys_"); // 设置过滤表前缀
                })
                .execute();
    }
}
