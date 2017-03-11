package ua.nick.milkcost.utils;

import org.springframework.stereotype.Component;
import ua.nick.milkcost.model.Constants;
import ua.nick.milkcost.model.FileDescription;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
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

    /*public List<FileDescription> getNewFileDescriptions(
            List<FileDescription> filesInDB, List<File> filesInFolder) { //filesInFolder > 0 !

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
                boolean names = file.getName().equals(fileDescription.getFileName());
                boolean dates = dateLastModified.after(fileDescription.getDateOfLastChange());
                if (!names || (names && dates))
                    newFileDescriptions.add(new FileDescription(file));
            }
        }
        return newFileDescriptions;
    }*/

	public List<FileDescription> getNewFileDescriptions(
				List<FileDescription> filesInDB, List<File> filesInFolder) { //filesInFolder.size() > 0 !
		List<FileDescription> newDbFiles = new ArrayList<>();

		if (filesInDB.size() == 0) {
            newDbFiles = filesInFolder.stream()
                    .map(file -> new FileDescription(file)).collect(Collectors.toList());
        } else {
            for (File file : filesInFolder) { //filesInDB.size() > 0 !
                FileDescription newDbFile = createNewDbFile(filesInDB, file);
                if (newDbFile != null)
                    newDbFiles.add(newDbFile);
            }
        }
		return newDbFiles;
    }

    private FileDescription createNewDbFile(List<FileDescription> filesInDB, File file) {
        FileDescription fileInDB = filesInDB.stream()
                .filter(f -> f.getFile().getName().equals(file.getName())).findAny().orElse(null);
		if (fileInDB != null) {
			Date newFileDateModified = new Date(file.lastModified());
			Date dbFileDateModified = fileInDB.getDateOfLastChange();

			if (!newFileDateModified.after(dbFileDateModified))
				return null;
		}
		return new FileDescription(file);
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
