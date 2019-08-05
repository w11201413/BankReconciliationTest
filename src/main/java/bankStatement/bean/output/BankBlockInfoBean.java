package bankStatement.bean.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @描述: 银行区间信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankBlockInfoBean {

    /**
     * 进账
     */
    private Double income;

    /**
     * 出账
     */
    private Double outcome;

}
