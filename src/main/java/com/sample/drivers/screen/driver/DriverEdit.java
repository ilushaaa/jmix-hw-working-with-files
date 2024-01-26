package com.sample.drivers.screen.driver;

import com.sample.drivers.entity.Document;
import io.jmix.ui.UiComponents;
import io.jmix.ui.action.BaseAction;
import io.jmix.ui.component.*;
import io.jmix.ui.component.data.ValueSource;
import io.jmix.ui.download.Downloader;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import com.sample.drivers.entity.Driver;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Driver.edit")
@UiDescriptor("driver-edit.xml")
@EditedEntityContainer("driverDc")
public class DriverEdit extends StandardEditor<Driver> {

    @Autowired
    private FileUploadField uploadPhoto;
    @Autowired
    private Image<byte[]> driverPhoto;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private MessageBundle messageBundle;
    @Autowired
    private Downloader downloader;

    @Subscribe(id = "driverDc", target = Target.DATA_CONTAINER)
    public void onDriverDcItemChange(final InstanceContainer.ItemChangeEvent<Driver> event) {
        Driver driver = event.getItem();
        updateUploadButtons(
                driver != null
                && driver.getPhoto() != null
                && driver.getPhoto().length > 0);
    }

    @Subscribe("uploadPhoto")
    public void onUploadPhotoValueChange(final HasValue.ValueChangeEvent<byte[]> event) {
        boolean hasValue = event.getValue() != null && event.getValue().length > 0;
        updateUploadButtons(hasValue);
    }

    private void updateUploadButtons(boolean hasItem) {
        if (!hasItem) {
            driverPhoto.setSource(ClasspathResource.class).setPath("icon/empty_photo.png");
        }
        uploadPhoto.setShowClearButton(hasItem);
        uploadPhoto.setUploadButtonCaption(hasItem
                ? messageBundle.getMessage("uploadFile.upload.caption")
                : messageBundle.getMessage("uploadFile.upload.emptyFile.caption"));
    }

    @Install(to = "documentsTable.file", subject = "columnGenerator")
    private Component tableDocumentTableFileColumnGenerator(Document document) {
        LinkButton fileLink = uiComponents.create(LinkButton.class);
        if (document == null || document.getFile() == null) {
            return null;
        }
        fileLink.setCaption(document.getFile().getFileName());
        fileLink.setAction(new BaseAction("download")
                .withCaption(document.getFile().getFileName())
                .withHandler(actionPerformedEvent -> downloader.download(document.getFile()))
        );
        return fileLink;
    }
}