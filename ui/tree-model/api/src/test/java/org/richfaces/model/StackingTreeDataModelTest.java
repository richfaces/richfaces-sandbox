/**
 * 
 */
package org.richfaces.model;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.ajax4jsf.model.DataVisitor;
import org.richfaces.model.entity.Directory;
import org.richfaces.model.entity.File;
import org.richfaces.model.entity.Named;
import org.richfaces.model.entity.Project;
import org.richfaces.test.AbstractFacesTest;


/**
 * @author Nick Belaevski
 *         mailto:nbelaevski@exadel.com
 *         created 30.07.2007
 *
 */
public class StackingTreeDataModelTest extends AbstractFacesTest {

	private StackingTreeModel stackingTreeModel;
	private StackingTreeModel projectsModel;
	private StackingTreeModel directoriesModel;
	private StackingTreeModel filesModel;

	private Object projectRequestObject;
	private Object directoryRequestObject;
	private Object fileRequestObject;
	
	/* (non-Javadoc)
	 * @see org.ajax4jsf.tests.AbstractAjax4JsfTestCase#setUp()
	 */
	public void setUp() throws Exception {
		super.setUp();
		setupFacesRequest();
		this.stackingTreeModel = new StackingTreeModel();
		projectsModel = new StackingTreeModel("project", "project", new StackingTreeModelDataProvider() {

			private Map<String, Project> data = null;

			public Object getData() {
				if (data == null) {
					data = new LinkedHashMap<String, Project>();
					Project projectA = new Project("projectA", 10);

					Directory adir1 = new Directory("ADir1", 50);

					adir1.addFile(new File("AFile1", 60));
					adir1.addFile(new File("AFile2", 61));

					Directory adir2 = new Directory("ADir2", 101);

					projectA.addDirectory(adir1);
					projectA.addDirectory(adir2);

					data.put(projectA.getName(), projectA);

					Project projectB = new Project("projectB", 501);

					Directory bdir1 = new Directory("BDir1", 600);
					Directory bdir2 = new Directory("BDir2", 700);
					Directory bdir3 = new Directory("BDir3", 801);

					projectB.addDirectory(bdir1);
					projectB.addDirectory(bdir2);
					projectB.addDirectory(bdir3);

					data.put(projectB.getName(), projectB);
				}
				return data;
			}
		});
		final ValueBinding dirVB = application.createValueBinding("#{project.directories}");
		directoriesModel = new StackingTreeModel("directory", "directory", new StackingTreeModelDataProvider() {
			public Object getData() {
				return dirVB.getValue(facesContext);
			}
		});
		final ValueBinding fileVB = application.createValueBinding("#{directory.files}");
		filesModel = new StackingTreeModel("file", "file", new StackingTreeModelDataProvider() {
			public Object getData() {
				return fileVB.getValue(facesContext);
			}
		});
		directoriesModel.addStackingModel(filesModel);
		projectsModel.addStackingModel(directoriesModel);
		this.stackingTreeModel.addStackingModel(projectsModel);
		
		projectRequestObject = new Object();
		directoryRequestObject = new Object();
		fileRequestObject = new Object();
		
		Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
		requestMap.put("project", projectRequestObject);
		requestMap.put("directory", directoryRequestObject);
		requestMap.put("file", fileRequestObject);
	}

	/* (non-Javadoc)
	 * @see org.ajax4jsf.tests.AbstractAjax4JsfTestCase#tearDown()
	 */
	public void tearDown() throws Exception {
		super.tearDown();
		this.stackingTreeModel = null;
		this.projectsModel = null;
		this.directoriesModel = null;
		this.filesModel = null;
		
		this.projectRequestObject = null;
		this.directoryRequestObject = null;
		this.fileRequestObject = null;
	}

