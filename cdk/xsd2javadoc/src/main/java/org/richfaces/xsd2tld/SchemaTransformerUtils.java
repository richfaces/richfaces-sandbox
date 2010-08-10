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
package org.richfaces.xsd2tld;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.netbeans.modules.xml.axi.datatype.Datatype;
import org.netbeans.modules.xml.schema.model.Schema;
import org.netbeans.modules.xml.xam.dom.ReadOnlyAccess;
import org.netbeans.modules.xml.xam.spi.DocumentModelAccessProvider;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Nick Belaevski
 * 
 */
final class SchemaTransformerUtils {

    private static final String XHTML_DOCUMENT_PROLOG = "<div xmlns=\"http://www.w3.org/1999/xhtml\">";
    private static final String XHTML_DOCUMENT_EPILOG = "</div>";
    private static final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    private SchemaTransformerUtils() {
    }

    static String getJavaClassNameByType(Datatype datatype) {
        String result;

        // based on http://java.sun.com/javaee/5/docs/tutorial/doc/bnazq.html#bnazr
        switch (datatype.getKind()) {
            case STRING:
                result = String.class.getName();
                break;

            case INTEGER:
                result = BigInteger.class.getName();
                break;

            case INT:
            case UNSIGNED_SHORT:
                result = Integer.TYPE.getName();
                break;

            case LONG:
            case UNSIGNED_INT:
                result = Long.TYPE.getName();
                break;

            case SHORT:
            case UNSIGNED_BYTE:
                result = Short.TYPE.getName();
                break;

            case DECIMAL:
                result = BigDecimal.class.getName();
                break;

            case FLOAT:
                result = Float.TYPE.getName();
                break;

            case DOUBLE:
                result = Double.TYPE.getName();
                break;

            case BOOLEAN:
                result = Boolean.TYPE.getName();
                break;

            case BYTE:
                result = Byte.TYPE.getName();
                break;

            case QNAME:
                result = QName.class.getName();
                break;

            case DATE_TIME:
            case DATE:
            case TIME:
            case G_DAY:
            case G_MONTH_DAY:
            case G_YEAR:
            case G_YEAR_MONTH:
            case G_MONTH:
                result = XMLGregorianCalendar.class.getName();
                break;

            case BASE64_BINARY:
            case HEX_BINARY:
                result = Byte.TYPE.getName() + "[]";
                break;

            case DURATION:
                result = Duration.class.getName();
                break;

            case NOTATION:
                result = QName.class.getName();
                break;

            default:
                result = Object.class.getName();
                break;
        }

        return result;
    }

    static String getShortName(Schema schema) {
        String namespaceUri = schema.getTargetNamespace();

        return namespaceUri.replaceAll("\\W", "_");
    }

    static String capitalize(String s) {
        if (s.length() > 0) {
            char[] chars = s.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);

            return new String(chars);
        } else {
            throw new IllegalArgumentException(s);
        }
    }

    static String createTagClassName(String tagName) {
        return "org.richfaces." + capitalize(tagName) + "Tag";
    }

    private static void convertCDataIntoTextNodes(Node node) {
        for (Node childNode = node.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
            if (childNode.getNodeType() == Node.CDATA_SECTION_NODE) {
                org.w3c.dom.Document document = childNode.getOwnerDocument();
                Text textNode = document.createTextNode(childNode.getNodeValue());
                childNode.getParentNode().replaceChild(textNode, childNode);
            }

            convertCDataIntoTextNodes(childNode);
        }
    }

    static List<Object> parseRawXml(String xmlText) {
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document document = db.parse(new InputSource(new StringReader(XHTML_DOCUMENT_PROLOG + xmlText
                + XHTML_DOCUMENT_EPILOG)));

            org.w3c.dom.Element documentElement = document.getDocumentElement();
            convertCDataIntoTextNodes(documentElement);
            documentElement.normalize();

            List<Object> result = new ArrayList<Object>();
            NodeList childNodes = documentElement.getChildNodes();
            int childNodesLength = childNodes.getLength();
            for (int i = 0; i < childNodesLength; i++) {
                Node item = childNodes.item(i);
                switch (item.getNodeType()) {
                    case Node.TEXT_NODE:
                        result.add(item.getNodeValue());
                        break;

                    case Node.ELEMENT_NODE:
                        result.add(item);
                        break;

                    default:
                        // don't process
                        break;
                }
            }

            return result;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (SAXException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    static Lookup createLookup(String fileName) {
        DocumentModelAccessProvider provider = ReadOnlyAccess.Provider.getInstance();

        File file = new File(fileName);
        Document document = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            document = provider.loadSwingDocument(in);

            return Lookups.fixed(file, document, CatalogModelImpl.getInstance(), provider);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (BadLocationException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    static Map<String, String> parseLiteralHash(String hashString) {
        HashMap<String, String> result = new HashMap<String, String>();

        String[] splits = hashString.replaceAll("\\{|\\}", "").split(",");
        for (String split : splits) {
            String[] ss = split.split("=");
            result.put(ss[0], ss[1]);
        }

        return result;
    }

    static String normalizeXml(String rawXml) {
        StringWriter resultWriter = new StringWriter();
        List<Object> parsedXml = parseRawXml(rawXml);

        for (Object object : parsedXml) {
            if (object instanceof String) {
                resultWriter.write((String) object);
            } else if (object instanceof Node) {
                Node node = (Node) object;

                DOMImplementationLS lsImplementation = (DOMImplementationLS) node.getOwnerDocument()
                    .getImplementation().getFeature("LS", "3.0");

                LSOutput lsOutput = lsImplementation.createLSOutput();
                lsOutput.setCharacterStream(resultWriter);

                LSSerializer lsSerializer = lsImplementation.createLSSerializer();
                lsSerializer.getDomConfig().setParameter("xml-declaration", Boolean.FALSE);
                lsSerializer.write(node, lsOutput);
            } else {
                throw new IllegalArgumentException();
            }
        }

        return resultWriter.toString();
    }
}
