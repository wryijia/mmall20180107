package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/manage/product/")
public class ProductManageController {

    @Autowired
    private IProductService iProductService;
    @Autowired
    private ICategoryService iCategoryService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IFileService iFileService;

    /**
     * 产品列表
     * @param request
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "list.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> getProductList(HttpServletRequest request, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){

//        String loginToken = CookieUtil.readLoginToken(request);
//        if(StringUtils.isEmpty(loginToken)){
//            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
//        }
//
//        String userStr = RedisShardedPoolUtil.get(loginToken);
//
//        User user = JsonUtil.str2Obj(userStr,User.class);
//        if(user==null){
//            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());
//        }
//        if(!iUserService.checkAdminRole(user).isSuccess()){
//            return ServerResponse.createByErrorMessage("非管理员权限");
//        }

        return iProductService.getProductList(pageNum,pageSize);

    }

    /**
     * 搜索产品
     * @param request
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "search.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse searchProduct(HttpServletRequest request, String productName, Integer productId, @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
//
//        String loginToken = CookieUtil.readLoginToken(request);
//        if(StringUtils.isEmpty(loginToken)){
//            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
//        }
//
//        String userStr = RedisShardedPoolUtil.get(loginToken);
//
//        User user = JsonUtil.str2Obj(userStr,User.class);
//        if(user==null){
//            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());
//        }
//        if(iUserService.checkAdminRole(user).isSuccess()){
//
//            return iProductService.searchProduct(productName,productId,pageNum,pageSize);
//
//        }else {
//            return ServerResponse.createByErrorMessage("非管理员权限");
//        }
        return iProductService.searchProduct(productName,productId,pageNum,pageSize);
    }

    /**
     *
     * 产品详情
     * @param request
     * @param productId
     * @return
     */
    @RequestMapping(value = "detail.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productDetail(HttpServletRequest request, Integer productId){
//        String loginToken = CookieUtil.readLoginToken(request);
//        if(StringUtils.isEmpty(loginToken)){
//            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
//        }
//
//        String userStr = RedisShardedPoolUtil.get(loginToken);
//
//        User user = JsonUtil.str2Obj(userStr,User.class);
//        if(user==null){
//            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());
//        }
//        if(iUserService.checkAdminRole(user).isSuccess()){
//
//            return iProductService.getProductDetail(productId);
//
//        }else {
//            return ServerResponse.createByErrorMessage("非管理员权限");
//        }
        return iProductService.getProductDetail(productId);
    }


    /**
     * 上传文件
     * @param request
     * @param file
     * @return
     */
    @RequestMapping(value = "upload.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(HttpServletRequest request, MultipartFile file){
//
        String loginToken = CookieUtil.readLoginToken(request);
//        if(StringUtils.isEmpty(loginToken)){
//            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
//        }
//
//        String userStr = RedisShardedPoolUtil.get(loginToken);
//
//        User user = JsonUtil.str2Obj(userStr,User.class);
//        if(user==null){
//            return ServerResponse.createByErrorCodeAndMessage(Const.ResponseCode.NEED_LOGIN.getCode(),Const.ResponseCode.NEED_LOGIN.getDesc());
//        }
//        if(iUserService.checkAdminRole(user).isSuccess()){
//
//            String path = request.getSession().getServletContext().getRealPath("upload");
//            String targetFileName = iFileService.upload(file,path);
//
//            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+"image/"+targetFileName;
//
//            Map fileMap = Maps.newHashMap();
//            fileMap.put("uri",targetFileName);
//            fileMap.put("url",url);
//
//            return ServerResponse.createBySuccessData(fileMap);
//
//
//        }else {
//            return ServerResponse.createByErrorMessage("非管理员权限");
//        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file,path);

        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+"image/"+targetFileName;

        Map fileMap = Maps.newHashMap();
        fileMap.put("uri",targetFileName);
        fileMap.put("url",url);

        return ServerResponse.createBySuccessData(fileMap);
    }


    /**
     *  富文本上传
     * @param file
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "richtext_img_upload.do",method = RequestMethod.POST)
    @ResponseBody
    public Map richtextImgUpload(HttpSession session,  @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){

        Map resultMap = Maps.newHashMap();
//
//
//        String loginToken = CookieUtil.readLoginToken(request);
//
//        if(StringUtils.isEmpty(loginToken)){
//            resultMap.put("success",false);
//            resultMap.put("msg","用户未登录，获取不到当前用户信息");
//        }
//
//        String userStr = RedisShardedPoolUtil.get(loginToken);
//
//        User user = JsonUtil.str2Obj(userStr,User.class);
//
//        if(user==null){
//            resultMap.put("success",false);
//            resultMap.put("msg","请联系管理员");
//
//        }
//
//        if(iUserService.checkAdminRole(user).isSuccess()){
//            String path = request.getSession().getServletContext().getRealPath("upload");
//            String targetName = iFileService.upload(file,path);
//
//            if(StringUtils.isBlank(targetName)){
//                resultMap.put("success",false);
//                resultMap.put("msg","上传富文本失败");
//                return resultMap;
//            }
//
//            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetName;
//            resultMap.put("success",true);
//            resultMap.put("msg","上传富文本成功");
//            resultMap.put("file_path",url);
//            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
//            return resultMap;
//        }else {
//            resultMap.put("success",false);
//            resultMap.put("msg","无权限操作");
//            return resultMap;
//        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetName = iFileService.upload(file,path);

        if(StringUtils.isBlank(targetName)){
            resultMap.put("success",false);
            resultMap.put("msg","上传富文本失败");
            return resultMap;
        }

        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetName;
        resultMap.put("success",true);
        resultMap.put("msg","上传富文本成功");
        resultMap.put("file_path",url);
        response.addHeader("Access-Control-Allow-Headers","X-File-Name");
        return resultMap;
    }

































}
