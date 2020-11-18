/*
 * package com.oracle.kafka.demo;
 * 
 * import org.aspectj.lang.annotation.Aspect; import
 * org.aspectj.lang.annotation.Before; import
 * org.aspectj.lang.annotation.Pointcut;
 * 
 * @Aspect public class Logging {
 * 
 * @Pointcut("execution(* com.oracle.kafka.demo.producer.ProducerService.writeFileToFile(..))"
 * ) private void logInfo() {
 * 
 * }
 * 
 * @Before("logInfo()") public void beforeAdvice() {
 * System.out.println("Going to setup student profile."); } }
 */