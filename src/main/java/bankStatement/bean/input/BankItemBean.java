package bankStatement.bean.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @描述: 对账单项目信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankItemBean {

    /**
     * 日期
     */
    private LocalDate data;

    /**
     * 入账
     */
    private Double income;

    /**
     * 出账
     */
    private Double outcome;

}
