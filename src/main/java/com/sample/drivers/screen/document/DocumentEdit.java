package com.sample.drivers.screen.document;

import io.jmix.core.FileRef;
import io.jmix.ui.component.BrowserFrame;
import io.jmix.ui.component.FileStorageResource;
import io.jmix.ui.component.FileStorageUploadField;
import io.jmix.ui.component.HasValue;
import io.jmix.ui.screen.*;
import com.sample.drivers.entity.Document;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Document.edit")
@UiDescriptor("document-edit.xml")
@EditedEntityContainer("documentDc")
public class DocumentEdit extends StandardEditor<Document> {
    @Autowired
    private FileStorageUploadField documentUpload;
    @Autowired
    private MessageBundle messageBundle;
    @Autowired
    private BrowserFrame fileBrowse;

    @Subscribe("documentUpload")
    public void onDocumentUploadValueChange(final HasValue.ValueChangeEvent<FileRef> event) {

        boolean hasValue = event.getValue() != null;
        documentUpload.setShowClearButton(hasValue);
        documentUpload.setUploadButtonCaption(hasValue
                ? messageBundle.getMessage("uploadFile.upload.caption")
                : messageBundle.getMessage("uploadFile.upload.emptyFile.caption"));

        showFileInBrowseFrame();
    }

    private void showFileInBrowseFrame() {
        FileRef documentFileRef = getEditedEntity().getFile();
        fileBrowse.setVisible(documentFileRef != null);
        if (documentFileRef != null) {
            fileBrowse.setSource(FileStorageResource.class)
                    .setFileReference(documentFileRef)
                    .setMimeType(documentFileRef.getContentType());
        }
    }
}