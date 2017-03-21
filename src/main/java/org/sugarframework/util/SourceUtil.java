package org.sugarframework.util;

import static org.sugarframework.util.ClassUtils.getTargetClass;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.sugarframework.SugarException;
import org.sugarframework.component.ColumnWidth;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public final class SourceUtil {

	public static final void updateComponentOrder(String json, Object instance) {
		
		try {
			String sourcePath = CompilerUtil.findDebugSourcePath(getTargetClass(instance));

			String contents = new String(Files.readAllBytes(Paths.get(sourcePath)));

			JsonArray array = new JsonParser().parse(json).getAsJsonArray();

			for (int i = 0; i < array.size(); i++) {
				JsonObject element = array.get(i).getAsJsonObject();
				if(element.get("id") != null){
					String componentId = element.get("id").getAsString();
					int order = element.get("order").getAsInt();
	
					contents = updateOrderAnnotation(componentId, order, contents);
				}	
			}
			
			Files.write(Paths.get(sourcePath), contents.getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private static BodyDeclaration find(final String name, TypeDeclaration typeDecl){
		for(FieldDeclaration f : typeDecl.getFields()){
			
			for(Object frag : f.fragments()){
				if(frag instanceof VariableDeclarationFragment){
					String variableName = ((VariableDeclarationFragment) frag).getName().getFullyQualifiedName();
					if(name.equals(variableName)){
						return f;
					}
				}
			}
		}
		
		for(org.eclipse.jdt.core.dom.MethodDeclaration m : typeDecl.getMethods()){
			String methodName = m.getName().getFullyQualifiedName();
			if(name.equals(methodName)){
				return m;
			}
		}
		
		return null;
	}
	
	
	private static void createOrReplaceImport(String importStatement, CompilationUnit astRoot){
		
		
		List<ImportDeclaration> imports = astRoot.imports();
		
		for (ImportDeclaration i : imports){
			
			if(i.getName().getFullyQualifiedName().equals(importStatement)){
				return;
			}
		}
		
		ImportDeclaration importDeclaration = astRoot.getAST().newImportDeclaration();
		
		importDeclaration.setName(astRoot.getAST().newName(new String[] {"org", "sugarframework", "Order"}));
	
		astRoot.imports().add(importDeclaration);
		
	}
	
	public static String updateOrderAnnotation(final String fieldName, final int order, String contents) throws Exception {
			
		org.eclipse.jface.text.Document document = new org.eclipse.jface.text.Document(contents);

		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(document.get().toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		final CompilationUnit astRoot = (CompilationUnit) parser.createAST(null);
		ASTRewrite rewriter = ASTRewrite.create(astRoot.getAST());   
		astRoot.recordModifications();
		
		createOrReplaceImport("org.sugarframework.Order", astRoot);
		
	    TypeDeclaration typeDecl = (TypeDeclaration) astRoot.types().get(0);
	    BodyDeclaration target = find(fieldName, typeDecl);
	    
	  	Object toRemove = null;
	  	
	  	for(Object s : target.modifiers()){
	  		if(s instanceof Annotation){
	  			Annotation annotation = (Annotation) s;
	  			String name = annotation.getTypeName().getFullyQualifiedName();
	  			if(name.equals("Order")){
	  				toRemove = s;
	  			}
	  		}
	  	}
	  	if(toRemove != null){
	  		target.modifiers().remove(toRemove);
	  	}
	  		  	
	  	final ListRewrite listRewrite = rewriter.getListRewrite(astRoot, CompilationUnit.TYPES_PROPERTY);
	    
	  	SingleMemberAnnotation singleMemberAnnotation = astRoot.getAST().newSingleMemberAnnotation();
	  	singleMemberAnnotation.setTypeName(astRoot.getAST().newSimpleName("Order"));
	  	NumberLiteral number = astRoot.getAST().newNumberLiteral(String.valueOf(order));	  	
	  	singleMemberAnnotation.setValue(number);
		    
	  	target.modifiers().add(0, singleMemberAnnotation);
	  	
	  	listRewrite.replace(target, target, null);
	  	astRoot.rewrite(document, null).apply(document);
	  	 
		return document.get();

	}
	
	protected static MemberValuePair createQualifiedAnnotationMember(final AST ast, final String name, final String value, final String value2) {
	    final MemberValuePair mV = ast.newMemberValuePair();
	    mV.setName(ast.newSimpleName(name));
	    final TypeLiteral typeLiteral = ast.newTypeLiteral();
	    final QualifiedType newQualifiedName = ast.newQualifiedType(ast.newSimpleType(ast.newSimpleName(value)), ast.newSimpleName(value2));
	    typeLiteral.setType(newQualifiedName);
	    mV.setValue(typeLiteral);
	    return mV;
	}

	protected static MemberValuePair createAnnotationMember(final AST ast, final String name, final String value) {

	    final MemberValuePair mV = ast.newMemberValuePair();
	    mV.setName(ast.newSimpleName(name));
	    org.eclipse.jdt.core.dom.StringLiteral stringLiteral = ast.newStringLiteral();
	    stringLiteral.setLiteralValue(value);
	    mV.setValue(stringLiteral);
	    return mV;
	}
	
	protected static MemberValuePair createAnnotationMember2(final AST ast, final String name, final String value) {

	    final MemberValuePair mV = ast.newMemberValuePair();
	    mV.setName(ast.newSimpleName(name));
	    final TypeLiteral typeLiteral = ast.newTypeLiteral();
	    typeLiteral.setType(ast.newSimpleType(ast.newSimpleName(value)));
	    mV.setValue(typeLiteral);
	    return mV;
	}


	public static void replaceContainerAnnotation(String json, Object instance) {

		String replacement = "@Container(";

		JsonElement jelement = new JsonParser().parse(json);

		JsonArray rows = jelement.getAsJsonArray();
		if (rows.size() > 0) {
			replacement += "rows = {" + "\n";
		}

		for (int i = 0; i < rows.size(); i++) {
			JsonObject obj = rows.get(i).getAsJsonObject();
			String name = obj.get("name").getAsString();
			replacement += String.format("\t@Row(value=\"%s\"", name);
			JsonArray cols = obj.getAsJsonArray("columns");

			if (cols.size() > 0) {
				replacement += ", columns = {" + "\n";
			}

			for (int c = 0; c < cols.size(); c++) {
				JsonObject column = cols.get(c).getAsJsonObject();

				replacement += String.format("\t\t@Column(value=\"%s\", width=ColumnWidth.%s)", column.get("id")
						.getAsString(), ColumnWidth.find(column.get("class").getAsString()).getLiteralString());
				if (c < cols.size() - 1) {
					replacement += ",";
				}
				replacement += "\n";
			}

			if (cols.size() > 0) {
				replacement += "\t})";
			} else {
				replacement += ")";
			}

			if (i < rows.size() - 1) {
				replacement += ",";
			}
			replacement += "\n";
		}

		if (rows.size() > 0) {
			replacement += "\n})";
		} else {
			replacement += ")";
		}

		try {
			replaceContainer(getTargetClass(instance), replacement);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SugarException(e.getMessage(), e);
		}

	}

	public static void replaceContainer(Class<?> clazz, String replacement) throws Exception {

		String sourcePath = CompilerUtil.findDebugSourcePath(clazz);

		String contents = new String(Files.readAllBytes(Paths.get(sourcePath)));

		String result = replaceContainer(contents, replacement);

		Files.write(Paths.get(sourcePath), result.getBytes());
	}

	private static String replaceContainer(String contents, String replacement) {

		final String annotation = "@Container";

		if (!contents.contains(annotation)) {
			return contents;
		}

		String[] strs = contents.split(annotation);

		String restOfFile = strs[1];

		int open = 0;
		int closed = 0;

		String remove = "";

		for (char c : restOfFile.toCharArray()) {
			if (c == '(') {
				open++;
			} else if (c == ')') {
				closed++;
			}
			remove += c;

			if (open == closed) {
				break;
			}
		}

		return contents.replace(annotation + remove, replacement);
	}

	public static Set<String> estimateSourceDirectories(Class<?> contextClass, String... baseDirectories)
			throws IOException {
		Set<String> directories = new HashSet<>();

		URL main = contextClass.getResource(contextClass.getSimpleName() + ".class");

		if (!"file".equalsIgnoreCase(main.getProtocol())) {

			throw new IllegalStateException("Main class is not stored in a file.");
		}

		String s = new File(contextClass.getProtectionDomain().getCodeSource().getLocation().getPath()).toString();

		s += "/../";

		for (String postpend : baseDirectories) {

			String fullpath = new File(s + postpend).getCanonicalPath();
			Path path = Paths.get(s + postpend);

			if (Files.exists(path)) {

				String javaSource = contextClass.getName().replace(".", "/") + ".java";

				String sourceCheck = fullpath + "/" + javaSource;

				if (Files.exists(Paths.get(sourceCheck))) {
					directories.add(fullpath);
					return directories;
				}
			}
		}

		return directories;

	}

}
