package ua.nick.milkcost.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.nick.milkcost.utils.DateUtil;
import ua.nick.milkcost.utils.ReadExcelFileUtil;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class CostStructureFactory {

    @Autowired
    private ReadExcelFileUtil readFilesUtil;

    @Autowired
    private DateUtil dateUtil;

    private List<FileDescription> newFiles;
    private Set<String> accounts;

    public CostStructureFactory() {
    }

    public void setNewFiles(List<FileDescription> newFiles) {
        this.newFiles = newFiles;
    }

    public void setAccounts(Set<String> accounts) {
        this.accounts = accounts;
    }

    public CostStructure createNewCostStructure(TypeCosts type, CostStructure... args) {
        CostStructure costStructure = null;

        String filePath = readFilesUtil.getFilePath(type, newFiles);
        Date period = filePath.length() > 0 ? dateUtil.getDateFromFilePath(filePath) : null;

        if (type == TypeCosts.DIRECT)
            costStructure = new CostStructureDirect(period, accounts, filePath);

        else if (type == TypeCosts.OVERHEAD)
            costStructure = new CostStructureOverhead(period, accounts, filePath);

        else if (type == TypeCosts.ADDITIONAL)
            costStructure = new CostStructureAdditional(period, accounts, filePath);

        else if (type == TypeCosts.TOTAL && args.length > 0)
            costStructure = new CostStructureTotal(args);

        return costStructure;
    }
}
