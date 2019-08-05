package bankStatement.bean.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @描述: 银行总信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankInfoBean {

    /**
     * 名称
     */
    private String name;

    /**
     * 进账
     */
    private Double income;

    /**
     * 出帐
     */
    private Double outcome;

    /**
     * 1-10号区段信息
     */
    private BankBlockInfoBean oneToTen;

    /**
     * 11-20号区段信息
     */
    private BankBlockInfoBean elevenToTwenty;

    /**
     * 21-号区段信息
     */
    private BankBlockInfoBean twentyOneToEnd;

}
