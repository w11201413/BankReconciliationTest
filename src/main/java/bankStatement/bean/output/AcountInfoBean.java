package bankStatement.bean.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @描述: 账号总信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcountInfoBean {

    /**
     * 账号名
     */
    private String name;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份
     */
    private Integer mouth;

    /**
     * 进账
     */
    private Double income;

    /**
     * 出帐
     */
    private Double outcome;

    /**
     * 银行信息
     */
    private Map<String, BankInfoBean> bankInfoBeanMap;

}
