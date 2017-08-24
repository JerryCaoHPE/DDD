package ddd.base.dynamicSql.directive;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;

import ddd.base.persistence.SessionFactory;

public class Limit extends Directive{

	@Override
	public String getName() {
		return "Limit";
	}

	@Override
	public int getType() {
		return LINE;
	}

	@Override
	public boolean render(InternalContextAdapter ica, Writer writer, Node node)
			throws IOException, ResourceNotFoundException, ParseErrorException,
			MethodInvocationException {
		
		SimpleNode sn_0 = (SimpleNode) node.jjtGetChild(0);
		Object p0 = sn_0.value(ica);
		if(p0==null){
			p0=sn_0.literal();
		}
		
		SimpleNode sn_1 = (SimpleNode) node.jjtGetChild(1);
		Object p1 = sn_1.value(ica);
		if(p1==null){
			p1=sn_1.literal();
		}
		writer.write("_Limit("+p0+","+p1+")");
		return true;
	}

}
