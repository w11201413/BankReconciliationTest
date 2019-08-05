import bankStatement.bean.input.BankBean;
import bankStatement.bean.input.BankItemBean;
import bankStatement.bean.output.AcountInfoBean;
import bankStatement.bean.output.BankBlockInfoBean;
import bankStatement.bean.output.BankInfoBean;
import bankStatement.resolveFactory.BankEnum;
import bankStatement.resolveFactory.BankResolveFactory;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.NumberUtil;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @描述: 程序入口
 */
public class BankReconciliationTestMain {

    // 线程池
    private static ExecutorService executorService = Executors.newFixedThreadPool(30);

    // 文件输出地址
    private static String OUTPUTPATH = "./output/result.txt";

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 读取/解析全部对账单
        System.out.println("[INFO] 读取/解析全部对账单。");
        Instant start = Instant.now();
        List<Future<BankBean>> futures = new ArrayList();
        for (File file : FileUtil.loopFiles(new ClassPathResource("input").getFile())) {
            if (!FileUtil.isFile(file)) continue;

            BankEnum bankEnum = BankEnum.containsOf(file.getName());
            if (bankEnum == BankEnum.NONE) {
                System.out.println("[WARN] 读取到程序无法解析文件名。请确认:".concat(file.getPath()));
                continue;
            }

            futures.add(executorService.submit(BankResolveFactory.getResolve(bankEnum, file)));
        }
        executorService.shutdown();

        // 获取解析结果并分析组合数据
        Map<String, AcountInfoBean> acountInfoBeanMap = new HashMap<>();
        for (Future<BankBean> future : futures) {
            BankBean bankBean = future.get();
            for (BankItemBean item : bankBean.getBankItemBeen()) {
                // 将账号信息取出
                String acountInfoKey = bankBean.getAccountName()+item.getData().getYear()+item.getData().getMonthValue();
                AcountInfoBean acountInfoBean;
                if (acountInfoBeanMap.containsKey(acountInfoKey)) {
                    acountInfoBean = acountInfoBeanMap.get(acountInfoKey);
                } else {
                    acountInfoBean = new AcountInfoBean(bankBean.getAccountName(), item.getData().getYear(),
                            item.getData().getMonthValue(), 0.0, 0.0, new HashMap<>());
                    acountInfoBeanMap.put(acountInfoKey, acountInfoBean);
                }

                // 将银行信息取出
                Map<String, BankInfoBean> bankInfoBeanMap = acountInfoBean.getBankInfoBeanMap();
                BankInfoBean bankInfoBean;
                if (bankInfoBeanMap.containsKey(bankBean.getBankName())) {
                    bankInfoBean = bankInfoBeanMap.get(bankBean.getBankName());
                } else {
                    bankInfoBean = new BankInfoBean(bankBean.getBankName(), 0.0, 0.0, new BankBlockInfoBean(0.0, 0.0),
                            new BankBlockInfoBean(0.0, 0.0), new BankBlockInfoBean(0.0, 0.0));
                    bankInfoBeanMap.put(bankBean.getBankName(), bankInfoBean);
                }

                // 将区段信息取出
                BankBlockInfoBean oneToTen = bankInfoBean.getOneToTen();
                BankBlockInfoBean elevenToTwenty = bankInfoBean.getElevenToTwenty();
                BankBlockInfoBean twentyOneToEnd = bankInfoBean.getTwentyOneToEnd();
                if (item.getData().getDayOfMonth() >= 1 && item.getData().getDayOfMonth() <= 10) {
                    oneToTen.setIncome(oneToTen.getIncome() + item.getIncome());
                    oneToTen.setOutcome(oneToTen.getOutcome() + item.getOutcome());
                } else if (item.getData().getDayOfMonth() >= 11 && item.getData().getDayOfMonth() <= 20) {
                    elevenToTwenty.setIncome(oneToTen.getIncome() + item.getIncome());
                    elevenToTwenty.setOutcome(oneToTen.getOutcome() + item.getOutcome());
                } else {
                    twentyOneToEnd.setIncome(oneToTen.getIncome() + item.getIncome());
                    twentyOneToEnd.setOutcome(oneToTen.getOutcome() + item.getOutcome());
                }

                bankInfoBean.setIncome(bankInfoBean.getIncome() + item.getIncome());
                bankInfoBean.setOutcome(bankInfoBean.getOutcome() + item.getOutcome());

                acountInfoBean.setIncome(acountInfoBean.getIncome() + item.getIncome());
                acountInfoBean.setOutcome(acountInfoBean.getOutcome() + item.getOutcome());
            }
        }
        System.out.println("[INFO] 获取解析结果并分析组合数据完成。");

        // 重新排序数据
        List<AcountInfoBean> acountInfoBeanList = acountInfoBeanMap.entrySet().stream().sorted((x, y) -> x.getKey().compareTo(y.getKey()))
                .map(m -> m.getValue()).collect(Collectors.toList());

        // 输出数据
        System.out.println("[INFO] 开始输出数据。");
        if (FileUtil.exist(OUTPUTPATH)) FileUtil.del(OUTPUTPATH);
        FileWriter writer = FileWriter.create(FileUtil.file(OUTPUTPATH));
        int number = 1;
        for (int i=0; i<acountInfoBeanList.size(); i++) {
            AcountInfoBean acountInfoBean = acountInfoBeanList.get(i);
            if (i > 0 && !acountInfoBean.getName().equals(acountInfoBeanList.get(i-1).getName())) number++;
            writer.append(StrFormatter.format("客户{}: {}  年份:{}  月份:{}  合计:进账:{}元  出账:{}元\n",
                        number, acountInfoBean.getName(), acountInfoBean.getYear(), acountInfoBean.getMouth(),
                        NumberUtil.round(acountInfoBean.getIncome(), 2), NumberUtil.round(acountInfoBean.getOutcome(), 2)
                    ));
            acountInfoBean.getBankInfoBeanMap().forEach((k, v) -> {
                writer.append(StrFormatter.format("    开户行: {}\n", k));
                writer.append(StrFormatter.format("         小计: 进账: {}元  出账: {}元\n", NumberUtil.round(v.getIncome(), 2), NumberUtil.round(v.getOutcome(), 2)));
                writer.append(StrFormatter.format("         其中: 1号—10号, 进账: {}元  出账: {}元\n", NumberUtil.round(v.getOneToTen().getIncome(), 2), NumberUtil.round(v.getOneToTen().getOutcome(), 2)));
                writer.append(StrFormatter.format("         其中: 11号—20号, 进账: {}元  出账: {}元\n", NumberUtil.round(v.getElevenToTwenty().getIncome(), 2), NumberUtil.round(v.getElevenToTwenty().getOutcome(), 2)));
                writer.append(StrFormatter.format("         其中: 21号—月底, 进账: {}元  出账: {}元\n", NumberUtil.round(v.getTwentyOneToEnd().getIncome(), 2), NumberUtil.round(v.getTwentyOneToEnd().getOutcome(), 2)));
            });
        }

        Instant end = Instant.now();
        System.out.println("[INFO] 输出完成。总耗时:" + (end.toEpochMilli() - start.toEpochMilli()) + "ms。");
    }

}
