package jingsai.demo.team.service.impl;

import jingsai.demo.team.dao.TeamIntroductionMapper;
import jingsai.demo.team.dao.TeamMapper;
import jingsai.demo.team.domain.Team;
import jingsai.demo.team.domain.TeamExample;
import jingsai.demo.team.domain.TeamIntroduction;
import jingsai.demo.team.service.TeamIntroductionService;
import jingsai.demo.team.service.TeamService;
import jingsai.demo.utils.*;
import jingsai.demo.work.dao.WorkMapper;
import jingsai.demo.work.domain.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TeamImpl implements TeamService {
    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private TeamIntroductionMapper teamIntroductionMapper;

    @Autowired
    private TeamIntroductionService teamIntroductionService;


    @Override
    public int insert(Team team, String suffix, String data) throws RuntimeException{
        try {
            team.setGmtCreate(new Date());
            team.setGmtModified(new Date());
            team.setIsDelete(false);
            team.setPicture(uploadImage(suffix, null, data, true));
            teamMapper.insert(team);
            teamIntroductionService.insert();
            return 1;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String uploadImage(String suffix, String FileName, String data, Boolean insert) throws RuntimeException {
        String fileName;
        try
        {
            //Base64解码
            byte[] b = Base64Utils.decodeFromString(data);
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {
                    //调整异常数据
                    b[i]+=256;
                }
            }
            if(!insert){
                fileName = FileName;
                File oldFile = new File(ConstantsT.getCurrenPath()+fileName);
                if(oldFile.exists()){
                    oldFile.delete();
                }
                fileName =  FileName.split("\\.")[0] + suffix;
            }else{
                fileName = UUID.randomUUID().toString() + suffix;
            }
            String imgFilePath = ConstantsT.getCurrenPath()+fileName;
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return fileName;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public int updateImage(Long teamId, String suffix,String data) {
        Team team = teamMapper.selectByPrimaryKey(teamId);
        team.setPicture(uploadImage(suffix, team.getPicture(), data, false));
        return teamMapper.updateByPrimaryKey(team);
    }


    @Override
    public String base64Data(Long teamId){
        Team team = teamMapper.selectByPrimaryKey(teamId);
        if(team.getPicture()==null){
            return null;
        }
        InputStream inputStream = null;
        byte[] data = null;
        String imgFile = ConstantsT.getCurrenPath() + team.getPicture();
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        String Data = "data:image/png;base64," + encoder.encode(data);
        return Data;
    }

    @Override
    public PageBean getPageBean(Integer limit, Integer page) {
        try {
            TeamExample example = new TeamExample();
            TeamExample.Criteria criteria = example.createCriteria();
            criteria.andIsDeleteEqualTo(false);
            int count = (int) teamMapper.countByExample(example);
            if (count > 0) {
                PageBean pageBean = new PageBean(page, count, limit);
                example.setLimit(limit);
                example.setOffset(pageBean.getStart());
                List<Team> teamList = teamMapper.selectByExample(example);
                for(Team team : teamList){
                    if(team.getPicture()!=null){
                        team.setPicture((Base64.toBase64T(team.getPicture())));
                    }
                }
                pageBean.setList(teamList);
                return pageBean;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Team selectByTeamId(Long teamId) throws RuntimeException {
        try {
            Team team = teamMapper.selectByTeamId(teamId);
            if (team != null) {
                if (team.getIsDelete()==true) {
                    return null;
                } else {
                    team.setPicture((Base64.toBase64T(team.getPicture())));
                    return team;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public int deleteTeam(Long teamId) throws RuntimeException {
        try {
            if (teamId != null) {
                if (teamMapper.selectByPrimaryKey(teamId) != null) {
                    Team team = new Team();
                    team.setPkId(teamId);
                    team.setIsDelete(true);
                    return teamMapper.updateByPrimaryKeySelective(team);
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Team updateTeam(Team team) throws RuntimeException {
        try {
            if(teamMapper.updateByPrimaryKeySelective(team)>0){
                teamMapper.updateByPrimaryKeySelective(team);
                TeamIntroduction teamIntroduction = new TeamIntroduction();
                teamIntroduction.setContext(team.getTeamIntroduction().getContext());
                teamIntroduction.setPkId(team.getPkId());
                teamIntroductionMapper.updateByPrimaryKeySelective(teamIntroduction);
                return teamMapper.selectByPrimaryKey(team.getPkId());
            }else {
                throw new RuntimeException("更新失败");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String staticImage(String suffix, String data) throws RuntimeException {
        String fileName;
        try
        {
            //Base64解码
            byte[] b = Base64Utils.decodeFromString(data);
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {
                    //调整异常数据
                    b[i]+=256;
                }
            }

            fileName = UUID.randomUUID().toString() + suffix;
            String imgFilePath = ConstantsS.getCurrenPath()+fileName;
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return fileName;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
