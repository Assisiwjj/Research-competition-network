package jingsai.demo.backstage.web;

import jingsai.demo.backstage.service.HonorService;
import jingsai.demo.team.dao.TeamMapper;
import jingsai.demo.team.domain.Team;
import jingsai.demo.team.service.TeamService;
import jingsai.demo.work.dao.WorkMapper;
import jingsai.demo.work.domain.Work;
import jingsai.demo.work.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/backstage")
public class BackstageController extends BaseController{
    @Autowired
    private WorkService workService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private HonorService honorService;

    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private TeamMapper teamMapper;

    //插上传作品信息
    @RequestMapping(value = "work/insert", method = RequestMethod.POST)
    public Map<String, Object> insertWork(HttpServletRequest request, @RequestBody Work work){
        msg.clear();
        try{
            String dataPrix = "";
            String data = "";
            String base64Data = work.getPicture();
            if(base64Data == null || "".equals(base64Data)){
                msg.put("code","0");
                msg.put("msg","上传失败，上传图片数据为空");
                return msg;
            }else{
                String [] d = base64Data.split("base64,");
                if(d != null && d.length == 2){
                    dataPrix = d[0];
                    data = d[1];
                    work.setPicture(null);
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


            if (work.getName()!=null && work.getType()!=null && work.getTeamName()!=null
                    && work.getMember()!=null){
                if (workService.insert(work,suffix,data)!=0L) {
                    msg.put("code", "1");
                    msg.put("msg", "成功");
                    msg.put("workId",work.getPkId());
                }else
                {
                    msg.put("code", "0");
                    msg.put("msg", "失败");
                }
            }else
            {
                msg.put("code","0");
                msg.put("msg","请传入完整参数");
            }
        }catch (Exception e){
            msg.put("code","0");
            msg.put("msg",e.getMessage());
        }
        return msg;
    }



    //获取作品封面图片
    @RequestMapping(value = "work/getImage/{workId}",method = RequestMethod.GET)
    public Map<String, Object> getWorkImage (@PathVariable("workId")Long workId) throws IOException {
        msg.clear();
        try{
            if(workId!=null &&workMapper.selectByPrimaryKey(workId)!=null){
                String data = workService.base64Data(workId);
                if(data!=null){
                    msg.put("code","1");
                    msg.put("msg","成功");
                    msg.put("data",data);
                }else{
                    msg.put("code","0");
                    msg.put("msg","失败");
                }
            }
        }catch (Exception e){
            throw new IOException(e.getMessage());
        }
        return msg;
    }


    //上传作品
    @RequestMapping(value = "work/upload/{workId}", method = RequestMethod.POST)
    public Map<String, Object> uploadWork(@PathVariable("workId")Long workId,@RequestParam("file") MultipartFile file,
                                          @RequestParam("filePath") String filePath){
        msg.clear();
        if (file.isEmpty()) {
            msg.put("code","0");
            msg.put("msg","请传入文件");
        }
        else {
            Calendar date = Calendar.getInstance();
            String year = String.valueOf(date.get(Calendar.YEAR));
            String uploadDir ="D:/upload1/word/" + year +"/"+filePath + "/" + workId + "/";
            File dir = new File(uploadDir);
            //如果文件夹不存在
            if(!dir.exists()){
                //创建文件夹
                dir.mkdirs();
            }
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadDir+ file.getOriginalFilename());
                String path1 = path.toString();
                Files.write(path, bytes);
                if (workService.updateFilePath(workId,path1)==1) {
                    msg.put("code", "1");
                    msg.put("msg", "上传成功");
                }else {
                    msg.put("code", "0");
                    msg.put("msg", "失败");
                }
            } catch (Exception e) {
                msg.put("code", "0");
                msg.put("msg", e.getMessage());
            }
        }
        return msg;
    }

    //删除作品
    @RequestMapping(value = "work/delete/{workId}",method = RequestMethod.DELETE)
    public Map<String, Object>deleteWork(HttpServletRequest request, @PathVariable("workId") Long workId){
        msg.clear();
        try{
            if (workId != null) {
                if (workService.workDelete(workId) == 1) {
                    msg.put("code", "1");
                    msg.put("msg", "成功");
                } else {
                    msg.put("code", "0");
                    msg.put("msg", "失败");
                }
            } else {
                msg.put("code", "0");
                msg.put("msg", "请输入作品ID");
            }
        }catch (Exception e){
            msg.put("code","0");
            msg.put("msg", e.getMessage());
        }
        return msg;
    }


