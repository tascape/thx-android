#!/bin/bash

mvn clean install

java -cp uia-test/target/*:uia-test/target/dependency/* com.tascape.qa.th.android.tools.UiAutomatorViewer
