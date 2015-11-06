/*
 * Copyright 2015.
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
package tascape.qa.th.driver;

import com.android.uiautomator.stub.IUiCollection;
import com.android.uiautomator.stub.IUiDevice;
import com.android.uiautomator.stub.IUiObject;
import com.android.uiautomator.stub.IUiScrollable;
import com.android.uiautomator.stub.UiSelector;
import com.google.common.collect.Lists;
import com.tascape.qa.th.SystemConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.sf.lipermi.handler.CallHandler;
import net.sf.lipermi.net.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author linsong wang
 */
public class AndroidUiAutomatorDevice extends AndroidAdbDevice {
    private static final Logger LOG = LoggerFactory.getLogger(AndroidUiAutomatorDevice.class);

    public static final String SYSPROP_UIA_SERVER = "qa.th.comm.UIA_SERVER";

    public static final String SYSPROP_UIA_BUNDLE = "qa.th.comm.UIA_BUNDLE";

    public static final String UIA_SERVER = "uia-server.jar";

    public static final String UIA_BUNDLE = "bundle.jar";

    public static final long WAIT_FOR_EXISTS = 30000;

    static {
        LOG.debug("Please specify where uiautomator server jar is by setting system property {}={}",
            SYSPROP_UIA_SERVER, "/path/to/your/" + UIA_SERVER);
        LOG.debug("Please specify where third-party bundle jar is by setting system property {}={}",
            SYSPROP_UIA_BUNDLE, "/path/to/your/" + UIA_BUNDLE);
    }

    private final String ip = "localhost";

    private int port = IUiDevice.UIAUTOMATOR_RMI_PORT;

    private Client client;

    private IUiDevice uiDeviceStub;

    private IUiObject uiObjectStub;

    private IUiCollection uiCollectionStub;

    private IUiScrollable uiScrollableStub;

    private final String uiaServer = SystemConfiguration.getInstance().getProperty(SYSPROP_UIA_SERVER, UIA_SERVER);

    private final String uiaBundle = SystemConfiguration.getInstance().getProperty(SYSPROP_UIA_BUNDLE, UIA_BUNDLE);

    public AndroidUiAutomatorDevice(int port) throws IOException, InterruptedException {
        this.port = port;
    }

    public void init() throws IOException, InterruptedException {
        this.setupUiAutomatorRmiServer();
        this.adb.setupAdbPortForward(port, IUiDevice.UIAUTOMATOR_RMI_PORT);

        CallHandler callHandler = new CallHandler();
        this.client = new Client(this.ip, this.port, callHandler);
        this.uiDeviceStub = IUiDevice.class.cast(client.getGlobal(IUiDevice.class));
        this.uiObjectStub = IUiObject.class.cast(client.getGlobal(IUiObject.class));
        this.uiCollectionStub = IUiCollection.class.cast(client.getGlobal(IUiCollection.class));
        this.uiScrollableStub = IUiScrollable.class.cast(client.getGlobal(IUiScrollable.class));
        LOG.debug("Device product name '{}'", this.uiDeviceStub.getProductName());
    }

    @Override
    public String getName() {
        return AndroidUiAutomatorDevice.class.getSimpleName();
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException();
    }

    public IUiDevice getUiDeviceStub() {
        return uiDeviceStub;
    }

    public IUiObject getUiObjectStub() {
        return uiObjectStub;
    }

    public IUiCollection getUiCollectionStub() {
        return uiCollectionStub;
    }

    public IUiScrollable getUiScrollableStub() {
        return uiScrollableStub;
    }

    public void home() {
        LOG.debug("press home");
        uiDeviceStub.pressHome();
    }

    public void back() {
        LOG.debug("press back");
        uiDeviceStub.pressBack();
    }

    public void enter() {
        LOG.debug("press enter");
        uiDeviceStub.pressEnter();
    }

    public void backToHome() {
        for (int i = 0; i < 5; i++) {
            back();
        }
        home();
        home();
    }

    public File dumpWindowHierarchy() throws IOException {
        String f = "/data/local/tmp/uidump.xml";
        adb.shell(Lists.newArrayList("uiautomator", "dump", f));
        File xml = this.getLogPath().resolve("ui-" + System.currentTimeMillis() + ".xml").toFile();
        this.adb.pull(f, xml);
        LOG.debug("Save WindowHierarchy to {}", xml.getAbsolutePath());
        return xml;
    }