    //附件上传
    @RequestMapping(value = "work/annex/{workId}", method = RequestMethod.POST)
    public Map<String, Object> uploadAnnex(@PathVariable("workId")Long workId,@RequestParam("filePath") String filePath,
                                           @RequestParam(required = false,value = "files") MultipartFile[] multipartFiles){
        msg.clear();
        if (multipartFiles.length>0&&multipartFiles!=null){
            for (MultipartFile multipartFile:multipartFiles){
                Calendar date = Calendar.getInstance();
                String year = String.valueOf(date.get(Calendar.YEAR));
                String uploadDir ="D:/upload1/word/" + year +"/"+filePath + "/" + workId + "/";
                File dir = new File(uploadDir);
                //如果文件夹不存在
                if(!dir.exists()){
                    //创建文件夹
                    dir.mkdirs();
                }
                try {
                    byte[] bytes = multipartFile.getBytes();
                    Path pathq = Paths.get(uploadDir + multipartFile.getOriginalFilename());
                    Files.write(pathq, bytes);
                    msg.put("code", "1");
                    msg.put("msg", "上传成功");
                } catch (Exception e) {
                    msg.put("code", "0");
                    msg.put("msg", e.getMessage());
                }
            }
        }else {
            msg.put("code","0");
            msg.put("msg","请传入文件");
        }
        return msg;
    }

