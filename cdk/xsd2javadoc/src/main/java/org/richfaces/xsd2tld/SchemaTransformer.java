package org.richfaces.xsd2tld;

import static org.richfaces.xsd2tld.SchemaTransformerUtils.createLookup;
import static org.richfaces.xsd2tld.SchemaTransformerUtils.createTagClassName;
import static org.richfaces.xsd2tld.SchemaTransformerUtils.getJavaClassNameByType;
import static org.richfaces.xsd2tld.SchemaTransformerUtils.getShortName;
import static org.richfaces.xsd2tld.SchemaTransformerUtils.normalizeXml;
import static org.richfaces.xsd2tld.SchemaTransformerUtils.parseLiteralHash;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXB;

import org.netbeans.modules.xml.axi.AXIComponent;
import org.netbeans.modules.xml.axi.AXIModel;
import org.netbeans.modules.xml.axi.AXIModelFactory;
import org.netbeans.modules.xml.axi.AXIType;
import org.netbeans.modules.xml.axi.AbstractAttribute;
import org.netbeans.modules.xml.axi.Attribute;
import org.netbeans.modules.xml.axi.Element;
import org.netbeans.modules.xml.axi.datatype.Datatype;
import org.netbeans.modules.xml.axi.visitor.AXINonCyclicVisitor;
import org.netbeans.modules.xml.schema.model.Annotation;
import org.netbeans.modules.xml.schema.model.Documentation;
import org.netbeans.modules.xml.schema.model.Schema;
import org.netbeans.modules.xml.schema.model.SchemaComponent;
import org.netbeans.modules.xml.schema.model.SchemaModel;
import org.netbeans.modules.xml.schema.model.SchemaModelFactory;
import org.netbeans.modules.xml.schema.model.Attribute.Use;
import org.netbeans.modules.xml.xam.ModelSource;
import org.richfaces.taglib.model.ObjectFactory;
import org.richfaces.taglib.model.TagType;
import org.richfaces.taglib.model.TldAttributeType;
import org.richfaces.taglib.model.TldTaglibType;

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/**
 * @author Nick Belaevski
 * 
 */
public class SchemaTransformer {

    private static final ObjectFactory objectFactory = new ObjectFactory();

    private Map<String, TldTaglibType> taglibs = new HashMap<String, TldTaglibType>();

    private Map<String, String> taglibShortNames = new HashMap<String, String>();

    private SchemaModelFactory schemaModelFactory = SchemaModelFactory.getDefault();

    private List<SchemaModel> schemaModels = new ArrayList<SchemaModel>();

    private AXIModelFactory axiModelFactory = AXIModelFactory.getDefault();

    private TldTaglibType createTaglib(String namespaceUri) {
        TldTaglibType taglib = objectFactory.createTldTaglibType();

        taglibs.put(namespaceUri, taglib);
        taglib.setUri(namespaceUri);
        return taglib;
    }

    protected SchemaModel parseSchema(String fileName) throws Exception {
        ModelSource modelSource = new ModelSource(createLookup(fileName), false);

        SchemaModel schemaModel = schemaModelFactory.getModel(modelSource);
        schemaModel.sync();

        return schemaModel;
    }

    public void setTaglibShortNames(Map<String, String> taglibShortNames) {
        this.taglibShortNames = taglibShortNames;
    }

    public void addSchema(String fileName) throws Exception {
        SchemaModel schemaModel = parseSchema(fileName);
        schemaModels.add(schemaModel);
    }

    public void parseSchemas() {
        for (SchemaModel schemaModel : schemaModels) {
            Schema schema = schemaModel.getSchema();

            String targetNamespace = schema.getTargetNamespace();
            TldTaglibType taglib = createTaglib(targetNamespace);

            String shortName = taglibShortNames.get(targetNamespace);
            if (shortName == null) {
                shortName = getShortName(schema);
            }

            taglib.setShortName(shortName);

            Annotation schemaAnnotation = schema.getAnnotation();
            if (schemaAnnotation != null) {
                Collection<Documentation> documentationElements = schemaAnnotation.getDocumentationElements();
                if (documentationElements != null) {
                    List<Object> descriptionsList = taglib.getDescription();
                    for (Documentation documentationElement : documentationElements) {
                        // descriptionsList.addAll(parseRawXml(documentationElement.getContentFragment()));
                        descriptionsList.add(documentationElement.getContentFragment());
                    }
                }
            }

            addElementsAndAttributes(schemaModel, taglib);
        }

        schemaModels.clear();
    }

    public void writeTransformedSchemas(String outputDirectory) {
        File outputDirectoryObject = new File(outputDirectory);
        outputDirectoryObject.mkdirs();

        for (TldTaglibType taglib : taglibs.values()) {
            File outputFile = new File(outputDirectoryObject, taglib.getShortName() + ".tld");
            outputFile.delete();
            JAXB.marshal(objectFactory.createTaglib(taglib), outputFile);
        }
    }

