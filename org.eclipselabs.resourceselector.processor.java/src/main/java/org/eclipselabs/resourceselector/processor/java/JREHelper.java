/**
 * $Id: JREHelper.java,v 1.1 2008/06/10 12:52:16 maxence Exp $
 * 
 * HISTORY 
 * -------
 * $Log: JREHelper.java,v $
 * Revision 1.1  2008/06/10 12:52:16  maxence
 * First Draft
 *
 * Revision 1.1  2008/04/01 07:55:16  a125788
 * MVA : Replaced with Resource Selector 2
 *
 */
package org.eclipselabs.resourceselector.processor.java;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;

/**
 * Helper for resolving JRE names from JRE Library locations.
 * 
 * @author mvanbesien
 * @version $Revision: 1.1 $
 * @since $Date: 2008/06/10 12:52:16 $
 * 
 */
class JREHelper {

	/**
	 * Instance
	 */
	private static JREHelper instance;

	/**
	 * @return JRE Helper instance
	 */
	public static JREHelper getInstance() {
		if (JREHelper.instance == null)
			JREHelper.instance = new JREHelper();
		return JREHelper.instance;
	}

	/**
	 * Private Constructor
	 * 
	 */
	private JREHelper() {
		this.init();
	}

	/**
	 * Contains resolved names of libraries
	 */
	private Map<String, String> fLib2Name = new HashMap<String, String>();

	/**
	 * Contains resolved names of JREs
	 */
	private Map<String, String> jreNames = new HashMap<String, String>();

	/**
	 * Initializes this helper instance
	 * 
	 */
	private void init() {
		IVMInstallType[] installs = JavaRuntime.getVMInstallTypes();
		for (IVMInstallType element : installs)
			this.processVMInstallType(element);
	}

	/**
	 * Processes an Virtual Install Type
	 * 
	 * @param installType
	 *            : IVMInstallType
	 */
	private void processVMInstallType(IVMInstallType installType) {
		if (installType != null) {
			IVMInstall[] installs = installType.getVMInstalls();
			boolean isMac = Platform.OS_MACOSX.equals(Platform.getOS());
			final String HOME_SUFFIX = "/Home";
			for (IVMInstall element : installs) {
				String label = element.getName();
				LibraryLocation[] libLocations = element.getLibraryLocations();
				if (libLocations != null)
					this.processLibraryLocation(libLocations, label);
				else {
					String filePath = element.getInstallLocation().getAbsolutePath();
					// on MacOS X install locations end in an additional "/Home"
					// segment; remove it
					if (isMac && filePath.endsWith(HOME_SUFFIX))
						filePath = filePath.substring(0, filePath.length() - HOME_SUFFIX.length() + 1);
					this.jreNames.put(filePath, label);
				}
			}
		}
	}

	/**
	 * Process and resolves Library locations, and associate them with defined
	 * label.
	 * 
	 * @param libLocations
	 *            : Library Locations
	 * @param label
	 *            : Label
	 */
	private void processLibraryLocation(LibraryLocation[] libLocations, String label) {
		for (LibraryLocation location : libLocations)
			this.fLib2Name.put(location.getSystemLibraryPath().toString(), label);
	}

	/**
	 * Returns JRE name from Library path
	 * 
	 * @param path
	 * @return JRE Name
	 */
	public String getJREName(String path) {
		for (String key : this.jreNames.keySet())
			if (path.startsWith(key))
				return "[" + this.jreNames.get(key) + "]";
		return null;
	}
}