	public void testWalk() throws Exception {
		StackingTreeDataModelTestVisitor1 visitor1 = new StackingTreeDataModelTestVisitor1();
		this.stackingTreeModel.walk(facesContext, visitor1, null, null);
		assertEquals(9, visitor1.getCounter());
		this.stackingTreeModel.setRowKey(null);
		assertFalse(this.stackingTreeModel.isRowAvailable());

		Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
		assertSame(this.projectRequestObject, requestMap.get("project"));
		assertSame(this.directoryRequestObject, requestMap.get("directory"));
		assertSame(this.fileRequestObject, requestMap.get("file"));
	}

	public void testBadKey() throws Exception {
		StackingTreeDataModelTestVisitor1 visitor1 = new StackingTreeDataModelTestVisitor1();
		this.stackingTreeModel.walk(facesContext, visitor1, null, null);
		this.stackingTreeModel.setRowKey(new ListRowKey(new StackingTreeModelKey("project", "projectA")));
		assertTrue(this.stackingTreeModel.isRowAvailable());
		assertNotNull(this.stackingTreeModel.getRowData());
		
		assertNull(this.stackingTreeModel.getTreeNode());
		assertFalse(this.stackingTreeModel.isLeaf());
		
		this.stackingTreeModel.setRowKey(new ListRowKey(new StackingTreeModelKey("project", "projectAAAAA")));
		assertFalse(this.stackingTreeModel.isRowAvailable());
		try {
			Object rowData = this.stackingTreeModel.getRowData();
			assertNull(rowData);
			//FIXME: Maksim - bad keys happen for a reason
			//fail();
		} catch (Exception e) {

		}
		
		try {
			boolean leaf = this.stackingTreeModel.isLeaf();
			assertTrue(leaf);
			//FIXME: Maksim - bad keys happen for a reason
			//fail();
		} catch (Exception e) {

		}

		try {
			TreeNode node = this.stackingTreeModel.getTreeNode();
			assertNull(node);
			//FIXME: Maksim - bad keys happen for a reason
			//fail();
		} catch (Exception e) {

		}
	}
	
	public void testActiveData() throws Exception {
		final ValueBinding fileVB = application.createValueBinding("#{directory.files}");
		StackingTreeModel localFilesModel = new StackingTreeModel("file", "file", new StackingTreeModelDataProvider() {
			public Object getData() {
				return fileVB.getValue(facesContext);
			}
			
		}) {
			protected boolean isActiveData() {
				Map requestMap = facesContext.getExternalContext().getRequestMap();
				Object object = requestMap.get("file");
				assertNotNull(object);
				File file = (File) object;
				if (file.getTag() == 61) {
					return false;
				}
				
				return super.isActiveData();
			}
		};
		directoriesModel.removeStackingModel(filesModel);
		directoriesModel.addStackingModel(localFilesModel);
		
		stackingTreeModel.walk(facesContext, new StackingTreeDataModelTestVisitor3(), null, null);
	}
	
