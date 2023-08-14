package com.example.demowithtests.util.annotations;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

import static com.example.demowithtests.util.annotations.LogColorConstants.ANSI_BLUE;
import static com.example.demowithtests.util.annotations.LogColorConstants.ANSI_RESET;

@Log4j2
@Aspect
@Component
public class LoggingServiceClassesAspect {

    private LocalDateTime startTime;
    private final Statistics hibernateStatistics;

    @Autowired
    public LoggingServiceClassesAspect(SessionFactory sessionFactory) {
        this.hibernateStatistics = sessionFactory.getStatistics();
    }

    @Pointcut("execution(public * com.example.demowithtests.service..*.*(..))")
    public void callAtMyServicesPublicMethods() {
    }

    @Pointcut("execution(public * com.example.demowithtests.service.fillDataBase.LoaderServiceBean.*(..))")
    public void callAtLoader() {
    }

    @Before("callAtMyServicesPublicMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        startTime = LocalDateTime.now();
        if (args.length > 0) {
            log.debug(ANSI_BLUE + "Service: " + methodName + " - start. Args count - {}" + ANSI_RESET, args.length);
        } else {
            log.debug(ANSI_BLUE + "Service: " + methodName + " - start." + ANSI_RESET);
        }
    }

    @AfterReturning(value = "callAtMyServicesPublicMethods()", returning = "returningValue")
    public void logAfter(JoinPoint joinPoint, Object returningValue) {
        String methodName = joinPoint.getSignature().toShortString();
        Object outputValue;
        log.debug("Duration(in millis): " + Duration.between(startTime, LocalDateTime.now()).toMillis());
        if (returningValue != null) {
            if (returningValue instanceof Collection) {
                outputValue = "Collection size - " + ((Collection<?>) returningValue).size();
            } else if (returningValue instanceof byte[]) {
                outputValue = "File as byte[]";
            } else {
                outputValue = returningValue;
            }
            log.debug(ANSI_BLUE + "Service: " + methodName + " - end. Returns - {}" + ANSI_RESET, outputValue);
        } else {
            log.debug(ANSI_BLUE + "Service: " + methodName + " - end." + ANSI_RESET);
        }
    }
    @AfterReturning("callAtLoader()")
    public void countSqlQueries() {
        long queryCounter = hibernateStatistics.getPrepareStatementCount();
        log.debug("SQL queries:" + queryCounter);
        hibernateStatistics.clear();
    }
}
