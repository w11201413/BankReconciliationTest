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
 * @描述: 中国银行流水解析实现类
 */
public class BankOfChinaStatementResolve extends BankStatementResolve{

    public BankOfChinaStatementResolve(File file) {
        super(file);
    }

    @Override
    public BankBean resolve(File file) {
        ExcelReader excelReader = ExcelUtil.getReader(file);
        List<List<Object>> datas = excelReader.read();
        List<BankItemBean> bankItemBeen = new ArrayList<>();
        BankBean bankBean = new BankBean(datas.get(0).get(0).toString(), datas.get(1).get(3).toString(), bankItemBeen);
        for (int i=3; i<datas.size(); i++) {
            Double amount = Double.valueOf(datas.get(i).get(13).toString());
            bankItemBeen.add(new BankItemBean(
                    LocalDate.parse(datas.get(i).get(10).toString(), DateTimeFormatter.ofPattern("yyyyMMdd")),
                    amount > 0 ? amount : 0 ,
                    amount < 0 ? Math.abs(amount) : 0
            ));
        }
        return bankBean;
    }
}
