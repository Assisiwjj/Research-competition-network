package jingsai.demo.team.web;

import jingsai.demo.backstage.web.BaseController;
import jingsai.demo.team.dao.TeamMapper;
import jingsai.demo.team.domain.Team;
import jingsai.demo.team.service.TeamService;
import jingsai.demo.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/team")
public class TeamController extends BaseController {
    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamMapper teamMapper;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Map<String, Object> getAllTeam(HttpServletRequest request){
        msg.clear();
        try{
            Integer limit = Integer.parseInt(request.getParameter("limit"));
            Integer page = Integer.parseInt(request.getParameter("page"));
            PageBean pageBean =teamService.getPageBean(limit, page);
            msg.put("code","1");
            msg.put("msg","成功");
            msg.put("data",pageBean);
        }catch (Exception e){
            msg.put("code","0");
            msg.put("msg",e.getMessage());
        }
        return msg;
    }


    @RequestMapping(value = "/list/{teamId}",method = RequestMethod.GET)
    public Map<String, Object>getTeamByTeamId(@PathVariable("teamId") Long teamId){
        msg.clear();
        try{
            Team team = teamService.selectByTeamId(teamId);
            msg.put("code","1");
            msg.put("msg","成功");
            msg.put("data",team);
        }catch (Exception e){
            msg.put("code","0");
            msg.put("msg",e.getMessage());
        }
        return msg;
    }

    @RequestMapping(value = "image/upload",method = RequestMethod.POST)
    public Map<String, Object> staticImage (HttpServletRequest request,@RequestBody Map<String , Object> image){
        msg.clear();
        try {
            String dataPrix = "";
            String data = "";
            String base64Data = image.get("image").toString();
            if(base64Data == null || "".equals(base64Data)){
                msg.put("code","0");
                msg.put("msg","上传失败，上传图片数据为空");
                return msg;
            }else{
                String [] d = base64Data.split("base64,");
                if(d != null && d.length == 2){
                    dataPrix = d[0];
                    data = d[1];
                }else{
                    msg.put("code","0");
                    msg.put("msg","上传失败，数据不合法");
                    return msg;
                }
            }

            String suffix = "";
            if("data:image/jpeg;".equalsIgnoreCase(dataPrix)){//data:image/jpeg;base64,base64编码的jpeg图片数据
                suffix = ".jpg";
            } else if("data:image/x-icon;".equalsIgnoreCase(dataPrix)){//data:image/x-icon;base64,base64编码的icon图片数据
                suffix = ".ico";
            } else if("data:image/gif;".equalsIgnoreCase(dataPrix)){//data:image/gif;base64,base64编码的gif图片数据
                suffix = ".gif";
            } else if("data:image/png;".equalsIgnoreCase(dataPrix)){//data:image/png;base64,base64编码的png图片数据
                suffix = ".png";
            }else{
                msg.put("code","0");
                msg.put("msg","上传图片格式不合法");
                return msg;
            }

            if (image!=null){
                String fileName = teamService.staticImage(suffix,data);
                if (fileName!=null) {
                    msg.put("code", "1");
                    msg.put("msg", "上传成功");
                    //"localhost:8080/image/"
                    msg.put("url", "http://192.168.10.113:8080/image/" + fileName);
                } else {
                    msg.put("code", "0");
                    msg.put("msg", "上传失败");
                }
            }else {
                msg.put("code", "0");
                msg.put("msg", "上传失败,图片为空");
            }
        }catch (Exception e){
            msg.put("code","0");
            msg.put("msg",e.getMessage());
        }
        return msg;
    }
}