	public void testKey() throws Exception {
		StackingTreeModelKey key = new StackingTreeModelKey("aaa", new Integer(10));
		StackingTreeModelKey key2 = new StackingTreeModelKey("aaa", new Integer(11));
		StackingTreeModelKey key3 = new StackingTreeModelKey("aaa", new Integer(10));
		StackingTreeModelKey key4 = new StackingTreeModelKey("bbb", new Integer(10));
		
		assertFalse(key.equals(new StackingTreeModelKey("aaa", new Integer(0)) {} ));
		
		assertTrue(key.equals(key3));
		assertTrue(key3.equals(key));
		assertTrue(key2.equals(key2));
		assertTrue(key4.equals(key4));

		assertTrue(key.hashCode() == key3.hashCode());
		assertTrue(key3.hashCode() == key.hashCode());
		assertTrue(key2.hashCode() == key2.hashCode());
		assertTrue(key4.hashCode() == key4.hashCode());
		
		assertFalse(key.equals(key2));
		assertFalse(key3.equals(key2));
		assertFalse(key2.equals(key));
		assertFalse(key2.equals(key3));

		assertFalse(key.hashCode() == key2.hashCode());
		assertFalse(key3.hashCode() == key2.hashCode());
		assertFalse(key2.hashCode() == key.hashCode());
		assertFalse(key2.hashCode() == key3.hashCode());
		
		assertFalse(key4.equals(key));
		assertFalse(key4.equals(key2));
		assertFalse(key4.equals(key3));

		assertFalse(key4.hashCode() == key.hashCode());
		assertFalse(key4.hashCode() == key2.hashCode());
		assertFalse(key4.hashCode() == key3.hashCode());

		assertFalse(key.equals(key4));
		assertFalse(key2.equals(key4));
		assertFalse(key3.equals(key4));
		
		assertFalse(new StackingTreeModelKey("aaa", new Integer(10)).equals(null));
		assertFalse(new StackingTreeModelKey("aaa", null).equals(null));
		assertFalse(new StackingTreeModelKey(null, new Integer(10)).equals(null));
		assertFalse(new StackingTreeModelKey(null, null).equals(null));

		assertFalse(key.hashCode() == key4.hashCode());
		assertFalse(key2.hashCode() == key4.hashCode());
		assertFalse(key3.hashCode() == key4.hashCode());

		assertFalse(key.hashCode() == 0);
		assertFalse(key2.hashCode() == 0);
		assertFalse(key3.hashCode() == 0);
		assertFalse(key4.hashCode() == 0);
		
		assertTrue(new StackingTreeModelKey(null, new Integer(11)).equals(new StackingTreeModelKey(null, new Integer(11))));
		assertFalse(new StackingTreeModelKey(null, new Integer(10)).equals(new StackingTreeModelKey(null, new Integer(11))));
		assertFalse(new StackingTreeModelKey(null, new Integer(10)).equals(new StackingTreeModelKey("aaa", new Integer(10))));

		assertTrue(new StackingTreeModelKey(null, new Integer(11)).hashCode()  == new StackingTreeModelKey(null, new Integer(11)).hashCode());
		assertFalse(new StackingTreeModelKey(null, new Integer(10)).hashCode() == new StackingTreeModelKey(null, new Integer(11)).hashCode());
		assertFalse(new StackingTreeModelKey(null, new Integer(10)).hashCode() == new StackingTreeModelKey("aaa", new Integer(10)).hashCode());

		assertTrue(new StackingTreeModelKey("aaa", null).equals(new StackingTreeModelKey("aaa", null)));
		assertFalse(new StackingTreeModelKey("aaa", null).equals(new StackingTreeModelKey("bbb", null)));
		assertFalse(new StackingTreeModelKey("aaa", null).equals(new StackingTreeModelKey("aaa", new Integer(10))));

		assertTrue(new StackingTreeModelKey("aaa", null).hashCode() == new StackingTreeModelKey("aaa", null).hashCode());
		assertFalse(new StackingTreeModelKey("aaa", null).hashCode() == new StackingTreeModelKey("bbb", null).hashCode());
		assertFalse(new StackingTreeModelKey("aaa", null).hashCode() == new StackingTreeModelKey("aaa", new Integer(10)).hashCode());
	}
	
	class StackingTreeDataModelTestVisitor1 implements DataVisitor, LastElementAware {

		private boolean last;
		private int tag = 0;
		private int counter = 0;

