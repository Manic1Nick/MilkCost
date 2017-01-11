package ua.nick.milkcost.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListFilesUtil {

    private File directory;
    private File[] fList;

    public ListFilesUtil(String directoryName) {
        directory = new File(directoryName);
        fList = directory.listFiles();
    }

    public List<String> listFiles() {
        return Arrays.stream(fList)
                .filter(File::isFile)
                .map(File::getName)
                .collect(Collectors.toList());
    }

    public List<String> listFolders(){
        return Arrays.stream(fList)
                .filter(File::isDirectory)
                .map(File::getName)
                .collect(Collectors.toList());
    }

    public List<String> listFilesAndFilesSubDirectories() {
        List<String> result = new ArrayList<>();
        return findFilesInSubDirectories(result, directory.getAbsolutePath());
    }

    public List<String> findFilesInSubDirectories(List<String> result, String directoryName) {

        File[] fSubList = new File(directoryName).listFiles();
        for (File file : fSubList) {
            if (file.isFile())
                result.add(file.getAbsolutePath());

            else if (file.isDirectory())
                findFilesInSubDirectories(result, file.getAbsolutePath());
        }
        return result;
    }

    public static void main (String[] args){
        final String directoryLinuxMac ="/home/jessy/FOLDER2";
        ListFilesUtil listFilesUtil = new ListFilesUtil(directoryLinuxMac);
        List<String> res = listFilesUtil.listFilesAndFilesSubDirectories();
        for (String s :
                res) {
            System.out.println(s);
        }
    }
}
