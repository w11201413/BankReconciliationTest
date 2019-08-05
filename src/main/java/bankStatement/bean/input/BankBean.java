package bankStatement.bean.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @描述: 对账单账户信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankBean {

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 对账单项目列表
     */
    private List<BankItemBean> bankItemBeen;

}
