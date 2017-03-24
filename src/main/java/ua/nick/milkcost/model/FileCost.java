package ua.nick.milkcost.model;

import javax.persistence.*;
import java.io.File;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "files")
public class FileCost {

    private Long id;
    private String fileName;
    private TypeCosts typeCosts;
    private Long periodId;
    private Date dateOfLastChange;
    private Set<Cost> costs;
    private File file;
    private boolean changed;

    public FileCost() {
    }

    public FileCost(TypeCosts typeCosts, String fileName, Date dateOfLastChange) {
        this.fileName = fileName;
        this.typeCosts = typeCosts;
        this.dateOfLastChange = dateOfLastChange;
    }

    public FileCost(File file) {
        if (file.isFile()) {
            this.fileName = file.getName();
            this.typeCosts = determineTypeCostsOfFile();
            this.dateOfLastChange = new Date(file.lastModified());
            this.file = file;
        }
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

    public Long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Long periodId) {
        this.periodId = periodId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateOfLastChange() {
        return dateOfLastChange;
    }

    public void setDateOfLastChange(Date dateOfLastChange) {
        this.dateOfLastChange = dateOfLastChange;
    }

    @Transient
    public Set<Cost> getCosts() {
        return costs;
    }

    public void setCosts(Set<Cost> costs) {
        this.costs = costs;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    private TypeCosts determineTypeCostsOfFile() {
        if (fileName.toLowerCase().contains("direct"))
            return TypeCosts.DIRECT;
        else if (fileName.toLowerCase().contains("overhead"))
            return TypeCosts.OVERHEAD;
        else if (fileName.toLowerCase().contains("additional"))
            return TypeCosts.ADDITIONAL;

        return null;
    }
}
