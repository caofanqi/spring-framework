package cn.caofanqi.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TestAspect {

	@Pointcut("execution(* cn.caofanqi.service..*.*(..))")
	public void pointcut() {
	}

	@Before("pointcut()")
	public void before() {
		System.out.println("======before======");
	}


	@After("pointcut()")
	public void after() {
		System.out.println("======after======");
	}

	@AfterReturning("pointcut()")
	public void afterReturning() {
		System.out.println("======afterReturning======");
	}

	@AfterThrowing("pointcut()")
	public void afterThrowing() {
		System.out.println("======afterThrowing======");
	}

	@Around("pointcut()")
	public void around(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("======around before======");
		pjp.proceed();
		System.out.println("======around after======");
	}

}
