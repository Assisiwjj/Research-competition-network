package jingsai.demo.backstage.web;

import jingsai.demo.backstage.domain.Administrator;
import jingsai.demo.backstage.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class AdministratorController extends BaseController {
    @Autowired
    private AdministratorService administratorService;

    //登陆
    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Object> login(@RequestBody Administrator administrator) {
        msg.clear();
        try {
            if (administrator.getName()!=null&&administrator.getPassword()!=null){
                if (administratorService.login(administrator)==true){
                    msg.put("code", "1");
                    msg.put("msg", "成功");
                }else {
                    msg.put("code", "0");
                    msg.put("msg", "用户名或密码不正确");
                }
            }else {
                msg.put("code", "0");
                msg.put("msg", "传参不正确");
            }
        } catch (Exception e) {
            msg.put("code", "0");
            msg.put("msg", e.getMessage());
        }
        return msg;
    }
}
