package org.eclipselabs.resourceselector.processor.java.hierarchy;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;

public class HierarchyScopesCache {

	private Map<IType, IJavaSearchScope> scopes = new HashMap<IType, IJavaSearchScope>();

	private static class HierarchyScopesCacheHolder {
		static HierarchyScopesCache instance = new HierarchyScopesCache();
	}

	private HierarchyScopesCache() {
	}

	public static HierarchyScopesCache getInstance() {
		return HierarchyScopesCacheHolder.instance;
	}

	public synchronized IJavaSearchScope getScope(IType type) throws JavaModelException {
		IJavaSearchScope scope = this.scopes.get(type);
		if (scope == null) {
			scope = SearchEngine.createHierarchyScope(type);
			this.scopes.put(type, scope);
		}
		return scope;
	}

	public void clear() {
		this.scopes.clear();
	}
}
