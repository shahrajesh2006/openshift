package com.loyalty.web.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.loyalty.web.errors.BusinessException;
import com.loyalty.web.errors.SystemException;


public class ReflectionUtils 
{
	public static String toString(Object object) {
	    return "";
		/*Method[] methods = object.getClass().getMethods();
        int len = methods.length;
        Object[] objParam = new Object[0];
        StringBuffer sb = new StringBuffer();
        sb.append(object.getClass().getName()).append("\n")
        	.append("{").append("\n");
        for (int i = 0; i < len; i++)
        {
            String name = methods[i].getName();
            if (name.startsWith("get") && methods[i].getParameterTypes().length == 0)
            {
                try {
                    Object o = methods[i].invoke(object, objParam);
                    sb.append(name.substring(3));
                    sb.append(":[");
                    if(o != null)
                        sb.append(o.toString());
                    else
                        sb.append("null");
                    sb.append("]\n");
                } catch (InvocationTargetException ex) {
                } catch (IllegalArgumentException ex) {
                } catch (IllegalAccessException ex) {
                }
            }
        }
        sb.append("}");
        return sb.toString();
        */
	}
	
    /** Creates a new instance of ReflectionUtils */
    public ReflectionUtils() {
    }
    
    
    /**
     * Calls supplied method on supplied object
     * 
     * Example, 
     * person [p] has attribute of [addresses]
     * you want to call "getAddresses"
     * 
     * then, 
     * Collection addresses = (Collection) ReflectionUtil.callMethodNoArgs(Person.class, p, "getAddresses"); 
     * 
     * 
     * @param clazz Class of target object
     * @param obj Object you want method called on
     * @param methodName
     * @return results of method execution
     */
    public static Object callMethodNoArgs(Class clazz, Object obj, String methodName) throws Exception {
        
        
        return callMethodWithArgs(clazz, obj, methodName, new Class[] {}, new Object[] {} );
        
    }
    
    /**
     * @param clazz Class of target object
     * @param obj Object you want method called on
     * @param methodName
     * @param classArgs
     * @param Object[] methodArgs
     * @return results of method execution
     */
    public static Object callMethodWithArgs(Class clazz, Object obj, String methodName, Class[] classArgs, Object[] methodArgs) throws SystemException,BusinessException {
        
        Object returnValue = null;
       
        Method m = null;
        try {
        	m = clazz.getMethod(methodName, classArgs);
        } catch (NoSuchMethodException nsme) {
        	String errorMsg = "NoSuchMethodException --- An attempt was made to call "+clazz.getName()+"."+methodName+"("+classArgs[0].getName()+").  This method has not yet been implmented.";
        	throw new SystemException("00000", errorMsg, nsme);
        }
        
        try {
        returnValue = m.invoke(obj, methodArgs );
        }
        catch(InvocationTargetException e){
           Throwable throwable = e.getTargetException();
            
            if(throwable instanceof SystemException){
                throw (SystemException)throwable; //We need to throw the SystemException
            }
            else if(throwable instanceof BusinessException){
                throw (BusinessException)throwable; // We need to throw the BusinessException
            }
            else {
                String errorMsg = e.getMessage(); 
            throw new SystemException("00000", errorMsg, e);
            }
        }
        catch (Exception e) {
           
             String errorMsg = e.getMessage(); 
            throw new SystemException("00000", errorMsg, e);
        }
     
        
        return returnValue;
        
    }
    
    /**
     * Construct object of supplied Class
     * @param clazz Class of target object
     * @return instance of target object
     */
     public static Object callConstructorNoArgs(Class clazz) throws Exception {
         
         return clazz.newInstance();
         
     }
     
     /**
     * Construct object of supplied Class with supplied args
     * @param clazz Class of target object
     * @return instance of target object
     */
     public static Object callConstructorWithArgs(Class clazz, Class[] constructorClasses, Object[] constructorArgs) throws Exception {
         
         Constructor constructor = clazz.getConstructor(constructorClasses);
         
         Object obj = constructor.newInstance(constructorArgs);
         
         return obj;
         
     }

}
