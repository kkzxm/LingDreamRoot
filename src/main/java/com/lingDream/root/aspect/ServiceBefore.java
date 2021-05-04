package com.lingDream.root.aspect;

import com.baomidou.mybatisplus.annotations.TableId;
import com.lingDream.root.config.aboutService.ServiceAspectConfig;
import com.lingDream.root.mapper.MyMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import static com.lingDream.root.utils.strUtil.StringUtils.get_set;
import static com.lingDream.root.utils.strUtil.StringUtils.lowFirstChar;
import static java.util.Objects.isNull;

/**
 * @Author: 酷酷宅小明
 * @CreateTime: 2021-04-25 08:27
 */
@Aspect
@Component
public class ServiceBefore extends ApplicationObjectSupport {
    private final ServiceAspectConfig serviceAspectConfig;

    //region 属性/构造器
    ApplicationContext applicationContext;
    public ServiceBefore(ServiceAspectConfig serviceAspectConfig,
                         ApplicationContext applicationContext) {
        this.serviceAspectConfig = serviceAspectConfig;
        this.applicationContext = applicationContext;
    }
    //endregion

    //region insertBefore
    @Around("execution(* com.lingDream.root.service.MyService.insert(..))")
    public Object insertBefore(ProceedingJoinPoint pjp) throws Throwable {
        //开关控制
        if (serviceAspectConfig.isServiceInsertAspectOnOff()) {
            //数据准备
            Object entity = getEntity(pjp);
            Class<?> entityClass = entity.getClass();

            //查看唯一列是否存在,如果存在,则直接返回false
            Object selectByOnly = selectByOnly(entity);
            if (!isNull(selectByOnly)) return false;

            //遍历对象内部属性(筛选出与对象同包下的类对象(比如,学生类里面有个班级属性)),不存在则先添加一下,再次查询
            Field[] fields = entityClass.getDeclaredFields();
            for (Field field : fields) {
                Class<?> fieldType = field.getType();
                if (!fieldType.getPackage().equals(entityClass.getPackage())) continue;

                Object fieldValue = getFieldValue(field, entity);

                MyMapper<Object> fieldMapper = getMapper(fieldValue);
                Object selectOne = fieldMapper.selectOne(fieldValue);

                if (isNull(selectOne)) {
                    fieldMapper.insert(fieldValue);
                    selectOne = fieldMapper.selectOne(fieldValue);
                }

                String set = get_set(field.getName(), "set");
                entityClass.getMethod(set, fieldType).invoke(entity, selectOne);
            }
        }
        return pjp.proceed();
    }
    //endregion

    //region updateBefore
    /**
     * 更新之前的操作,
     * 更新之前查询唯一列是否存在,
     * 存在则直接返回false,不可修改
     * 不存在则执行修改
     */
    @Around("execution(* com.lingDream.root.service.MyService.updateById(..))")
    public Object updateBefore(ProceedingJoinPoint pjp) throws Throwable {
        //开关控制
        if (serviceAspectConfig.isServiceUpdateAspectOnOff()) {
            Object entity = getEntity(pjp);
            Object entityIdValue = getIDValue(entity);

            MyMapper<Object> mapper = getMapper(entity);
            Object selectByOnly = mapper.selectByOnly(entity);

            if (!isNull(selectByOnly) && !getIDValue(selectByOnly).equals(entityIdValue)) return false;
        }
        return pjp.proceed();
    }
    //endregion

    //region 公用

    /**
     * 查询唯一列
     */
    public Object selectByOnly(Object entity){
        return getMapper(entity).selectByOnly(entity);
    }
    /**
     * 得到方法参数中的实体对象 entity
     * @return 实体对象
     */
    private Object getEntity(ProceedingJoinPoint pjp){
        return  pjp.getArgs()[0];
    }

    /**
     * 得到IOC容器中的Mapper对象
     */
    private MyMapper<Object> getMapper(String mapperName){
        return (MyMapper<Object>) applicationContext.getBean(lowFirstChar(mapperName)+"Mapper");
    }

    private MyMapper<Object> getMapper(Object entity){
        return getMapper(entity.getClass().getSimpleName());
    }

    /**
     * 得到某个属性的值
     */
    private Object getFieldValue(Field field,Object entity) throws IllegalAccessException {
        field.setAccessible(true);
        return field.get(entity);
    }

    /**
     * 根据MyBatis的注解(@TableId),并得到该属性的值
     */
    private Object getIDValue(Object entity) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Object fieldValue = null;
        for (Field field : entity.getClass().getDeclaredFields()) {
            TableId annotation = field.getAnnotation(TableId.class);
            if (!isNull(annotation)){
                fieldValue = getFieldValue(field, entity);
                break;
            }
        }
        return fieldValue;
    }
    //endregion

}
