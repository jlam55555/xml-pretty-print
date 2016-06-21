import java.io.BufferedWriter;
import java.nio.file.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.*;

public class XmlPrettyPrint {
	public static String indents(int indent) {
		String indents = "";
		for(int i = 0; i < indent; i++)
			indents += "  ";
		return indents;
	}
	public static void main(String[] args) throws Exception {
		System.out.println("Reading File...");
		List<String> lines = Files.readAllLines(Paths.get(args[0]), Charset.forName("UTF-8"));
		String file = "";
		for(String line : lines)
			file += line;
		file = file.replaceAll("\\s+", " ");
		System.out.println("Finding Tags...");
		Pattern tags = Pattern.compile("(<[^><]+?>)([^><]+?</[^><]+?>)??");
		Matcher tagMatcher = tags.matcher(file);
		String newFile = "";
		int indent = 0;
		while(tagMatcher.find()) {
			String group = tagMatcher.group(1), group1 = tagMatcher.group(2);
			if(group.indexOf("/") != 1) {
				boolean hasContent = group1 != null && group.indexOf("/") == -1;
				newFile += indents(indent) + group + (hasContent ? group1 : "") + "\n";
				if(group.indexOf("/") == -1 && group.indexOf("!") == -1 && group.indexOf("?") == -1)
					indent++;
				if(hasContent)
					indent--;
			} else
				newFile += indents(--indent) + group + "\n";
		}
		System.out.println("Writing File...");
		BufferedWriter bf = Files.newBufferedWriter(Paths.get(args[1]), Charset.forName("UTF-8"));
		bf.write(newFile, 0, newFile.length());
		bf.close();
	}
}