    public boolean resourceIdExists(String resouceId) {
        LOG.debug("look for {}", resouceId);
        uiObjectStub.useUiObjectSelector(new UiSelector().resourceId(resouceId));
        return uiObjectStub.exists();
    }

    public boolean textExists(String text) {
        LOG.debug("look for {}", text);
        uiObjectStub.useUiObjectSelector(new UiSelector().text(text));
        return uiObjectStub.exists();
    }

    public boolean waitForResourceId(String resouceId) {
        LOG.debug("wait for {}", resouceId);
        uiObjectStub.useUiObjectSelector(new UiSelector().resourceId(resouceId));
        uiObjectStub.waitForExists(WAIT_FOR_EXISTS);
        return uiObjectStub.exists();
    }

    public boolean waitForText(String text) {
        LOG.debug("wait for {}", text);
        uiObjectStub.useUiObjectSelector(new UiSelector().text(text));
        uiObjectStub.waitForExists(WAIT_FOR_EXISTS);
        return uiObjectStub.exists();
    }

    public boolean waitForTextContains(String text) {
        LOG.debug("wait for {}", text);
        uiObjectStub.useUiObjectSelector(new UiSelector().textContains(text));
        uiObjectStub.waitForExists(WAIT_FOR_EXISTS);
        return uiObjectStub.exists();
    }

    public void clickByResourceId(String resouceId) {
        LOG.debug("click {}", resouceId);
        uiObjectStub.useUiObjectSelector(new UiSelector().resourceId(resouceId));
        uiObjectStub.click();
        uiDeviceStub.waitForIdle();
    }

    public void clickByText(String text) {
        LOG.debug("click {}", text);
        uiObjectStub.useUiObjectSelector(new UiSelector().text(text));
        uiObjectStub.click();
        uiDeviceStub.waitForIdle();
    }

    public void clickByTextContains(String text) {
        LOG.debug("click {}", text);
        uiObjectStub.useUiObjectSelector(new UiSelector().textContains(text));
        uiObjectStub.click();
        uiDeviceStub.waitForIdle();
    }

    public void clearTextByResourceId(String resouceId) {
        uiObjectStub.useUiObjectSelector(new UiSelector().resourceId(resouceId));
        uiObjectStub.clearTextField();
        String text = uiObjectStub.getText();
        if (text.isEmpty()) {
            return;
        }
        uiObjectStub.clickBottomRight();
        for (int i = 0; i < text.length(); i++) {
            uiDeviceStub.pressDelete();
        }
    }

    public void setTextByResourceId(String resouceId, String text) {
        uiObjectStub.useUiObjectSelector(new UiSelector().resourceId(resouceId));
        clearTextByResourceId(resouceId);
        uiObjectStub.setText(text);
        this.back();
    }

    public String getTextByResourceId(String resouceId) {
        if (resourceIdExists(resouceId)) {
            uiObjectStub.useUiObjectSelector(new UiSelector().resourceId(resouceId));
            return uiObjectStub.getText();
        }
        return null;
    }

    public File takeDeviceScreenshot() throws IOException {
        String f = "/data/local/tmp/ff.png";
        this.uiDeviceStub.takeScreenshot(new File(f));
        File png = this.getLogPath().resolve("ss-" + System.currentTimeMillis() + ".png").toFile();
        this.adb.pull(f, png);
        LOG.debug("Save screenshot to {}", png.getAbsolutePath());
        return png;
    }

    private void setupUiAutomatorRmiServer() throws IOException, InterruptedException {
        List<Object> cmdLine = new ArrayList<>();
        cmdLine.add("push");
        cmdLine.add(uiaServer);
        cmdLine.add("/data/local/tmp/");
        adb.adb(cmdLine);

        cmdLine = new ArrayList<>();
        cmdLine.add("push");
        cmdLine.add(uiaBundle);
        cmdLine.add("/data/local/tmp/");
        adb.adb(cmdLine);

        cmdLine = new ArrayList();
        cmdLine.add("uiautomator");
        cmdLine.add("runtest");
        cmdLine.add(UIA_SERVER);
        cmdLine.add(UIA_BUNDLE);
        cmdLine.add("-c");
        cmdLine.add("com.android.uiautomator.stub.UiAutomatorRmiServer");
        this.adb.shellAsync(cmdLine, Long.MAX_VALUE);

        Thread.sleep(5000);
    }
}
