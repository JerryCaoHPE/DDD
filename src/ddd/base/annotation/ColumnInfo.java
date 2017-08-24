package ddd.base.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *@author 作者:吴桥伟
 *@version 创建时间：2015年1月28日 下午3:45:27
 * 类说明
 */
public class  ColumnInfo {
	public final static int COLUMN_TYPE_INVALID=0; //不是合适的类型
	public final static int COLUMN_TYPE_PRIMARY=1;
	public final static int COLUMN_TYPE_ONE2ONE=2;
	public final static int COLUMN_TYPE_ONE2MANY=3;
	
	private boolean Id = false;

	private String name="";
	
	private String FKName="";

	private String label="";
	
	public String codeTable="";
	
	//字段类型：基本类型，一对一的引用，一对多的引用
	private int type;
	
	private String columnDefinition="";

	private int length;

	private boolean nullable = true;

	private int precision;

	private int scale;

	private boolean unique = false;
	
	private String uniqueName="";

	private Class<?> clazz;
	
	private String joinColumn="";
	
	private String joinTableName="";
	
	private String joinTableOneSide="";
	
	private String joinTableManySide="";

	private boolean composition = false;
	
	private DeleteType deleteType;
	
	private String comment="";

	private boolean index;
	
	private Field field;
	
	private Method fieldGetter;
	
	private Method fieldSetter;
	
	private Class<?> fieldType;

	private String orderBy="";
	
	public boolean isId() {
		return Id;
	}

	public void setId(boolean id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getColumnDefinition() {
		return columnDefinition;
	}

	public void setColumnDefinition(String columnDefinition) {
		this.columnDefinition = columnDefinition;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String getJoinColumn() {
		return joinColumn;
	}

	public void setJoinColumn(String joinColumn) {
		this.joinColumn = joinColumn;
	}

	public boolean isComposition() {
		return composition;
	}

	public void setComposition(boolean composition) {
		this.composition = composition;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isIndex() {
		return index;
	}

	public void setIndex(boolean index) {
		this.index = index;
	}

	public Class<?> getFieldType() {
		return fieldType;
	}

	public void setFieldType(Class<?> fieldType) {
		this.fieldType = fieldType;
	}

	public DeleteType getDeleteType() {
		return deleteType;
	}

	public void setDeleteType(DeleteType deleteType) {
		this.deleteType = deleteType;
	}

	public String getJoinTableName() {
		return joinTableName;
	}

	public void setJoinTableName(String joinTableName) {
		this.joinTableName = joinTableName;
	}

	public String getJoinTableOneSide() {
		return joinTableOneSide;
	}

	public void setJoinTableOneSide(String joinTableOneSide) {
		this.joinTableOneSide = joinTableOneSide;
	}

	public String getJoinTableManySide() {
		return joinTableManySide;
	}

	public void setJoinTableManySide(String joinTableManySide) {
		this.joinTableManySide = joinTableManySide;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Method getFieldGetter() {
		return fieldGetter;
	}

	public void setFieldGetter(Method fieldGetter) {
		this.fieldGetter = fieldGetter;
	}

	public Method getFieldSetter() {
		return fieldSetter;
	}

	public void setFieldSetter(Method fieldSetter) {
		this.fieldSetter = fieldSetter;
	}

	public static int getColumnTypeInvalid() {
		return COLUMN_TYPE_INVALID;
	}

	public static int getColumnTypePrimary() {
		return COLUMN_TYPE_PRIMARY;
	}

	public static int getColumnTypeOne2one() {
		return COLUMN_TYPE_ONE2ONE;
	}

	public static int getColumnTypeOne2many() {
		return COLUMN_TYPE_ONE2MANY;
	}

	public String getFKName() {
		return FKName;
	}

	public void setFKName(String fKName) {
		this.FKName = fKName;
	}
	
	public String getCodeTable() {
		return codeTable;
	}

	public void setCodeTable(String codeTable) {
		this.codeTable = codeTable;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

}
