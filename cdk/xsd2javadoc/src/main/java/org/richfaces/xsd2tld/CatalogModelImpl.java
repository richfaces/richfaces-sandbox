package org.richfaces.xsd2tld;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.netbeans.modules.xml.xam.ModelSource;
import org.netbeans.modules.xml.xam.locator.CatalogModel;
import org.netbeans.modules.xml.xam.locator.CatalogModelException;
import org.netbeans.modules.xml.xam.spi.DocumentModelAccessProvider;
import org.openide.util.lookup.Lookups;
import org.w3c.dom.ls.LSInput;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Nick Belaevski
 * 
 */
final class CatalogModelImpl implements CatalogModel {

    private static CatalogModel instance;

    protected CatalogModelImpl() {
    }

    public static CatalogModel getInstance() {
        if (instance == null) {
            instance = new CatalogModelImpl();
        }
        return instance;
    }

    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
        // TODO Auto-generated method stub
        return null;
    }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public ModelSource getModelSource(URI locationURI, ModelSource modelSourceOfSourceDocument)
        throws CatalogModelException {
        DocumentModelAccessProvider accessProvider = modelSourceOfSourceDocument.getLookup().lookup(
            DocumentModelAccessProvider.class);
        if (accessProvider != null) {
            File file = (File) accessProvider.getModelSourceKey(modelSourceOfSourceDocument);
            File parentFile = file.getParentFile();
            File modelFile = new File(parentFile, locationURI.getPath());

            Document document;
            try {
                document = accessProvider.loadSwingDocument(new FileInputStream(modelFile));
                return new ModelSource(Lookups.fixed(document, modelFile, this, accessProvider), false);
            } catch (FileNotFoundException e) {
                throw new CatalogModelException(e);
            } catch (IOException e) {
                throw new CatalogModelException(e);
            } catch (BadLocationException e) {
                throw new CatalogModelException(e);
            }
        }

        return null;
    }

    public ModelSource getModelSource(URI locationURI) throws CatalogModelException {
        // TODO Auto-generated method stub
        return null;
    }
}