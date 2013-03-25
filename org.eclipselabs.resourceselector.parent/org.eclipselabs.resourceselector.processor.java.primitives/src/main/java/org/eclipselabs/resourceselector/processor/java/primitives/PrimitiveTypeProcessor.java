package org.eclipselabs.resourceselector.processor.java.primitives;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipselabs.resourceselector.core.processor.ResourceProcessor;
import org.eclipselabs.resourceselector.core.resources.ResourceInfo;

/**
 * Type Processor extension for Primitive Types
 * 
 * @author mvanbesien
 * 
 */
public class PrimitiveTypeProcessor extends ResourceProcessor {

	/**
	 * Creates a new PrimitiveTypeProcessor with input parameters
	 * 
	 * @param project
	 *            : input project
	 * @param pattern
	 *            : input pattern
	 */
	public PrimitiveTypeProcessor(IProject project, String pattern) {
		this.iProject = project;
		this.pattern = pattern;
		if (PrimitiveTypeProcessor.primitiveTypeInfoList == null)
			this.loadPrimitiveTypesInfoList();
	}

	/**
	 * Fills the static list of Java Primitive Types
	 */
	private void loadPrimitiveTypesInfoList() {
		PrimitiveTypeProcessor.primitiveTypeInfoList = new PrimitiveTypeInfo[9];
		PrimitiveTypeProcessor.primitiveTypeInfoList[0] = new PrimitiveTypeInfo("void");
		PrimitiveTypeProcessor.primitiveTypeInfoList[1] = new PrimitiveTypeInfo("short");
		PrimitiveTypeProcessor.primitiveTypeInfoList[2] = new PrimitiveTypeInfo("int");
		PrimitiveTypeProcessor.primitiveTypeInfoList[3] = new PrimitiveTypeInfo("long");
		PrimitiveTypeProcessor.primitiveTypeInfoList[4] = new PrimitiveTypeInfo("float");
		PrimitiveTypeProcessor.primitiveTypeInfoList[5] = new PrimitiveTypeInfo("double");
		PrimitiveTypeProcessor.primitiveTypeInfoList[6] = new PrimitiveTypeInfo("char");
		PrimitiveTypeProcessor.primitiveTypeInfoList[7] = new PrimitiveTypeInfo("byte");
		PrimitiveTypeProcessor.primitiveTypeInfoList[8] = new PrimitiveTypeInfo("boolean");
	}

	/**
	 * Static list containing the Java Primitive Types
	 */
	private static PrimitiveTypeInfo[] primitiveTypeInfoList = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipselabs.resourceselector.core.processor.TypeProcessor#process()
	 */
	@Override
	protected void process() {
		this.searchResults = new ArrayList<ResourceInfo>();
		for (PrimitiveTypeInfo primitiveTypeInfo : PrimitiveTypeProcessor.primitiveTypeInfoList)
			if (primitiveTypeInfo.getElementName().startsWith(this.pattern))
				this.searchResults.add(primitiveTypeInfo);
	}
}
