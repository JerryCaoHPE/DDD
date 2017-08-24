package ddd.develop;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ddd.base.Config;
import ddd.base.util.FileUtil;

public class JSreplace {

	public static void main(String[] args) throws IOException {
		start();
	}

	public static void start() throws IOException {
		//从main.js中读取需要替换的部分
		String mainJSOfString = null;
		//要替换到main.js的字符串
		String lastString = "";

		Pattern pattern = null;
		Matcher matcher = null;

		String sourcePath = Config.applicationPath
				+ "\\WebContent\\ddd\\js\\base\\main.js";
		String file = FileUtil.readeString(new File(sourcePath));

		pattern = Pattern.compile("paths:[\\s\\S]*?},");
		matcher = pattern.matcher(file);

		while (matcher.find()) {
			mainJSOfString = matcher.group();
		}

		String[] str = mainJSOfString.split("\\n");
		boolean replace = judueIsMin(str[1]);

		for (int i = 0; i < str.length; i++) {
			if (!replace) {

				str[i] = str[i].replace(".min", "");

				str[i] = str[i].replace("',", ".min',");

				if (isFormat(str[i])) {

					String flag = str[i].split(":")[1].split("'")[1];

					flag = flag.replace("/", "\\");

					String jsPath = Config.applicationPath
							+ "WebContent\\ddd\\" + flag + ".js";

					File jsFile = new File(jsPath);
					if (!jsFile.exists()) {
						str[i] = str[i].replace(".min", "");
					}
				}
				
			} else {
				str[i] = str[i].replace(".min", "");
			
			}
			lastString += str[i] + "\n";

		}
		if(replace){
			System.out.println("转换为非压缩文件,调试状态（非.min.js）");
		}else{
			System.out.println("转换为压缩文件,运行状态（.min.js）");
		}
		file = matcher.replaceAll(lastString);
		FileUtil.writeToFile(new File(sourcePath), file);

	}

	public static boolean judueIsMin(String str) {
		Pattern pattern = Pattern.compile("[\\s\\S]*?\\.min[\\s\\S]*");
		Matcher match = pattern.matcher(str);
		return match.matches();
	}

	public static boolean isFormat(String str) {
		Pattern pattern = Pattern.compile("[\\s\\S]*?'[\\s\\S]*?':[\\s\\S]*");
		Matcher match = pattern.matcher(str);
		return match.matches();
	}
}