		public void process(FacesContext context, Object rowKey, Object argument)
		throws IOException {

			StackingTreeDataModelTestVisitor2 visitor2 = new StackingTreeDataModelTestVisitor2();
			
			stackingTreeModel.walk(context, visitor2, null, new ListRowKey(new StackingTreeModelKey("project", "projectA")),
					argument, false);

			assertEquals(5, visitor2.getCounter());

			StackingTreeDataModelTestVisitor2 visitor20 = new StackingTreeDataModelTestVisitor2();
			
			stackingTreeModel.walk(context, visitor20, new TreeRange() {

				public boolean processChildren(TreeRowKey rowKey) {
					return false;
				}

				public boolean processNode(TreeRowKey rowKey) {
					return false;
				}
				
			}, new ListRowKey(new StackingTreeModelKey("project", "projectA")),
					argument, false);

			assertEquals(0, visitor20.getCounter());

			StackingTreeDataModelTestVisitor2 visitor21 = new StackingTreeDataModelTestVisitor2();
			
			stackingTreeModel.walk(context, visitor21, new TreeRange() {

				public boolean processChildren(TreeRowKey rowKey) {
					return false;
				}

				public boolean processNode(TreeRowKey rowKey) {
					return true;
				}
				
			}, new ListRowKey(new StackingTreeModelKey("project", "projectA")),
					argument, false);

			assertEquals(1, visitor21.getCounter());

			StackingTreeDataModelTestVisitor2 visitor22 = new StackingTreeDataModelTestVisitor2();
			
			stackingTreeModel.walk(context, visitor22, null, new ListRowKey(new StackingTreeModelKey("project", "projectB")),
					argument, false);

			assertEquals(4, visitor22.getCounter());
			
			StackingTreeDataModelTestVisitor2 visitor23 = new StackingTreeDataModelTestVisitor2();
			
			stackingTreeModel.walk(context, visitor23, new TreeRange() {

				private boolean rootProcessed = false;
				
				public boolean processChildren(TreeRowKey rowKey) {
					boolean result = rootProcessed;
					rootProcessed = true;
					return !result;
				}

				public boolean processNode(TreeRowKey rowKey) {
					return true;
				}
				
			}, new ListRowKey(new StackingTreeModelKey("project", "projectB")),
					argument, false);

			assertEquals(4, visitor23.getCounter());

			stackingTreeModel.setRowKey(rowKey);

			assertSame(rowKey, stackingTreeModel.getRowKey());

			assertNull(stackingTreeModel.getTreeNode());
			Object rowData = stackingTreeModel.getRowData();
			assertNotNull(rowData);
			assertTrue(rowData instanceof Named);

			Named named = (Named) rowData;

			int currentTag = named.getTag();
			assertTrue(currentTag > tag);
			this.tag = currentTag;

			if (this.tag % 10 == 1) {
				assertTrue(last);
			} else {
				assertFalse(last);
			}

			if (named instanceof Directory) {
				if ("ADir1".equals(named.getName())) {
					assertFalse(stackingTreeModel.isLeaf());
				} else {
					assertTrue(stackingTreeModel.isLeaf());
				}
			} else if (named instanceof Project) {
				assertFalse(stackingTreeModel.isLeaf());
			} else if (named instanceof File) {
				assertTrue(stackingTreeModel.isLeaf());
			} else {
				fail();
			}

			counter++;
		}

		public void resetLastElement() {
			this.last = false;
		}

		public void setLastElement() {
			this.last = true;
		}

		public int getCounter() {
			return counter;
		}
	}

	class StackingTreeDataModelTestVisitor2 implements DataVisitor {
		private int counter = 0;
		private int tag = 0;

		public void process(FacesContext context, Object rowKey, Object argument)
		throws IOException {

			stackingTreeModel.setRowKey(rowKey);
			Object rowData = stackingTreeModel.getRowData();
			assertNotNull(rowData);
			Named named = (Named) rowData;

			int currentTag = named.getTag();
			assertTrue(currentTag > tag);
			tag = currentTag;
			counter++;
		}

		public int getCounter() {
			return counter;
		}
	}
	
	class StackingTreeDataModelTestVisitor3 implements DataVisitor, LastElementAware {

		private boolean last;
		
		public void process(FacesContext context, Object rowKey, Object argument)
				throws IOException {

			stackingTreeModel.setRowKey(rowKey);
			Named named = (Named) stackingTreeModel.getRowData();
			int tag = named.getTag();
			
			if (tag == 60) {
				assertTrue(last);
			}
			assertFalse(tag == 61);
		}

		public void resetLastElement() {
			this.last = false;
		}

		public void setLastElement() {
			this.last = true;
		}
	}
}

