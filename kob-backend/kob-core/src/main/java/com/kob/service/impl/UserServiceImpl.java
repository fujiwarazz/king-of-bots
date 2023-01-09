package com.kob.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.annotation.Prevent;
import com.kob.controller.UserController;
import com.kob.mapper.UserMapper;
import com.kob.model.dto.UserLoginDto;
import com.kob.model.dto.UserRegDto;
import com.kob.model.entity.User;
import com.kob.model.vo.UserInfoVo;
import com.kob.service.IUserService;

import com.kob.util.*;
import org.joda.time.DateTime;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Wrapper;
import java.util.Base64;
import java.util.Locale;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author peelsannaw
 * @since 2023-01-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserMapper userMapper;
    /**
     * 记录登录信息
     * @param loginIdAsInt 用户id
     */
    @Override
    public void logLogin(int loginIdAsInt) {

    }

    @Override
//    @Prevent(value = "3")
    public ResponseResult<?> handleLogin(UserLoginDto userLoginDto, HttpServletRequest request) {
        try {
            if(StpUtil.isLogin()){
                StpUtil.logout();
            }
            String nickname = userLoginDto.getNickname();
            String loginDtoPassword = userLoginDto.getPassword();
            if(nickname == null || loginDtoPassword == null){
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
            }
            String encryptPassword =Base64Utils.encode((loginDtoPassword+Base64Utils.DEFAULT_SALT).getBytes(StandardCharsets.UTF_8));
            User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getNickname, nickname));
            if(user==null){
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST.getCode(),"无此用户!");
            }
            if(!user.getPassword().equals(encryptPassword)){
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }
            UserContextHolder.setUser(user);
            String userAgent = request.getHeader("user-agent").toLowerCase(Locale.ROOT);
            String type = StpUtil.getLoginType();
            StpUtil.login(user.getId(),type);
            System.out.println(StpUtil.getLoginType());
            UserInfoVo userInfoVo = new UserInfoVo();
            userInfoVo.setUserId(user.getId());

            return ResponseResult.okResult(StpUtil.getTokenValue());
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
        }
    }

    @Override
    @Prevent(value = "30")
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<?> handleRegister(UserRegDto userRegDto) {
        String nickname = userRegDto.getUsername();
        String password = userRegDto.getPassword();
        String email = userRegDto.getEmail();
        String avatar = userRegDto.getAvatar();
        if(nickname==null || password==null || StrUtil.isBlank(nickname) || StrUtil.isBlank(password)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        User user = new User();
        user.setNickname(nickname);
        if(StrUtil.isNotBlank(avatar)){
            user.setAvatar(avatar);
        }
        String encryptPassword = Base64Utils.encode((password + Base64Utils.DEFAULT_SALT).getBytes(StandardCharsets.UTF_8));
        user.setPassword(encryptPassword);
        if(userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getNickname,nickname))!=null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID.getCode(),"此用户已存在!");
        }else{
            this.save(user);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> getInfoByToken() {
        long loginIdAsLong = StpUtil.getLoginIdAsLong();
        User loginUser = this.getById(loginIdAsLong);
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtil.copyProperties(loginUser,userInfoVo);
        userInfoVo.setUserId(loginUser.getId());
        userInfoVo.setAvatar(loginUser.getAvatar());
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult<?> uploadUserAvatar(MultipartFile file) {
        // 工具类获取值
        String endpoint = AliyunProperties.END_POINT;
        String accessKeyId = AliyunProperties.KEY_ID;
        String accessKeySecret = AliyunProperties.KEY_SECRET;
        String bucketName = AliyunProperties.BUCKET_NAME;

        try {
            // 创建OSS实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            //获取上传文件输入流
            InputStream inputStream = file.getInputStream();
            //获取文件名称
            String fileName = file.getOriginalFilename();

            //1 在文件名称里面添加随机唯一的值
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            // yuy76t5rew01.jpg
            fileName = uuid+fileName;

            //2 把文件按照日期进行分类
            //获取当前日期
            String datePath = new DateTime().toString("yyyy/MM/dd");
            //拼接
            fileName = datePath+"/"+fileName;

            //调用oss方法实现上传
            //第一个参数  Bucket名称
            //第二个参数  上传到oss文件路径和文件名称   aa/bb/1.jpg
            //第三个参数  上传文件输入流
            ossClient.putObject(bucketName,fileName , inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            String url = "https://"+bucketName+"."+endpoint+"/"+fileName;
            return  ResponseResult.okResult(url);
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
