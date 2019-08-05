import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @创建人: wangdong
 * @创建时间: 2019/8/4
 * @描述:
 */
public class BankReconciliationTest {

    @Test
    public void test() {

        // JDK8+新特性
        // Lambda

        List<String> testList = new ArrayList<String>();
        testList.add("3");

        // 对象流
        testList.stream().map(m -> m+"m").collect(Collectors.toList()).forEach(v -> System.out.println(v));

        // 国际化问题
        // 时间戳(UTC) 2019-01-01T04:00:00.333Z(0时区)
        // 一般时间写法: 2019-01-01 12:00:00.333(东八区) 2019-01-01 11:00:00.333(东七区)

        Date date = new Date();//Local
        System.out.println(date);

        // 时间戳: 0时区相对于格林威治时间的毫秒数
        // 0 ， 1， 东8 ,
        // LocalData / LocalDataTime /Instant (多了个时区概念)
        Instant instant = Instant.now();
        System.out.println(instant);

        LocalDateTime localDateTime = LocalDateTime.now(ZoneOffset.UTC);
        System.out.println(localDateTime);


        // Java源码(.java) -> JVM(虚拟机)(.class)[跨平台, Mac, Windows, Linux(硬件)] -> 机器码001010110101010010101010100

    }
}
