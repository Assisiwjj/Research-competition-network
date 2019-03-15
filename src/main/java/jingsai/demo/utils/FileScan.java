package jingsai.demo.utils;

import java.io.File;
import java.util.ArrayList;


public class FileScan {
    private static ArrayList<String> list = new ArrayList<String>();

    public static ArrayList<String> getList(String path)throws Exception{
        list.clear();
        path="D://upload1" + path;
        readAllFile(path);
        System.out.println(list);
        return list;
    }
    public static void readAllFile(String filepath) {
        File file= new File(filepath);
        if(!file.isDirectory()){
            list.add(file.getName());
        }else if(file.isDirectory()){
            System.out.println("文件");
            String[] filelist=file.list();
            for(int i = 0;i<filelist.length;i++){
                File readfile = new File(filepath);
                if (!readfile.isDirectory()) {
                    list.add(readfile.getName());
                } else if (readfile.isDirectory()) {
                    readAllFile(filepath + "\\" + filelist[i]);
                }
            }
        }
    }
}

