package ua.nick.milkcost.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.File;
import java.util.Date;

@Entity
@Table(name = "files")
public class FileDescription {

    private String fileName;
    private TypeCosts typeCosts;
    private Date dateOfLastChange;

    private File file;

    public FileDescription() {
    }

    public FileDescription(TypeCosts typeCosts, String fileName, Date dateOfLastChange) {
        this.fileName = fileName;
        this.typeCosts = typeCosts;
        this.dateOfLastChange = dateOfLastChange;
    }

    public FileDescription(File file) {
        if (file.isFile()) {
            this.fileName = file.getName();
            this.typeCosts = getTypeCostsOfFile();
            this.dateOfLastChange = new Date(file.lastModified());
            this.file = file;
        }
    }

    public TypeCosts getTypeCostsOfFile() {
        if (fileName.toLowerCase().contains("direct"))
            return TypeCosts.DIRECT;
        else if (fileName.toLowerCase().contains("overhead"))
            return TypeCosts.OVERHEAD;
        else if (fileName.toLowerCase().contains("additional"))
            return TypeCosts.ADDITIONAL;

        return null;
    }

    @Id
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public TypeCosts getTypeCosts() {
        return typeCosts;
    }

    public void setTypeCosts(TypeCosts typeCosts) {
        this.typeCosts = typeCosts;
    }

    public Date getDateOfLastChange() {
        return dateOfLastChange;
    }

    public void setDateOfLastChange(Date dateOfLastChange) {
        this.dateOfLastChange = dateOfLastChange;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
