package org.eclipselabs.resourceselector.processor.uml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * Helping class to manage models caching (as model loading is a very time &
 * memory consuming process.
 * 
 * In this case, model caching means that all the UMLTypeInfos associated to a
 * model will be kept in memory. The model itself is not really cached.
 * 
 * Model will be added to cache when loaded for the first time, and will be
 * invalidated when last modification date is later than the model caching date.
 * 
 * @author mvanbesien
 * 
 */
public class UMLModelsCache {

	/**
	 * Singleton holder
	 * 
	 * @author mvanbesien
	 */
	private static class UMLResourcesCacheHolder {
		static UMLModelsCache instance = new UMLModelsCache();
	}

	/**
	 * Private default constructor
	 */
	private UMLModelsCache() {
	}

	/**
	 * @return unique ModelCache instance
	 */
	public static UMLModelsCache getInstance() {
		return UMLResourcesCacheHolder.instance;
	}

	/**
	 * Inner class to describe a Cached Model
	 * 
	 * @author mvanbesien
	 * 
	 */
	private static class CachedResource {

		/**
		 * time when the resource was cached
		 */
		private long lastUpdateTimestamp;

		/**
		 * URL to the cached resource
		 */
		private String url;

		/**
		 * Collection of the UMLTypeInfos created from the resource
		 */
		private Collection<UMLTypeInfo> cachedElements = new ArrayList<UMLTypeInfo>();

	}

	/**
	 * Map of Cached Resources, referenced by URI
	 */
	public Map<String, CachedResource> cachedResources = new HashMap<String, CachedResource>();

	/**
	 * Returns Collection of UMLTypeInfos associated with a model file
	 * 
	 * @param iFile
	 *            : UML Model
	 * @param resourceSet
	 * @return Collection of UMLTypeInfo
	 */
	public Collection<UMLTypeInfo> getTypeInfosForModel(IFile iFile,
			ResourceSet resourceSet) {
		URI uri = URI.createPlatformResourceURI(iFile.getFullPath().toString(),
				true);
		String id = uri.toString();
		if (this.cachedResources.containsKey(id)) {
			CachedResource cachedResource = this.cachedResources.get(id);
			if (iFile.getModificationStamp() > cachedResource.lastUpdateTimestamp)
				return Collections.unmodifiableCollection(this.loadResource(
						uri, iFile.getModificationStamp(), resourceSet));
			else
				return Collections
						.unmodifiableCollection(cachedResource.cachedElements);
		} else
			return Collections.unmodifiableCollection(this.loadResource(uri,
					iFile.getModificationStamp(), resourceSet));
	}

	/**
	 * Loads an UML Model according to the URI passed as parameter
	 * 
	 * @param uri
	 *            : Model EMF URI
	 * @param stamp
	 *            : file last modification stamp
	 * @return Collection of created UMLTypeInfos
	 */
	private synchronized Collection<UMLTypeInfo> loadResource(URI uri,
			long stamp, ResourceSet resourceSet) {
		Resource resource = resourceSet.getResource(uri, true);
		if (!resource.isLoaded())
			try {
				resource.load(Collections.EMPTY_MAP);
			} catch (IOException e) {
				Activator.getDefault().logError(
						"Exception thrown while loading resource", e);
			}
		Collection<UMLTypeInfo> typeInfos = this.loadResource(resource, stamp);
		return typeInfos;
	}

	/**
	 * Extracts all the UMLTypesInfos from Resource, to store Resource and its
	 * TypeInfos in cache
	 * 
	 * @param resource
	 *            : EMF Resource
	 * @param stamp
	 *            : last modification stamp
	 * @return Collection of UMLTypesInfos
	 */
	private Collection<UMLTypeInfo> loadResource(Resource resource, long stamp) {
		Collection<UMLTypeInfo> typeInfos = new UMLResourceProcessor()
				.lookForTypesInResource(resource);
		CachedResource cachedResource = new CachedResource();
		cachedResource.cachedElements = typeInfos;
		cachedResource.url = resource.getURI().toString();
		cachedResource.lastUpdateTimestamp = stamp;
		this.cachedResources.put(cachedResource.url, cachedResource);
		return typeInfos;
	}

}
