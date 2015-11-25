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
package com.android.uiautomator.stub;

import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import java.io.IOException;
import net.sf.lipermi.exception.LipeRMIException;
import net.sf.lipermi.handler.CallHandler;
import net.sf.lipermi.net.Server;

/**
 * cd project-folder
 * /android-sdk/tools/android create uitest-project -n uia-server -t 1 -p .
 * ant build
 * adb push bin/uia-server.jar /data/local/tmp/
 * adb push bin/bundle.jar /data/local/tmp/
 * adb shell uiautomator runtest uia-server.jar bundle.jar -c com.android.uiautomator.stub.UiAutomatorRmiServer
 *
 * adb forward --remove tcp:local_port
 * adb forward tcp:local_port tcp:8998
 *
 * @author linsong wang
 */
public class UiAutomatorRmiServer extends UiAutomatorTestCase {
    private static final CallHandler CALL_HANDLER = new CallHandler();

    static {
        Server server = new Server();
        try {
            server.bind(IUiDevice.UIAUTOMATOR_RMI_PORT, CALL_HANDLER);

            CALL_HANDLER.registerGlobal(IUiDevice.class, new UiDeviceStub());
            CALL_HANDLER.registerGlobal(IUiObject.class, new UiObjectStub());
            CALL_HANDLER.registerGlobal(IUiCollection.class, new UiCollectionStub());
            CALL_HANDLER.registerGlobal(IUiScrollable.class, new UiScrollableStub());
        } catch (IOException | LipeRMIException e) {
            throw new RuntimeException(e);
        }
    }

    public void testRmiServer() throws Exception {
        while (true) {
            System.out.println("UiAutomator RMI Server is running");
            Thread.sleep(60000);
        }
    }
}
