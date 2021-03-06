package ua.nick.milkcost.model;

import javax.persistence.*;
import java.io.File;
import java.util.Date;

@Entity
@Table(name = "files")
public class FileDescription {

    private Long id;
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
            this.typeCosts = determineTypeCostsOfFile();
            this.dateOfLastChange = new Date(file.lastModified());
            this.file = file;
        }
    }

    public TypeCosts determineTypeCostsOfFile() {
        if (fileName.toLowerCase().contains("direct"))
            return TypeCosts.DIRECT;
        else if (fileName.toLowerCase().contains("overhead"))
            return TypeCosts.OVERHEAD;
        else if (fileName.toLowerCase().contains("additional"))
            return TypeCosts.ADDITIONAL;

        return null;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    @Temporal(TemporalType.TIMESTAMP)
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
