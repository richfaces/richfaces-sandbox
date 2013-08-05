package org.richfaces.sandbox.input.autocomplete;

import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.richfaces.deployment.Deployment;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

public class ComponentDeployment extends Deployment {

	private static Supplier<JavaArchive> COMPONENT_JAR = Suppliers.memoize(new Supplier<JavaArchive>() {
        @Override
        public JavaArchive get() {
            JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "this-component.jar");
            jar.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
                    .importDirectory("target/classes/").as(GenericArchive.class),
                    "/", Filters.includeAll());
            return jar;
        }
    });

	private static Supplier<JavaArchive> RICHFACES_JAR = Suppliers.memoize(new Supplier<JavaArchive>() {
        @Override
        public JavaArchive get() {
            JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "richfaces.jar");
            jar.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
                    .importDirectory("../../../richfaces5/framework/target/classes").as(GenericArchive.class),
                    "/", Filters.includeAll());
            return jar;
        }
    });

	protected ComponentDeployment(Class<?> testClass) {
		super(testClass);

		archive().addAsLibrary(COMPONENT_JAR.get());

		archive().addAsLibrary(RICHFACES_JAR.get());
	}

}
