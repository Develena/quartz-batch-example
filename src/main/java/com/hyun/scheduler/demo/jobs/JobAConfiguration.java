package com.hyun.scheduler.demo.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

/**
 * Spring Batch Job 하나에 대한 수행 내용 정의
 * 1. Job 정의
 * 2. Step 정의
 * 3. Reader, Processor, Writer 정의
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JobAConfiguration {

    private static final String JOB_NAME = "Job_A";
    private static final String STEP_1_NAME = "A_Step_1";
    private static final String STEP_2_NAME = "A_Step_2";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

//    @RequiredArgsConstructor 또는
//    @Autowired
//    public ExampleJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
//        this.jobBuilderFactory = jobBuilderFactory;
//        this.stepBuilderFactory = stepBuilderFactory;
//    }

    @Bean
    public Job getJobA() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(getStep1())
                .next(getStep2())
                .build();
    }

    @Bean
    public CronTriggerFactoryBean getTrigger() {
        return BatchHelper.cronTriggerFactoryBeanBuilder()
                .cronExpression("0 0/1 * 1/1 * ? *") // @Todo : properties로 관리.
                .jobDetailFactoryBean(getSchedule())
                .build();
    }

    @Bean
    public JobDetailFactoryBean getSchedule() {
        return BatchHelper.jobDetailFactoryBeanBuilder()
                .job(getJobA())
                .build();
    }



    /**
     * Step 1 생성
     * @return
     */
    @Bean
    @JobScope
    public Step getStep1() {
        return stepBuilderFactory.get(STEP_1_NAME)
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step1");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    /**
     * Step 2 생성
     * @return
     */
    @Bean
    @JobScope
    public Step getStep2() {
        return stepBuilderFactory.get(STEP_2_NAME)
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step2");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    /**
     * Step 생성의 예.
     * @Todo : reader, processor, writer 생성하기.
     */
//    private Step exampleJob1Step() {
//        return stepBuilderFactory.get(STEP_NAME)
//                .<String, String>chunk(2) // commit 단위 등록
//                .reader(exampleJob1Reader()) // reader 등록
//                .processor(exampleJob1Processor()) // processor 등록
//                .writer(exampleJob1Writer()) // writer 등록
//                .build();
//    }

//    @Bean
//    @StepScope
//    public ItemReader<String> exampleJob1Reader() {
//        return new ItemReader<String>() {
//            private List<String> sampleData;
//            private int count;
//
//            @Override
//            public String read() throws Exception {
//                fetch();
//                return next();
//            }
//
//            private String next() {
//                if (this.count >= this.sampleData.size()) {
//                    return null;
//                }
//                return this.sampleData.get(count++);
//            }
//
//            private void fetch() {
//                if(isInitialized()){
//                    return;
//                }
//                this.sampleData = IntStream.range(0, 20).boxed().map(String::valueOf).map(s -> s + "-read").collect(Collectors.toList());
//            }
//
//            private boolean isInitialized() {
//                return this.sampleData != null;
//            }
//        };
//    }
//
//    private ItemProcessor<String, String> exampleJob1Processor() {
//        return item -> item + "-processing";
//    }
//
//    private ItemWriter<String> exampleJob1Writer() {
//        return items -> items.stream().map(o -> "[" + JOB_NAME + "] " + o + "-write").forEach(log::info);
//    }
}
