package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.reggie_take_out.common.R;
import com.example.reggie_take_out.entity.User;
import com.example.reggie_take_out.service.UserService;
import com.example.reggie_take_out.utils.SMSUtils;
import com.example.reggie_take_out.utils.ValidateCodeUtils;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 發送手機短信驗證碼
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        // 獲取手機號
        String phone = user.getPhone();

        if(StringUtils.isNotEmpty(phone)) {
            // 生成隨機的4位驗證碼
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code:{}", code);

            // 調用阿里雲提供的短信服務API完成發送短信
//            SMSUtils.sendMessage("瑞吉外賣","", phone, code);

            // 需要將生成的驗證碼保存到Session
            session.setAttribute("phone", code);

            return R.success("手機驗證碼短信發送成功");
        }

        return R.error("短信發送失敗");
    }

    /**
     * 移動端用戶登錄
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());

        // 獲取手機號
        String phone = map.get("phone").toString();

        // 獲取驗證碼
        String code = map.get("code").toString();

        // 從Session中獲取保存的驗證碼
        Object codeInSession = session.getAttribute("phone");

        if(codeInSession!=null && codeInSession.equals(code)) {
            // 如果能夠比對成功，説明登錄成功

            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);

            User user = userService.getOne(queryWrapper);
            if(user == null) {
                // 判斷當前手機號對應的用戶是否爲新用戶，如果是新用戶就自動完成注冊
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            return R.success(user);
        }

        return R.error("登錄失敗");
    }
}
