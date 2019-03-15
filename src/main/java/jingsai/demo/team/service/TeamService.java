package jingsai.demo.team.service;

import jingsai.demo.team.domain.Team;
import jingsai.demo.utils.PageBean;

public interface TeamService {
    int insert(Team team, String suffix, String data);

    String uploadImage(String suffix, String FileName, String data, Boolean insert);

    int updateImage(Long teamId, String suffix,String data);

    String base64Data(Long teamId);

    PageBean getPageBean(Integer limit, Integer page);

    Team selectByTeamId(Long teamId);

    Team updateTeam(Team team);

    int deleteTeam(Long teamId);

    String staticImage(String suffix,  String data);
}
