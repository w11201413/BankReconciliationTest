package bankStatement.resolveFactory;

import bankStatement.resolve.BankStatementResolve;
import bankStatement.resolve.impl.*;

import java.io.File;

/**
 * @描述: 获取流水解析类的工厂
 */
public class BankResolveFactory {

    public static BankStatementResolve getResolve(BankEnum bankEnum, File file) {
        BankStatementResolve bankStatementResolve = null;
        switch (bankEnum) {
            case NONE:
                break;
            case AGRICULTURALBANKOFCHINA:
                bankStatementResolve = new AgriculturalBankOfChinaStatementResolve(file);
                break;
            case AGRICULTURALBANKOFCHINA2:
                bankStatementResolve = new AgriculturalBankOfChinaStatementResolve2(file);
                break;
            case BANKOFCHINA:
                bankStatementResolve = new BankOfChinaStatementResolve(file);
                break;
            case CHINAMERCHANTSBANK:
                bankStatementResolve = new ChinaMerchantsBankStatementResolve(file);
                break;
            case PINGANBANK:
                bankStatementResolve = new PingAnBankStatementResolve(file);
                break;
        }
        return bankStatementResolve;
    }

}
