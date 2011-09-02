package org.eclipselabs.resourceselector.processor.javafields;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipselabs.resourceselector.core.resources.ResourceInfo;

/**
 * Simplest ResourceInfo Descriptor for Java Methods
 * @author mvanbesien
 *
 */
public class JavaFieldsResourceInfo extends ResourceInfo {

	private static Image METHOD_IMAGE;

	static {
		ImageDescriptor descriptor = AbstractUIPlugin
				.imageDescriptorFromPlugin("org.eclipse.jdt.ui", "/icons/full/obj16/searchm_obj.gif");
		METHOD_IMAGE = descriptor.createImage();
	}

	public JavaFieldsResourceInfo(String elementName, String packageName,
			String containerName) {
		super(elementName, packageName, containerName);
	}

	@Override
	public Image getImage() {
		return JavaFieldsResourceInfo.METHOD_IMAGE;
	}

}
