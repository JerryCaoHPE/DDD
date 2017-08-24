/*package ddd.base.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.List;

import javax.swing.text.html.ListView;

import ddd.base.exception.DDDException;


public class HibernateLazyInitialize {
	public static Object initializePropertys(Object bean)// 加载bean中对应的一级的所有代理对象，例如传入student，则会级联加载一级的class
	{
		if (bean == null) {
			return null;
		}
		BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(bean.getClass(), Object.class);
		} catch (IntrospectionException e) {
			DDDException dddException = new DDDException(
					"HibernateLazyInitialize.initializePropertys(Object bean)",
					"反射出错，找不到对应的类信息", e);
			throw dddException;
		}
		PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
		BeanProxy beanProxy = new BeanProxy();
		for (PropertyDescriptor prop : props) {
			if (prop.getReadMethod() == null
					|| prop.getName().equals("hibernateLazyInitializer")) {
				continue;
			}
			try {
				Object value = beanProxy.getDDDValue(bean, prop.getName());
				initializeBean(value);
			} catch (MessageException e) {
				DDDException dddException = new DDDException(
						"HibernateLazyInitialize.initializePropertys(Object bean)",
						"反射出错，找不到对应的属性信息,原因是：" + e.getMessage(), e);
				throw dddException;
			}
			// System.out.println("执行完毕了for循环");
		}
		// System.out.println("执行完毕了initializeHierarchialPropertys");
		return bean;
	}

	public static void initializePropertys(List<?> beans)// 加载一个集合中的所有一级级联对象
	{
		for (Object bean : beans) {
			initializePropertys(bean);
		}
	}

	public static void initializePropertys(List<?> beans, String args)// 加载一个集合中固定的级联对象，例如传入List<Student>,"class.collega"
																		// 则会加载集合中所以的student.class
																		// 和student.class.collega
	{
		initializePropertys(beans, args, false);
	}

	*//**
	 * 
	 * @param beans
	 * @param args
	 * @param isInitializePropertys
	 *            如果为 true ,则初始化 args 所指属性对应的对象的所有属性
	 *//*
	public static void initializePropertys(List<?> beans, String args,
			Boolean isInitializePropertys)// 加载一个集合中固定的级联对象，例如传入List<Student>,"class.collega"
											// 则会加载集合中所以的student.class
											// 和student.class.collega
	{
		for (Object bean : beans) {
			initializePropertys(bean);
			initializeProperty(bean, args, isInitializePropertys);
		}
	}

	*//**
	 * 
	 * @param bean
	 * @param args
	 *//*
	public static void initializeProperty(Object bean, String args)// 加载对象的固定的级联对象，例如传入student,"class.collega"
																	// 则会加载student.class
																	// 和student.class.collega
	{
		initializeProperty(bean, args, false);
	}

	*//**
	 * 
	 * @param bean
	 * @param args
	 * @param isInitializePropertys
	 *            如果为 true ,则初始化 args 所指属性对应的对象的所有属性
	 *//*
	public static void initializeProperty(Object bean, String args,
			Boolean isInitializePropertys)// 加载对象的固定的级联对象，例如传入student,"class.collega"
											// 则会加载student.class
											// 和student.class.collega
	{
		if (bean == null) {
			// System.err.println("STOP==传入的bean为空");
			return;
		}

		initializeBean(bean);

		// System.out.println("bean.id="+((CodeTable)bean).getCodeTableId().toString()+"args="+args);
		BeanProxy beanProxy = new BeanProxy();
		try {
			Object value = beanProxy.getDDDValue(bean,
					StringTools.getFirstArg(args));

			if (value == null) {
				// System.err.println("STOP==下一级为空");
				return;
			}
			if (isInitializePropertys == true) {
				if ((value instanceof PersistentBag)
						|| (value instanceof List<?>))// 下一级如果是集合类，则先循环后递推
				{
					initializePropertys((PersistentBag) value);

				} else {
					initializePropertys(value);
				}
			} else {
				initializeBean(value);
			}
			if (args.indexOf(".") != -1)// 如果还有参数没遍历完，则需要继续递推
			{
				args = args.substring(args.indexOf(".") + 1);
				if ((value instanceof HibernateProxy))// 下一级如果是代理类，则继续递推
				{
					initializeProperty(value, args);
				} else if ((value instanceof PersistentBag))// 下一级如果是集合类，则先循环后递推
				{
					Object[] beans = ((PersistentBag) value).toArray();
					if (beans.length == 0) {
						// System.err.println("STOP==没有子节点了");
						return;
					}
					for (Object object : beans) {
						initializeProperty(object, args);
					}
				} else if ((value instanceof List<?>))// 下一级如果是集合类，则先循环后递推
				{
					Object[] beans = ((List<?>) value).toArray();
					if (beans.length == 0) {
						// System.err.println("STOP==没有子节点了");
						return;
					}
					for (Object object : beans) {
						initializeProperty(object, args);
					}
				} else// 如果下一级就是普通的类，因为还有args，所以还得继续递推
				{
					initializeProperty(value, args);
				}
			} else {
				// System.out.println("正常结束");
			}
		} catch (MessageException e) {
			DDDException dddException = new DDDException(
					"HibernateLazyInitialize.initializePropertys(Object bean)",
					"反射出错，找不到对应的类信息", e);
			throw dddException;
		}
	}

	private static void initializeBean(Object bean)// 判断bean是不是代理类或者集合，如果是并且没被加载，则使用Hibernate.initialize加载
	{
		if (bean instanceof HibernateProxy) {
			if (org.hibernate.Hibernate.isInitialized(bean) == false) {
				Hibernate.initialize(bean);
			}
		} else if (bean instanceof PersistentBag) {
			if (((PersistentBag) bean).wasInitialized() == false) {
				Hibernate.initialize(bean);
			}
		}
	}

	public static void initializeHierarchialEntityPropertys(List<?> beans,
			String propertyName, int level)// 加载层次节点的代理对象，例如传入codeTable,"parent",3
											// 则加载codeTable的parent属性5次，等同于调用initializeProperty(codeTable,"parent.parent.parent")
	{
		for (Object bean : beans) {
			initializeHierarchialEntityProperty(bean, propertyName, level);
		}
	}

	public static void initializeHierarchialEntityProperty(Object bean,
			String propertyName, int level)// 加载层次节点的代理对象，例如传入codeTable,"parent",3
											// 则加载codeTable的parent属性5次，等同于调用initializeProperty(codeTable,"parent.parent.parent")
	{
		String temp = propertyName;
		if (level == 0)// 规定0为无限循环
		{
			// 20次为老大决定。。。
			for (int i = 0; i < 20; i++) {
				temp = temp + "." + propertyName;
			}
		} else if (level > 0) {
			for (int i = 0; i < level - 1; i++) {
				temp = temp + "." + propertyName;
			}
		}
		initializeProperty(bean, temp);
	}

	public static void initializeListViewLazyProperty(List<?> beans,
			ListView listView)// 初始化listView，传入beans和listView则会自动根据listView里的属性判断是否该加载代理的对象
	{
		// 筛选出listView对应的listViewDisplay.EntityPropertyNames中包含有‘.’的对象——因为包含有.才证明有级联属性，有级联属性才会有懒加载
		try {
			for (Object bean : beans) {
				for (ListViewDisplay listViewDisplay : listView
						.getFlexColumnNames()) {
					if (listViewDisplay.getIsCanDisplay() == null
							|| listViewDisplay.getIsCanDisplay() == false)
						continue;
					String temp = listViewDisplay.getEntityPropertyNames();
					if (listViewDisplay.getIsEntityPropertyRef() == true) {

						// 码表默认显示
						// value,所以显示字段的属性就是码表，例如：patient.patientSex，patientSex本身就需要加载
						if ("ddd.simple.entity.code.CodeTable"
								.equals(listViewDisplay.getFlexColumnName()
										.getDddReturnJavaType())) {
							initializeProperty(bean, temp);
						} else {
							initializeProperty(bean,
									temp.substring(0, temp.lastIndexOf(".")));
						}
					} else if (!("").equals(listViewDisplay.getFlexColumnName()
							.getDddAdditionalData())) {
						initializeProperty(bean,
								listViewDisplay.getEntityPropertyNames());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			DDDException dddException = new DDDException(
					"HibernateLazyInitialize.initializePropertys(Object bean)",
					"反射出错，找不到该视图级联对象的属性信息,原因：" + e.getMessage(), e);
			throw dddException;
		}
	}

}
*/