    //插入团队信息
    @RequestMapping(value = "/team/insert", method = RequestMethod.POST)
    public Map<String, Object> insertTeam(HttpServletRequest request, @RequestBody Team team){
        msg.clear();
        try{
            String dataPrix = "";
            String data = "";
            String base64Data = team.getPicture();
            if(base64Data == null || "".equals(base64Data)){
                msg.put("code","0");
                msg.put("msg","上传失败，上传图片数据为空");
                return msg;
            }else{
                String [] d = base64Data.split("base64,");
                if(d != null && d.length == 2){
                    dataPrix = d[0];
                    data = d[1];
                    team.setPicture(null);
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

            if (team.getName()!=null && team.getManager()!=null && team.getmTelephone()!=null
                    && team.getTeacher()!=null){
                if (teamService.insert(team,suffix,data)==1){
                    msg.put("code", "1");
                    msg.put("msg", "成功");
                }else {
                    msg.put("code", "0");
                    msg.put("msg", "失败");
                    }
            }else {
                msg.put("code", "0");
                msg.put("msg", "请传输完整参数");
                }
        }catch (Exception e){
            msg.put("code","0");
            msg.put("msg",e.getMessage());
        }
        return msg;
    }


    //团队修改
    @RequestMapping(value = "team/update/{teamId}", method = RequestMethod.POST)
    public Map<String, Object> updateTeam(@PathVariable("teamId")Long teamId, @RequestBody Team team){
        msg.clear();
        try{
            String dataPrix = "";
            String data = "";
            String base64Data = team.getPicture();
            if(base64Data == null || "".equals(base64Data)){
                msg.put("code","0");
                msg.put("msg","上传失败，上传图片数据为空");
                return msg;
            }else{
                String [] d = base64Data.split("base64,");
                if(d != null && d.length == 2){
                    dataPrix = d[0];
                    data = d[1];
                    team.setPicture(null);
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

            if(team.getMember()!=null && team.getName()!=null && team.getManager()!=null
                    && team.getmTelephone() !=null){
                if (teamId!=null){
                    team.setPkId(teamId);
                    Team findTeam = teamService.updateTeam(team);
                    teamService.updateImage(teamId,suffix,data);
                    msg.put("code", "1");
                    msg.put("msg","成功");
                    msg.put("data", findTeam);
                } else {
                    msg.put("code", "0");
                    msg.put("msg", "失败");
                }
            }else {
                msg.put("code", "0");
                msg.put("msg", "参数不完整或者错误");
            }
        }catch (Exception e){
            msg.put("code","0");
            msg.put("msg",e.getMessage());
        }
        return msg;
    }

    //团队删除
    @RequestMapping(value = "team/delete/{teamId}", method = RequestMethod.DELETE)
    public Map<String, Object> deleteTeam(HttpServletRequest request,@PathVariable("teamId") Long teamId){
        msg.clear();
        try{
            if (teamId != null) {
                if (teamService.deleteTeam(teamId) == 1) {
                    msg.put("code", "1");
                    msg.put("msg", "成功");
                } else {
                    msg.put("code", "0");
                    msg.put("msg", "失败");
                }
            } else {
                msg.put("code", "0");
                msg.put("msg", "请输入团队ID");
            }
        }catch (Exception e){
            msg.put("code","0");
            msg.put("msg",e.getMessage());
        }
        return msg;
    }



    //获取团队封面图片
    @RequestMapping(value = "team/getImage/{teamId}",method = RequestMethod.GET)
    public Map<String, Object> getTeamImage (@PathVariable("teamId")Long teamId) throws IOException {
        msg.clear();
        try{
            String data = teamService.base64Data(teamId);
            if(data!=null){
                msg.put("code","1");
                msg.put("msg","成功");
                msg.put("data",data);
            }else{
                msg.put("code","0");
                msg.put("msg","失败");
            }
        }catch (Exception e){
            throw new IOException(e.getMessage());
        }

        return msg;
    }

    //荣誉删除
    @RequestMapping(value = "honor/delete/{honorId}", method = RequestMethod.DELETE)
    public Map<String, Object> deleteHonor(HttpServletRequest request,@PathVariable("honorId") Long honorId){
        msg.clear();
        try{
            if (honorId != null) {
                if (honorService.honorDelete(honorId) == 1) {
                    msg.put("code", "1");
                    msg.put("msg", "成功");
                } else {
                    msg.put("code", "0");
                    msg.put("msg", "失败");
                }
            } else {
                msg.put("code", "0");
                msg.put("msg", "请输入荣誉ID");
            }
        }catch (Exception e){
            msg.put("code","0");
            msg.put("msg",e.getMessage());
        }
        return msg;
    }



    //更新团队封面图片
//    @RequestMapping(value = "team/image/{teamId}",method = RequestMethod.POST)
//    public Map<String, Object> imageUploadT (@PathVariable("teamId")Long teamId,@RequestBody Team team){
//        msg.clear();
//        try {
//            String dataPrix = "";
//            String data = "";
//            String base64Data = team.getPicture();
//            if(base64Data == null || "".equals(base64Data)){
//                msg.put("code","0");
//                msg.put("msg","上传失败，上传图片数据为空");
//                return msg;
//            }else{
//                String [] d = base64Data.split("base64,");
//                if(d != null && d.length == 2){
//                    dataPrix = d[0];
//                    data = d[1];
//                    team.setPicture(null);
//                }else{
//                    msg.put("code","0");
//                    msg.put("msg","上传失败，数据不合法");
//                    return msg;
//                }
//            }
//
//            String suffix = "";
//            if("data:image/jpeg;".equalsIgnoreCase(dataPrix)){//data:image/jpeg;base64,base64编码的jpeg图片数据
//                suffix = ".jpg";
//            } else if("data:image/x-icon;".equalsIgnoreCase(dataPrix)){//data:image/x-icon;base64,base64编码的icon图片数据
//                suffix = ".ico";
//            } else if("data:image/gif;".equalsIgnoreCase(dataPrix)){//data:image/gif;base64,base64编码的gif图片数据
//                suffix = ".gif";
//            } else if("data:image/png;".equalsIgnoreCase(dataPrix)){//data:image/png;base64,base64编码的png图片数据
//                suffix = ".png";
//            }else{
//                msg.put("code","0");
//                msg.put("msg","上传图片格式不合法");
//                return msg;
//            }
//
//            if (teamMapper.selectByPrimaryKey(teamId)!=null){
//                if (team!=null){
//                    if(teamService.updateImage(teamId,suffix,data)>0){
//                        msg.put("code","1");
//                        msg.put("msg","上传成功");
//                    }else{
//                        msg.put("code","0");
//                        msg.put("msg","上传失败");
//                    }
//                }else{
//                    msg.put("code","0");
//                    msg.put("msg","上传失败,图片为空");
//                }
//            }else {
//                msg.put("code","0");
//                msg.put("msg","不存在该团队");
//            }
//        }catch (Exception e){
//            msg.put("code","0");
//            msg.put("msg",e.getMessage());
//        }
//        return msg;
//    }



    //更新作品封面图片
//    @RequestMapping(value = "work/image/{workId}",method = RequestMethod.POST)
//    public Map<String, Object> imageUploadW (@PathVariable("workId")Long workId,@RequestBody Work work){
//        msg.clear();
//        try {
//            String dataPrix = "";
//            String data = "";
//            String base64Data = work.getPicture();
//            if(base64Data == null || "".equals(base64Data)){
//                msg.put("code","0");
//                msg.put("msg","上传失败，上传图片数据为空");
//                return msg;
//            }else{
//                String [] d = base64Data.split("base64,");
//                if(d != null && d.length == 2){
//                    dataPrix = d[0];
//                    data = d[1];
//                    work.setPicture(null);
//                }else{
//                    msg.put("code","0");
//                    msg.put("msg","上传失败，数据不合法");
//                    return msg;
//                }
//            }
//
//            String suffix = "";
//            if("data:image/jpeg;".equalsIgnoreCase(dataPrix)){//data:image/jpeg;base64,base64编码的jpeg图片数据
//                suffix = ".jpg";
//            } else if("data:image/x-icon;".equalsIgnoreCase(dataPrix)){//data:image/x-icon;base64,base64编码的icon图片数据
//                suffix = ".ico";
//            } else if("data:image/gif;".equalsIgnoreCase(dataPrix)){//data:image/gif;base64,base64编码的gif图片数据
//                suffix = ".gif";
//            } else if("data:image/png;".equalsIgnoreCase(dataPrix)){//data:image/png;base64,base64编码的png图片数据
//                suffix = ".png";
//            }else{
//                msg.put("code","0");
//                msg.put("msg","上传图片格式不合法");
//                return msg;
//            }
//
//            if (workMapper.selectByPrimaryKey(workId)!=null){
//                if (work!=null){
//                    if(workService.updateImage(workId,suffix,data)>0){
//                        msg.put("code","1");
//                        msg.put("msg","上传成功");
//                    }else{
//                        msg.put("code","0");
//                        msg.put("msg","上传失败");
//                    }
//                }else{
//                    msg.put("code","0");
//                    msg.put("msg","上传失败,图片为空");
//                }
//            }else {
//                msg.put("code","0");
//                msg.put("msg","不存在该作品");
//            }
//        }catch (Exception e){
//            msg.put("code","0");
//            msg.put("msg",e.getMessage());
//        }
//        return msg;
//    }
}
