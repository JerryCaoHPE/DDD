package ddd.base.dbmanager;

import ddd.base.dbmanager.tableHandler.mysql.MysqlTableHandler;
import ddd.base.persistence.SessionFactory;

public class TestDbManager {

	public static void main(String[] args) {
		SessionFactory.getEntityClasses();
		//TableGenerator.generator(new MysqlTableHander());
	}

}
