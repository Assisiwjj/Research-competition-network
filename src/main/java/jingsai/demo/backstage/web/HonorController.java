package jingsai.demo.backstage.web;

import jingsai.demo.backstage.dao.HonorMapper;
import jingsai.demo.backstage.domain.Honor;
import jingsai.demo.backstage.service.HonorService;
import jingsai.demo.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/honor")
public class HonorController extends BaseController{
    @Autowired
    private HonorService honorService;

    @Autowired
    private HonorMapper honorMapper;

    @RequestMapping( method = RequestMethod.GET)
    public Map<String, Object> getAllHonor(HttpServletRequest request) {
        msg.clear();
        try{
            Integer limit = Integer.parseInt(request.getParameter("limit"));
            Integer page = Integer.parseInt(request.getParameter("page"));
            PageBean pageBean = honorService.getPageBean(limit, page);
            msg.put("code", "1");
            msg.put("msg","成功");
            msg.put("data", pageBean);
        }catch (Exception e){
            msg.put("code","0");
            msg.put("msg",e.getMessage());
        }
        return msg;
    }

    @RequestMapping(value = "/{pkId}",method = RequestMethod.GET)
    public Map<String, Object>getHonorByPkId(@PathVariable("pkId") Long pkId){
        msg.clear();
        try{
            Honor honor = honorService.selectById(pkId);
            msg.put("code","1");
            msg.put("msg","成功");
            msg.put("data",honor);
        }catch (Exception e){
            msg.put("code","0");
            msg.put("msg",e.getMessage());
        }
        return msg;
    }

    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public Map<String, Object>insertHonor(@RequestBody Honor honor){
        msg.clear();
        try{
            if (honor.getTitle()!=null&&honor.getSubtitle()!=null&&honor.getProfile()!=null){
                if (honorService.insert(honor)!=0L){
                    msg.put("code","1");
                    msg.put("msg","成功");
                    msg.put("pkId",honor.getPkId());
                }else {
                    msg.put("code","0");
                    msg.put("msg","失败");
                }
            }else {
                msg.put("code","0");
                msg.put("msg","传参不正确");
            }
        }catch (Exception e){
            msg.put("code","0");
            msg.put("msg",e.getMessage());
        }
        return msg;
    }

    @RequestMapping( value = "/pkId",method = RequestMethod.GET)
    public Map<String, Object> getAllPkId() {
        msg.clear();
        try{
            List<Map> list = honorMapper.selectAllPkId();
            msg.put("code", "1");
            msg.put("msg","成功");
            msg.put("data", list);
        }catch (Exception e){
            msg.put("code","0");
            msg.put("msg",e.getMessage());
        }
        return msg;
    }

}
