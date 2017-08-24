package ddd.base.helper.velocityDirective;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;

import ddd.base.Config;
import ddd.base.util.FileUtil;
import ddd.base.util.VelocityUtil;

public class Invoke extends Directive{

	@Override
	public String getName() {
		return "invoke";
	}

	@Override
	public int getType() {
		return LINE;
	}

	@Override
	public boolean render(InternalContextAdapter ica, Writer writer, Node node)
			throws IOException, ResourceNotFoundException, ParseErrorException,
			MethodInvocationException {
		
		SimpleNode sn_path = (SimpleNode) node.jjtGetChild(0);
		Object path = sn_path.value(ica);
		if(path==null){
			path = sn_path.literal();
		}
		String stringTemplate = FileUtil.readeString(new File(Config.tomcatWebContentPath+"/"+path));
		
		VelocityContext velocityContext = new VelocityContext(ica);
		
		String result = VelocityUtil.generateString(stringTemplate, velocityContext);
		writer.write(result);
		return true;
	}

}
