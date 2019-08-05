package bankStatement.resolve;

import bankStatement.bean.input.BankBean;

import java.io.File;
import java.time.Instant;
import java.util.concurrent.Callable;

/**
 * @描述: 银行流水解析接口
 */
public abstract class BankStatementResolve implements Callable<BankBean> {

    private File file;

    public BankStatementResolve(File file) {
        this.file = file;
    }

    @Override
    public BankBean call() throws Exception {
        System.out.println("[INFO] 读取/解析 [".concat(file.getPath()).concat(" ]开始."));
        Instant start = Instant.now();
        BankBean bankBean = resolve(this.file);
        Instant end = Instant.now();
        System.out.println("[INFO] 读取/解析 [".concat(file.getPath()).concat(" ]结束. 耗时: ").concat(String.valueOf(end.toEpochMilli() - start.toEpochMilli())).concat("ms。"));
        return bankBean;
    }

    public abstract BankBean resolve(File file);

}
