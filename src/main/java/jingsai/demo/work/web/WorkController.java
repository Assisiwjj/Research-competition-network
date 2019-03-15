package jingsai.demo.work.web;

import jingsai.demo.backstage.web.BaseController;
import jingsai.demo.utils.FileScan;
import jingsai.demo.utils.PageBean;
import jingsai.demo.work.dao.WorkMapper;
import jingsai.demo.work.domain.Work;
import jingsai.demo.work.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/work")
public class WorkController extends BaseController {
    @Autowired
    private WorkService workService;

    @Autowired
    private WorkMapper workMapper;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> getAllWork(HttpServletRequest request){
        msg.clear();
        try{
            Integer limit = Integer.parseInt(request.getParameter("limit"));
            Integer page = Integer.parseInt(request.getParameter("page"));
            String name = request.getParameter("name");
            String type = request.getParameter("type");
            String teamName = request.getParameter("teamName");
            PageBean pageBean =workService.getPageBean(limit, page, type, name, teamName);
            msg.put("code","1");
            msg.put("msg","成功");
            msg.put("data",pageBean);
        }
        catch (Exception e){
            msg.put("code","0");
            msg.put("msg", e.getMessage());
        }
        return msg;
    }

    @RequestMapping(value = "/list/{workId}", method = RequestMethod.GET)
    public Map<String, Object>getWorkByWorkId(@PathVariable("workId") Long workId){
        msg.clear();
        try{
            Work work = workService.selectByWorkId(workId);
            msg.put("code","1");
            msg.put("msg","成功");
            msg.put("data",work);
        }catch (Exception e){
            msg.put("code","0");
            msg.put("msg", e.getMessage());
        }
        return msg;
    }

    @RequestMapping(value = "/list/hot", method = RequestMethod.GET)
    public Map<String, Object>hotWork(){
        msg.clear();
        try{
            List<Work> list= workService.selectByHits();
            msg.put("code","1");
            msg.put("msg","成功");
            msg.put("data",list);
        }catch (Exception e){
            msg.put("code","0");
            msg.put("msg", e.getMessage());
        }
        return msg;
    }

    @RequestMapping(value = "/list/keyan", method = RequestMethod.GET)
    public Map<String, Object>keyanWork(){
        msg.clear();
        try{
            List<Work> list= workService.selectBykeyan();
            msg.put("code","1");
            msg.put("msg","成功");
            msg.put("data",list);
        }catch (Exception e){
            msg.put("code","0");
            msg.put("msg", e.getMessage());
        }
        return msg;
    }

    @RequestMapping(value = "/list/jingsai", method = RequestMethod.GET)
    public Map<String, Object>jingsaiWork(){
        msg.clear();
        try{
            List<Work> list= workService.selectByjingsai();
            msg.put("code","1");
            msg.put("msg","成功");
            msg.put("data",list);
        }catch (Exception e){
            msg.put("code","0");
            msg.put("msg", e.getMessage());
        }
        return msg;
    }

    @RequestMapping(value = "/annex", method = RequestMethod.GET)
    public Map<String, Object>getAnnexList(@RequestParam("path") String path){
        msg.clear();
        try{
            if (path!=null){
            List<String> list= FileScan.getList(path);
                msg.put("code","1");
                msg.put("msg","成功");
                msg.put("data",list);
            }else {
                msg.put("code","0");
                msg.put("msg","失败");
            }
        }catch (Exception e){
            msg.put("code","0");
            msg.put("msg", e.getMessage());
        }
        return msg;
    }
}
