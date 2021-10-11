package com.test.TestAPIAF.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * 
 * @author Laurent
 * This aspect manage logging value at entrance or outting on our API
 */
@Aspect
@Component
public class LoggingAspect {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Around("execution(* com.test.TestAPIAF.controller.*.*(..))")
	public Object methodTimeLogger(ProceedingJoinPoint joinPoint) throws Throwable {

		// Get method details
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		String className = methodSignature.getDeclaringType().getSimpleName();
		String methodName = methodSignature.getName();
		
		// Log input parameters
		if(logger.isInfoEnabled()) {
			Object[] signatureArgs = joinPoint.getArgs();
			logger.info("Input {} -> {} :",className, methodName);
			if(signatureArgs != null) {
				for(Object arg : signatureArgs){
					logger.info("Arg : {} ",arg);
				}
			}
		}

		// Measure method execution time
		StopWatch stopWatch = new StopWatch(className + "->" + methodName);
		stopWatch.start(methodName);
		Object result = joinPoint.proceed();
		stopWatch.stop();
		// Log method execution time
		if (logger.isInfoEnabled()) {
			logger.info(stopWatch.prettyPrint());
			if(result != null) {
				logger.info("Output {} -> {} : ",className, methodName);
				logger.info(result.toString());
			}else {
				logger.info("Output {} -> {} : none",className, methodName);
			}
		}
		return result;
	}

	@AfterThrowing(pointcut="execution(* com.test.TestAPIAF.controller.*.*(..))",throwing="ex")
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
		// Get method details
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		String className = methodSignature.getDeclaringType().getSimpleName();
		String methodName = methodSignature.getName();
		logger.error("Error {} -> {} : {}",className, methodName, ex.getMessage());
    }

}
