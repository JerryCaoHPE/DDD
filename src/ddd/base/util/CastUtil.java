package ddd.base.util;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import ddd.base.exception.DDDException;

public class CastUtil {

    // 转为 String 型
    public static String castString(Object obj) {
        return CastUtil.castString(obj, "");
    }

    // 转为 String 型（提供默认值）
    public static String castString(Object obj, String defaultValue) {
        return obj != null ? String.valueOf(obj) : defaultValue;
    }

    // 转为 double 型 
    public static double castDouble(Object obj) {
        return CastUtil.castDouble(obj, 0);
    }

    // 转为 double 型（提供默认值）
    public static double castDouble(Object obj, double defaultValue) {
        double doubleValue = defaultValue;
        if (obj != null) {
            String strValue = castString(obj);
            if (StringUtils.isNotEmpty(strValue)) {
                try {
                    doubleValue = Double.parseDouble(strValue);
                } catch (NumberFormatException e) {
                    doubleValue = defaultValue;
                }
            }
        }
        return doubleValue;
    }
    
    // 转为 Float 型 
    public static float castFloat(Object obj) {
        return CastUtil.castFloat(obj, 0);
    }

    // 转为 Float 型（提供默认值）
    public static float castFloat(Object obj, float defaultValue) {
    	float doubleValue = defaultValue;
        if (obj != null) {
            String strValue = castString(obj);
            if (StringUtils.isNotEmpty(strValue)) {
                try {
                    doubleValue = Float.parseFloat(strValue);
                } catch (NumberFormatException e) {
                    doubleValue = defaultValue;
                }
            }
        }
        return doubleValue;
    }

    // 转为 long 型
    public static long castLong(Object obj) {
        return CastUtil.castLong(obj, 0);
    }

    // 转为 long 型（提供默认值）
    public static long castLong(Object obj, long defaultValue) {
        long longValue = defaultValue;
        if (obj != null) {
            String strValue = castString(obj);
            if (StringUtils.isNotEmpty(strValue)) {
                try {
                    longValue = Long.parseLong(strValue);
                } catch (NumberFormatException e) {
                    longValue = defaultValue;
                }
            }
        }
        return longValue;
    }

    // 转为 int 型
    public static int castInt(Object obj) {
        return CastUtil.castInt(obj, 0);
    }

    // 转为 int 型（提供默认值）
    public static int castInt(Object obj, int defaultValue) {
        int intValue = defaultValue;
        if (obj != null) {
            String strValue = castString(obj);
            if (StringUtils.isNotEmpty(strValue)) {
                try {
                    intValue = Integer.parseInt(strValue);
                } catch (NumberFormatException e) {
                    intValue = defaultValue;
                }
            }
        }
        return intValue;
    }

    // 转为 boolean 型
    public static boolean castBoolean(Object obj) {
        return CastUtil.castBoolean(obj, false);
    }

    // 转为 boolean 型（提供默认值）
    public static boolean castBoolean(Object obj, boolean defaultValue) {
        boolean booleanValue = defaultValue;
        if (obj != null) {
            booleanValue = Boolean.parseBoolean(castString(obj));
        }
        return booleanValue;
    }

    // 转为 String[] 型
    public static String[] castStringArray(Object[] objArray) {
        if (objArray == null) {
            objArray = new Object[0];
        }
        String[] strArray = new String[objArray.length];
        if (ArrayUtils.isNotEmpty(objArray)) {
            for (int i = 0; i < objArray.length; i++) {
                strArray[i] = castString(objArray[i]);
            }
        }
        return strArray;
    }

    // 转为 double[] 型
    public static double[] castDoubleArray(Object[] objArray) {
        if (objArray == null) {
            objArray = new Object[0];
        }
        double[] doubleArray = new double[objArray.length];
        if (!ArrayUtils.isEmpty(objArray)) {
            for (int i = 0; i < objArray.length; i++) {
                doubleArray[i] = castDouble(objArray[i]);
            }
        }
        return doubleArray;
    }

    // 转为 long[] 型
    public static long[] castLongArray(Object[] objArray) {
        if (objArray == null) {
            objArray = new Object[0];
        }
        long[] longArray = new long[objArray.length];
        if (!ArrayUtils.isEmpty(objArray)) {
            for (int i = 0; i < objArray.length; i++) {
                longArray[i] = castLong(objArray[i]);
            }
        }
        return longArray;
    }

    // 转为 int[] 型
    public static int[] castIntArray(Object[] objArray) {
        if (objArray == null) {
            objArray = new Object[0];
        }
        int[] intArray = new int[objArray.length];
        if (!ArrayUtils.isEmpty(objArray)) {
            for (int i = 0; i < objArray.length; i++) {
                intArray[i] = castInt(objArray[i]);
            }
        }
        return intArray;
    }

    // 转为 boolean[] 型
    public static boolean[] castBooleanArray(Object[] objArray) {
        if (objArray == null) {
            objArray = new Object[0];
        }
        boolean[] booleanArray = new boolean[objArray.length];
        if (!ArrayUtils.isEmpty(objArray)) {
            for (int i = 0; i < objArray.length; i++) {
                booleanArray[i] = castBoolean(objArray[i]);
            }
        }
        return booleanArray;
    }
    
    //根据字段的类型给对象赋值
  	public static Object parseValue(String value,String typeName){
  		
  		try {
  			Object valueResultObject = null;
  			//Integer 类型的数据
  			if(typeName.equals("int") || typeName.equals("java.lang.Integer")){
  				valueResultObject = Integer.parseInt(value);
  			}
  			//Long 类型的数据
  			else if(typeName.equals("long") || typeName.equals("java.lang.Long")){
  				valueResultObject = Long.parseLong(value);
  			}
  			//Float 类型的数据
  			else if(typeName.equals("float") || typeName.equals("java.lang.Float")){
  				valueResultObject = Float.parseFloat(value);
  			}
  			//Double 类型的数据
  			else if(typeName.equals("double") || typeName.equals("java.lang.Double")){
  				valueResultObject = Double.parseDouble(value);
  			}
  			//String 类型的对象
  			else if(typeName.equals("java.lang.String")){
  				valueResultObject = value;
  			}
  			//解析为时间的格式
  			else if(typeName.equals("java.util.Date")){
  				if(value.toString().equals("")){
  	  				valueResultObject =  null ;
  				}else {
  					Date tempDate = SysUtil.praseDate(value);
  	  				valueResultObject = tempDate;
  				}
  				
  			}
  			//解析为时间的格式
  			else if(typeName.equals("java.sql.Timestamp")){
  				if(value.toString().equals("")){
  	  				valueResultObject =  null ;
  				}else {
  				 
  					Date tempDate = SysUtil.praseDate(value);
  	  				valueResultObject = new Timestamp(tempDate.getTime());
  				}
  			}
  			else {
  				valueResultObject = value;
  			}
  			return valueResultObject;
  		} 
  		catch (Exception e) {
  			throw new DDDException("将 "+value+" 转换为类型 "+typeName+" 出错，原因是："+e.getMessage(), e);
  		}
  	}    
}