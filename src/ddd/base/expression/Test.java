package ddd.base.expression;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ddd.base.expression.handler.TestHandler;

public class Test {

	public static void main(String[] args) {
		ExpressionService expressionService = new ExpressionService();
		expressionService.getBeans().put("测试", new TestHandler());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("用户名", "龚翔");
		params.put("时间", new Date() );
		params.put("ddd", "vvvv");
		
		expressionService.execute("${测试.哈哈(${用户名},${时间})}", params);
		expressionService.execute("${测试.哈哈2(龚翔,10,20)}");
		expressionService.execute("${测试}");
	}

}
