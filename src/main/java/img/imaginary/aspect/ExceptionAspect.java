package img.imaginary.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import img.imaginary.exception.DaoException;
import img.imaginary.exception.ServiceException;

@Aspect
@Component
public class ExceptionAspect {

    @Pointcut("execution(public * *(..))")
    private void allPublicMethods() {

    }
    
    @Pointcut("within(img.imaginary.service.*)")
    private void inService() {

    }
    
    @AfterThrowing(pointcut = "allPublicMethods() && inService()", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, DaoException exception) {
        throw new ServiceException(exception.getMessage(), exception);
    }
}