    private static String getDocumentation(AXIComponent axiComponent) {
        SchemaComponent peer = axiComponent.getPeer();
        if (peer == null || peer.getAnnotation() == null || peer.getAnnotation().getDocumentationElements() == null) {
            return null;
        }

        StringBuilder buffer = new StringBuilder();
        for (Documentation doc : peer.getAnnotation().getDocumentationElements()) {
            buffer.append(doc.getContentFragment());
        }

        return buffer.toString();
    }

    private String getElementQName(AXIComponent component) {
        return "{" + component.getTargetNamespace() + "}" + ((AXIType) component).getName();
    }

    /**
     * @param schema
     * @param taglib
     */
    private void addElementsAndAttributes(SchemaModel schemaModel, final TldTaglibType taglib) {
        final String targetNamespace = schemaModel.getSchema().getTargetNamespace();

        AXIModel axiModel = axiModelFactory.getModel(schemaModel);

        final Set<String> visitedObjects = new HashSet<String>();

        axiModel.getRoot().accept(new AXINonCyclicVisitor(axiModel) {

            private void addAttribute(TagType tagType, AbstractAttribute abstractAttribute) {
                TldAttributeType tldAttribute = objectFactory.createTldAttributeType();

                // TODO namespace check
                tldAttribute.setName(abstractAttribute.getName());

                if (abstractAttribute instanceof Attribute) {
                    Attribute attribute = (Attribute) abstractAttribute;
                    tldAttribute.setRequired(Use.REQUIRED.equals(attribute.getUse()));
                    tldAttribute.setType(getJavaClassNameByType((Datatype) attribute.getType()));
                } else {
                    // TODO
                }

                String attributeDocumentation = getDocumentation(abstractAttribute);
                if (attributeDocumentation != null && attributeDocumentation.length() != 0) {
                    // tldAttribute.getDescription().addAll(parseRawXml(attributeDocumentation));
                    tldAttribute.getDescription().add(normalizeXml(attributeDocumentation));
                }

                tagType.getAttribute().add(tldAttribute);
            }

            private static final String GLOBAL_ATTRIBUTES = "_global_attributes";

            @Override
            public void visit(Attribute attribute) {
                super.visit(attribute);

                if (!attribute.isReference() && targetNamespace.equals(attribute.getTargetNamespace())
                    && attribute.isGlobal() && visitedObjects.add(getElementQName(attribute))) {
                    TagType globalTagType = null;
                    List<TagType> tags = taglib.getTag();
                    for (TagType tagType : tags) {
                        if (GLOBAL_ATTRIBUTES.equals(tagType.getName())) {
                            globalTagType = tagType;
                            break;
                        }
                    }

                    if (globalTagType == null) {
                        globalTagType = objectFactory.createTagType();
                        globalTagType.setName(GLOBAL_ATTRIBUTES);
                        globalTagType.getDescription().add(
                            "Global attributes of " + attribute.getTargetNamespace() + " namespace");

                        taglib.getTag().add(globalTagType);
                    }

                    addAttribute(globalTagType, attribute);
                }
            }

            @Override
            public void visit(Element e) {
                super.visit(e);

                if (!e.isReference() && targetNamespace.equals(e.getTargetNamespace())
                    && visitedObjects.add(getElementQName(e))) {
                    List<TagType> tagsList = taglib.getTag();

                    TagType tagType = objectFactory.createTagType();

                    String tagName = e.getName();
                    tagType.setName(tagName);
                    tagType.setTagClass(createTagClassName(tagName));

                    String documentation = getDocumentation(e);
                    if (documentation != null && documentation.length() != 0) {
                        // tagType.getDescription().addAll(parseRawXml(documentation));
                        tagType.getDescription().add(normalizeXml(documentation));
                    }

                    tagsList.add(tagType);

                    List<AbstractAttribute> abstractAttributes = e.getAttributes();
                    if (abstractAttributes != null) {
                        for (AbstractAttribute abstractAttribute : abstractAttributes) {
                            addAttribute(tagType, abstractAttribute);
                        }
                    }
                }
            }
        });
    }

    private static enum State {
        FILES, LOCAL_NAMES_HASH, OUTPUT_DIR
    }

    public static void main(String[] args) throws Exception {
        State state = State.FILES;

        String outputDirectoryPath = null;
        SchemaTransformer schemaTransformer = new SchemaTransformer();

        for (String arg : args) {
            if (state == State.LOCAL_NAMES_HASH) {
                schemaTransformer.setTaglibShortNames(parseLiteralHash(arg));
                state = State.FILES;
            } else if (state == State.OUTPUT_DIR) {
                outputDirectoryPath = arg;
                state = State.FILES;
            } else if (arg.equals("-h")) {
                state = State.LOCAL_NAMES_HASH;
            } else if (arg.equals("-o")) {
                state = State.OUTPUT_DIR;
            } else {
                schemaTransformer.addSchema(arg);
            }
        }

        schemaTransformer.parseSchemas();

        File outputDirectory = new File(outputDirectoryPath);

        schemaTransformer.writeTransformedSchemas(outputDirectory.getAbsolutePath());
    }
}
