package bankStatement.resolve.impl;

import bankStatement.bean.input.BankBean;
import bankStatement.bean.input.BankItemBean;
import bankStatement.resolve.BankStatementResolve;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @描述: 中国招商银行流水解析类
 */
public class ChinaMerchantsBankStatementResolve extends BankStatementResolve{

    public ChinaMerchantsBankStatementResolve(File file) {
        super(file);
    }

    @Override
    public BankBean resolve(File file) {
        ExcelReader excelReader = ExcelUtil.getReader(file);
        List<List<Object>> datas = excelReader.read();
        List<BankItemBean> bankItemBeen = new ArrayList<>();
        BankBean bankBean = new BankBean(datas.get(0).get(0).toString(), datas.get(1).get(3).toString(), bankItemBeen);
        for (int i=9; i<datas.size(); i++) {
            bankItemBeen.add(new BankItemBean(
                    LocalDate.parse(datas.get(i).get(0).toString(), DateTimeFormatter.ofPattern("yyyyMMdd")),
                    Double.valueOf("".equals(datas.get(i).get(5).toString()) ? "0" : datas.get(i).get(5).toString()) ,
                    Double.valueOf("".equals(datas.get(i).get(4).toString()) ? "0" : datas.get(i).get(4).toString())
            ));
        }
        return bankBean;
    }
}
