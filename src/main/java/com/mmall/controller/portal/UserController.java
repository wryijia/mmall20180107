package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /***
     *   登陆
     * @param username 用户名
     * @param password 密码
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password,HttpSession session, HttpServletResponse httpServletResponse){

        ServerResponse<User> userServerResponse = iUserService.login(username,password);
        if(userServerResponse.isSuccess()){

           //session.setAttribute(Const.CURRENT_USER,userServerResponse.getData());
            CookieUtil.writeLoginToken(httpServletResponse,session.getId());

            RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.obj2String(userServerResponse.getData()),Const.RedisCacheExTime.REDIS_SESSION_EXTIME);


        }

        return userServerResponse;

    }


    /**
     * 登出
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "logout.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpServletRequest request,HttpServletResponse response){

        String loginToken = CookieUtil.readLoginToken(request);
        CookieUtil.deleteLoginToken(request,response);
        RedisShardedPoolUtil.delete(loginToken);

//        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccessMessage(" 登出成功");

    }

    /**
     *  注册
     * @param user
     * @return
     */
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){

      return iUserService.register(user);

    }


    /**
     *  获取用户信息
     * @param session
     * @return
     */
    @RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getUserInfo(HttpServletRequest request){


        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }

        String userStr = RedisShardedPoolUtil.get(loginToken);

        User user = JsonUtil.str2Obj(userStr,User.class);

        if(user==null){

            return ServerResponse.createByErrorMessage("用户尚未登录");
        }

        return ServerResponse.createBySuccessData(user);
    }

    /**
     *  忘记密码的情况，根据用户名获取问题
     * @param username
     * @return
     */
    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
    @ResponseBody
    public  ServerResponse<String> forgetGetQuestion(String username){

          return iUserService.forgetGetQuestion(username);

    }

    /**
     *  验证问题答案
     * @param username
     * @param question
     * @param answer
     * @return
     */

    public ServerResponse<String> forgetGetAnswer(String username,String question,String answer){

             return iUserService.checkAnswer(username,question,answer);

    }

    /**
     * 忘记密码情况下，重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public  ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){

        return iUserService.forgetResetPassword(username,passwordNew,forgetToken);

    }


    /**
     *  已登录状态下，更新密码
     * @param session
     * @param passwordOld 旧密码
     * @param passwordNew 新密码
     * @return
     */
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public  ServerResponse<String> resetPassword(HttpServletRequest request,String passwordOld,String passwordNew){

        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }

        String userStr = RedisShardedPoolUtil.get(loginToken);

        User user = JsonUtil.str2Obj(userStr,User.class);

        if(user==null){
           return ServerResponse.createByErrorMessage("用户未登陆");
        }

        return iUserService.resetPassword(user,passwordOld,passwordNew);

    }

    /**
     *  更新用户信息
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value = "update_info.do",method = RequestMethod.POST)
    @ResponseBody
    public  ServerResponse<User> updatePersonalInfo(HttpSession session,HttpServletRequest request,User user){

        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }

        String userStr = RedisShardedPoolUtil.get(loginToken);

        User userInRedis = JsonUtil.str2Obj(userStr,User.class);
        if(userInRedis==null){
            return ServerResponse.createByErrorMessage("用户未登陆");
        }

        user.setId(userInRedis.getId());
        user.setUsername(userInRedis.getUsername());

        ServerResponse<User> userServerResponse = iUserService.updatePersonalInformation(user);
        if(userServerResponse.isSuccess()){
            userServerResponse.getData().setUsername(userInRedis.getUsername());
            session.setAttribute(Const.CURRENT_USER,userServerResponse.getData());
        }

        return userServerResponse;
    }

























}

