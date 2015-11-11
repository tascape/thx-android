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
package com.tascape.qa.th.android.demo;

import com.android.uiautomator.stub.IUiCollection;
import com.android.uiautomator.stub.IUiDevice;
import com.android.uiautomator.stub.IUiObject;
import com.android.uiautomator.stub.IUiScrollable;
import com.android.uiautomator.stub.Point;
import com.android.uiautomator.stub.Rect;
import com.android.uiautomator.stub.UiSelector;
import com.tascape.qa.th.android.comm.Adb;
import com.tascape.qa.th.android.driver.AndroidUiAutomatorDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author linsong wang
 */
public class UiAuotmatorRmiDemoTests {
    private static final Logger LOG = LoggerFactory.getLogger(UiAuotmatorRmiDemoTests.class);

    private AndroidUiAutomatorDevice uiad;

    private IUiDevice uiDeviceStub;

    private IUiObject uiObjectStub;

    private IUiCollection uiCollectionStub;

    private IUiScrollable uiScrollableStub;

    public void setup() throws Exception {
        Adb adb = new Adb();
        uiad = new AndroidUiAutomatorDevice(IUiDevice.UIAUTOMATOR_RMI_PORT);
        uiad.setAdb(adb);
        uiad.init();

        uiDeviceStub = uiad.getUiDevice();
        uiObjectStub = uiad.getUiObject();
        uiCollectionStub = uiad.getUiCollection();
        uiScrollableStub = uiad.getUiScrollable();
    }

    public void testUiDevice() throws Exception {
        uiDeviceStub.pressHome();
        uiDeviceStub.waitForIdle();
        uiDeviceStub.click(500, 500);

        LOG.debug(uiDeviceStub.getDisplayWidth() + "/" + uiDeviceStub.getDisplayHeight());
        Point p = uiDeviceStub.getDisplaySizeDp();
        LOG.debug(p.x + "/" + p.y);
        uiDeviceStub.swipe(100, 0, 100, 500, 2);
        LOG.debug(uiDeviceStub.getCurrentActivityName());

        uiDeviceStub.swipe(new Point[]{new Point(100, 500), new Point(100, 0)}, 2);
        uiDeviceStub.swipe(100, 500, 100, 0, 2);
        LOG.debug(uiDeviceStub.getCurrentActivityName());
    }

    public void testUiObject() throws Exception {
        uiDeviceStub.pressHome();
        uiDeviceStub.waitForIdle();

        for (String app : new String[]{"电子邮件", "设置", "应用商店"}) {
            LOG.debug(app);
            this.uiDeviceStub.pressHome();
            this.uiObjectStub.useUiObjectSelector(
                new UiSelector().resourceId("com.miui.home:id/cell_layout"));
            this.uiObjectStub.useUiObjectSelector(new UiSelector().text(app));
            Rect rect = this.uiObjectStub.getBounds();
            LOG.debug("{}", rect);
            this.uiObjectStub.swipeLeft(10);
            this.uiObjectStub.swipeRight(10);
            this.uiObjectStub.click();
            this.uiDeviceStub.waitForIdle();
        }
    }

    public void testUiObjectNegative() throws Exception {
        uiDeviceStub.pressHome();
        uiDeviceStub.waitForIdle();

        LOG.debug("Book");
        uiObjectStub.useUiObjectSelector(new UiSelector().text("Book"));
        uiObjectStub.click();
        LOG.debug("hasUiObjectNotFoundException = {}", uiObjectStub.hasUiObjectNotFoundException());
        LOG.debug("Exception!", uiObjectStub.getUiObjectNotFoundException());
        LOG.debug("hasUiObjectNotFoundException = {}", uiObjectStub.hasUiObjectNotFoundException());
    }

    public void testUiCollection() throws Exception {
        uiDeviceStub.pressHome();
        uiDeviceStub.waitForIdle();

        this.uiCollectionStub.useUiCollectionSelector(new UiSelector().resourceId(
            "com.amazon.kindle.otter:id/library_selector_layout"));
        int n = this.uiCollectionStub.getChildCount(new UiSelector().className("android.widget.Button"));
        LOG.debug("buttons {}", n);
        for (int i = 0; i < n; i++) {
            uiDeviceStub.pressHome();
            this.uiCollectionStub.selectChildByInstance(new UiSelector().className("android.widget.Button"), i);
            LOG.debug("text {}, rect {}", this.uiCollectionStub.getText(), this.uiCollectionStub.getBounds());
            this.uiCollectionStub.click();
            uiDeviceStub.waitForIdle();
        }
    }

    public void testUiCollection2() throws Exception {
        uiDeviceStub.pressHome();
        uiDeviceStub.waitForIdle();

        this.uiCollectionStub.useUiCollectionSelector(new UiSelector().resourceId(
            "com.amazon.kindle.otter:id/library_selector_layout"));
        this.uiCollectionStub.swipeLeft(100);

        int n = this.uiCollectionStub.getChildCount(new UiSelector().className("android.widget.Button"));
        LOG.debug("buttons {}", n);
        for (int i = 0; i < n; i++) {
            uiDeviceStub.pressHome();
            this.uiCollectionStub.selectChildByInstance(new UiSelector().className("android.widget.Button"), i);
            LOG.debug("text {}, rect {}", this.uiCollectionStub.getText(), this.uiCollectionStub.getBounds());
            this.uiCollectionStub.click();
            uiDeviceStub.waitForIdle();
        }
    }

    public void testUiScrollable() throws Exception {
        uiDeviceStub.pressHome();
        uiDeviceStub.waitForIdle();
        this.uiCollectionStub.useUiCollectionSelector(new UiSelector().resourceId(
            "com.amazon.kindle.otter:id/library_selector_layout"));
        this.uiCollectionStub.swipeRight(100);

        this.uiObjectStub.useUiObjectSelector(new UiSelector().text("Books"));
        this.uiObjectStub.click();
        this.uiScrollableStub.useUiScrollableSelector(new UiSelector().scrollable(true));
        this.uiScrollableStub.scrollToBeginning(100);
        this.uiScrollableStub.scrollForward(100);
        this.uiObjectStub.useUiObjectSelector(new UiSelector().descriptionStartsWith("The Blind Side"));
        this.uiObjectStub.click();
        this.uiDeviceStub.waitForIdle();
    }

    public static void main(String[] args) {
        try {
            while (true) {
                UiAuotmatorRmiDemoTests t = new UiAuotmatorRmiDemoTests();
                t.setup();
                t.testUiDevice();
                t.testUiObject();
                t.testUiObjectNegative();
                t.testUiCollection();
                t.testUiCollection2();
                t.testUiScrollable();
            }
        } catch (Exception ex) {
            LOG.error("", ex);
        } finally {
            System.exit(0);
        }
    }
}