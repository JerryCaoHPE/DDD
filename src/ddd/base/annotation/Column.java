package ddd.base.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 作者:吴桥伟
 * @version 创建时间：2015年1月8日 上午9:58:12
 * 
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	
	/**
	 * 主键
	 * @return
	 */
	public boolean Id() default false;
	
	/**
	 * 字段名,默认值是使用属性名作为字段名
	 * @return
	 */
	public String name() default "";
	
	/**
	 * 外鍵名，默认是manySideTableName_fk_oneSideTableName
	 * @return
	 */
	public String FKName() default "";
	
	/**
	 * 中文名 
	 * @return
	 */
	public String label() default "";
	
	/**
	 * 码表的键
	 * @return
	 */
	public String codeTable() default "";
	
	/**
	 * 表示创建表时，该字段创建的SQL语句
	 * @return
	 */
	public String columnDefinition() default "";
	
	/**
	 * length属性表示字段的长度，当字段的类型为varchar时，该属性才有效，默认为255个字符。
	 * @return
	 */
	public int length() default 255;
	
	/**
	 * 属性表示该字段是否可以为null值，默认为true。
	 * @return
	 */
	public boolean nullable() default true;
	
	/**
	 *  precision属性和scale属性表示精度，当字段类型为double时，
	 *  precision表示数值的总长度scale表示小数点所占的位数。
	 *   默认值为零，是使用数据库的默认值
	 * @return
	 */
	public int precision() default 0;

	public int scale() default 0;

	/**
	 * unique属性表示该字段是否为唯一标识，默认为false。
	 * @return
	 */
	public boolean unique() default false;
	/**
	 * 唯一键名称
	 * @return
	 */
	public String uniqueName() default "" ;
	
//	/**
//	 * 表示引用属性的数据类型， 若是一对多关联时，多端的类型，否则是类属性的类型
//	 * @return
//	 */
//	public Class<?> clazz() default Object.class;
	
	/**
	 * 外键字段名称，在组合关系中，一端在多端的引用 字段名称，
	 * 例如：一个学生有多本书，书的字段中有一个studenId 外键=（student.EId）学生的主键，joinColumn="studenId"
	 * 知道这个字段名称时，才能方便查询这个学生的书
	 * @return
	 */
	public String joinColumn() default "" ;
	
	/**
	 * 中间表名,建议命名格式:tablename_tableName
	 * @return
	 */
	public String joinTableName() default "";
	
	/**
	 * 中间表中的一端字段名
	 * @return
	 */
	public String joinTableOneSide() default "";
	
	/**
	 * 中间表中的多端字段名
	 * @return
	 */
	public String joinTableManySide() default "";
	
	
	/**
	 * 组合关系 为true时会级联添加更新（只级联一级）
	 * @return
	 */
	public boolean composition() default false;
	
	public String orderBy() default "";
	/**
	 * 删除类型 :
	 * RESTRICT 拒绝（默认值）,
	 * NO_ACTION 不处理,
	 * CASCADE 级联删除,
	 * SET_NULL 置为空
	 * @return   没用到
	 */
	public DeleteType deleteType() default DeleteType.RESTRICT;
	
	/**
	 * 提示
	 * @return
	 */
	public String comment() default "";
	
	/**
	 * 是否索引
	 * @return
	 */
	public boolean index() default false;
}
