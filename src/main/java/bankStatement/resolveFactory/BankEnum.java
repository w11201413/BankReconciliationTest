package bankStatement.resolveFactory;

import java.util.Arrays;

/**
 * @描述: 银行枚举
 */
public enum BankEnum {

    NONE("未知"),
    AGRICULTURALBANKOFCHINA2("农业银行对账单 2"),
    AGRICULTURALBANKOFCHINA("农业银行"),
    BANKOFCHINA("中国银行"),
    CHINAMERCHANTSBANK("招商银行"),
    PINGANBANK("平安银行");

    BankEnum(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return this.name;
    }

    public static BankEnum containsOf(final String name) {
        return Arrays.stream(BankEnum.values()).filter(f -> name.contains(f.getName())).findFirst().orElse(NONE);
    }
}
