package com.szcgc.cougua.service.impl;

import com.szcgc.cougua.service.PasswordCrackService;
import com.szcgc.cougua.utils.PassWordUtil;
import com.szcgc.cougua.utils.UnZip7ZRarUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadProcessPasswordCrackServiceImpl implements PasswordCrackService {
    private static final Logger logger = LoggerFactory.getLogger(ThreadProcessPasswordCrackServiceImpl.class);
    private ThreadPoolExecutor threadPoolExecutor;

    public ThreadProcessPasswordCrackServiceImpl() {
        this.threadPoolExecutor = new ThreadPoolExecutor(4, 20, 60,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    @Override
    public String run(String source, String dest) {
//        List<String> numberStr = PassWordUtil.getNumberStr(6);
        List<String> numberStr = PassWordUtil.getNumberStr(8);
//        List<String> noA = getNoA(4);
        logger.info("共多少密码：{}",numberStr);
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
        numberStr.forEach(num -> queue.offer(num));
        long startTime = System.currentTimeMillis();
        while (!queue.isEmpty()) {
            threadPoolExecutor.execute(() -> {
                String name = Thread.currentThread().getName();
                String key = queue.poll();
                if (StringUtils.isNotBlank(key)) {
                    boolean result = UnZip7ZRarUtils.unRar(source, dest, key);
                    if (result) {
                        try (FileWriter fileWriter = new FileWriter(dest + "\\password.txt")) {
                            fileWriter.write(key);
                            System.out.println("线程：" + name + ",密码是：" + key);
                            queue.clear();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("线程：" + name + "," + key + "密码错误");
                    }
                }
            });
        }

        final long endTime = System.currentTimeMillis();
        System.out.println("共花费：" + (endTime - startTime) / 1000 + "秒");
        return null;
    }


}
