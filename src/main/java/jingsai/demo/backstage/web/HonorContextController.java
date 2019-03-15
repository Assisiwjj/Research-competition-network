package jingsai.demo.backstage.web;

import jingsai.demo.backstage.domain.HonorContext;
import jingsai.demo.backstage.service.HonorContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/honorContext")
public class HonorContextController extends BaseController {
    @Autowired
    private HonorContextService honorContextService;

    @RequestMapping(value = "/{honorId}",method = RequestMethod.GET)
    public Map<String, Object> getHonorContext(@PathVariable("honorId") Long honorId){
        msg.clear();
        try{
            if (honorId!=null){
                List<HonorContext> list=honorContextService.selectByHonorId(honorId);
                msg.put("code","1");
                msg.put("msg","成功");
                msg.put("data",list);
            }else{
                msg.put("code","0");
                msg.put("msg","传参不正确");
            }
        }catch (Exception e){
            msg.put("code","0");
            msg.put("msg",e.getMessage());
        }
        return msg;
    }

    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public Map<String, Object> insertHonor(@RequestBody HonorContext honorContext){
        msg.clear();
        try{
            if (honorContext.getAward()!=null&&honorContext.getHonorId()!=null&&honorContext.getSchoolClass()!=null
                    &&honorContext.getName()!=null&&honorContext.getTeacher()!=null&&honorContext.getType()!=null){
                if (honorContextService.insert(honorContext)==1){
                    msg.put("code","1");
                    msg.put("msg","成功");
                }else{
                    msg.put("code","0");
                    msg.put("msg","失败");
                }
            }else{
                msg.put("code","0");
                msg.put("msg","传参不正确");
            }
        }catch (Exception e){
            msg.put("code","0");
            msg.put("msg",e.getMessage());
        }
        return msg;
    }
}
