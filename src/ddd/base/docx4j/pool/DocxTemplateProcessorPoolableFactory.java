package ddd.base.docx4j.pool;

import java.lang.ref.SoftReference;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.PooledSoftReference;

import ddd.base.docx4j.DocxTemplateProcessor;



public class DocxTemplateProcessorPoolableFactory implements KeyedPooledObjectFactory<String, DocxTemplateProcessor> {
 
	//重新初始化实例返回池 
	@Override
	public void activateObject(String arg0, PooledObject<DocxTemplateProcessor> arg1) throws Exception {
		arg1.getObject().setActive(true);
		
	}

	//销毁被破坏的实例
	@Override
	public void destroyObject(String arg0, PooledObject<DocxTemplateProcessor> arg1) throws Exception {
		arg1 = null;
	}

	//创建一个实例到对象池
	@Override
	public PooledObject<DocxTemplateProcessor> makeObject(String key) throws Exception {
		//这里从数据库里查询出使用次数最少的配置
		DocxTemplateProcessor bo = new DocxTemplateProcessor(key);
		bo.setNum(0);
		PooledObject pooledObject = new PooledSoftReference(new SoftReference(bo));
		return pooledObject;
	}

	//取消初始化实例返回到空闲对象池
	@Override
	public void passivateObject(String arg0, PooledObject<DocxTemplateProcessor> arg1) throws Exception {
		arg1.getObject().setActive(false);
	}

	//验证该实例是否安全 true:正在使用
	@Override
	public boolean validateObject(String arg0, PooledObject<DocxTemplateProcessor> arg1) {
		//这里可以判断实例状态是否可用
		if(((DocxTemplateProcessor)arg1).isActive())
			return true;
		else
			return false;
	}
 
}