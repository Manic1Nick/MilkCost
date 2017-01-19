package ua.nick.milkcost.utils;

import ua.nick.milkcost.model.Constants;
import ua.nick.milkcost.model.FileDescription;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ListFilesUtil {

    private File directory;
    private File[] fList;

    public ListFilesUtil() {
        directory = new File(Constants.FOLDER_WITH_FILES_FOR_MILK_COST);
        fList = directory.listFiles();
    }

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

    public File[] getfList() {
        return this.fList;
    }

    public List<FileDescription> getNewFileDescriptions(
            List<FileDescription> filesInDB, List<File> filesInFolder) {

        if (filesInDB.size() == 0)
            return copyFilesToFileDescriptions(filesInFolder);

        return findNewFilesAndCopyToFileDescriptions(filesInDB, filesInFolder);
    }

    private List<FileDescription> copyFilesToFileDescriptions(List<File> filesInFolder) {
        List<FileDescription> newFileDescriptions = new ArrayList<>();

        for (File file : filesInFolder)
            newFileDescriptions.add(new FileDescription(file));

        return newFileDescriptions;
    }

    private List<FileDescription> findNewFilesAndCopyToFileDescriptions(
                List<FileDescription> filesInDB, List<File> filesInFolder) {
        List<FileDescription> newFileDescriptions = new ArrayList<>();

        for (File file : filesInFolder) {
            Date dateLastModified = new Date(file.lastModified());

            for (FileDescription fileDescription : filesInDB) {
                if (file.getName().equals(fileDescription.getFileName()) &&
                        dateLastModified.after(fileDescription.getDateOfLastChange())) {
                    newFileDescriptions.add(new FileDescription(file));
                }
            }
        }
        return newFileDescriptions;
    }

    //test
    public static void main (String[] args){
        final String directoryLinuxMac ="/home/jessy/FOLDER2/FOLDER_EMPTY";
        ListFilesUtil listFilesUtil = new ListFilesUtil(directoryLinuxMac);
        List<String> res = listFilesUtil.listFilesAndFilesSubDirectories();
        for (String s : res) {
            System.out.println(s);
        }
    }
}
