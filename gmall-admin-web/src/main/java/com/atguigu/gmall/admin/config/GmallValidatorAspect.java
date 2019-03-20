package com.atguigu.gmall.admin.config;


import com.atguigu.gmall.to.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Slf4j
@Aspect  //说明这是一个切面
@Component
public class GmallValidatorAspect {


    @Around("execution(* com.atguigu.gmall.admin..controller..*.*(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("校验切面切入进行工作......");
        //proceedingJoinPoint.proceed();
        //xxx.invoke();
        Object[] args = proceedingJoinPoint.getArgs();
        Object proceed = null;

            //前置通知
            for (Object obj:args) {
                //获取当前所有参数
                if(obj instanceof BindingResult){
                    //只获取感兴趣的BindingResult
                    //判断校验有无错误
                    int count = ((BindingResult) obj).getErrorCount();
                    if(count > 0){
                        //有错误
                        log.info("校验发生错误。。。直接给用户返回....");
                        CommonResult commonResult = new CommonResult().validateFailed((BindingResult) obj);
                        return commonResult;
                    }
                }
            }
            //方法执行完成  method.invoke();
            proceed = proceedingJoinPoint.proceed(args);

        //后置通知
        return proceed;
    }
}
