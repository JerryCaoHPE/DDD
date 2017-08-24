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

public class ToDate extends Directive{

	@Override
	public String getName() {
		return "ToDate";
	}

	@Override
	public int getType() {
		return LINE;
	}

	@Override
	public boolean render(InternalContextAdapter ica, Writer writer, Node node)
			throws IOException, ResourceNotFoundException, ParseErrorException,
			MethodInvocationException {
		
		SimpleNode sn_date = (SimpleNode) node.jjtGetChild(0);
		Object dateString = sn_date.value(ica);
		if(dateString==null){
			dateString = sn_date.literal();
		}
		String out = SessionFactory.getSqlHandler().toDate(dateString.toString());
		writer.write(out);
		return true;
	}

}
