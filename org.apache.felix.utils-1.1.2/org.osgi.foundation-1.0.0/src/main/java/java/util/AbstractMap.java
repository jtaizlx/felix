/*
 * $Header: /cvshome/build/ee.foundation/src/java/util/AbstractMap.java,v 1.6 2006/03/14 01:20:26 hargrave Exp $
 *
 * (C) Copyright 2001 Sun Microsystems, Inc.
 * Copyright (c) OSGi Alliance (2001, 2005). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package java.util;
public abstract class AbstractMap implements java.util.Map {
	protected AbstractMap() { }
	public void clear() { }
	public boolean containsKey(java.lang.Object var0) { return false; }
	public boolean containsValue(java.lang.Object var0) { return false; }
	public abstract java.util.Set entrySet();
	public boolean equals(java.lang.Object var0) { return false; }
	public java.lang.Object get(java.lang.Object var0) { return null; }
	public int hashCode() { return 0; }
	public boolean isEmpty() { return false; }
	public java.util.Set keySet() { return null; }
	public java.lang.Object put(java.lang.Object var0, java.lang.Object var1) { return null; }
	public void putAll(java.util.Map var0) { }
	public java.lang.Object remove(java.lang.Object var0) { return null; }
	public int size() { return 0; }
	public java.lang.String toString() { return null; }
	public java.util.Collection values() { return null; }
}

