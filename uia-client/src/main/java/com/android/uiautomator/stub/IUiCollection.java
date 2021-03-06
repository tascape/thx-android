/*
 * Copyright 2015 - 2016 Nebula Bay.
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

/**
 *
 * @author linsong wang
 */
public interface IUiCollection extends IUiObject {

    void useUiCollectionSelector(UiSelector selector);

    /**
     * Searches for child UI element within the constraints of this UiCollection {@link UiSelector}
     * selector.
     *
     * It looks for any child matching the <code>childPattern</code> argument that has
     * a child UI element anywhere within its sub hierarchy that has content-description text.
     * The returned UiObject will point at the <code>childPattern</code> instance that matched the
     * search and not at the identifying child element that matched the content description.
     *
     * @param childPattern {@link UiSelector} selector of the child pattern to match and return
     * @param text         String of the identifying child contents of of the <code>childPattern</code>
     *
     * @return boolean
     *
     * @since API Level 16
     */
    boolean selectChildByDescription(UiSelector childPattern, String text);

    /**
     * Searches for child UI element within the constraints of this UiCollection {@link UiSelector}
     * selector.
     *
     * It looks for any child matching the <code>childPattern</code> argument that has
     * a child UI element anywhere within its sub hierarchy that is at the <code>instance</code>
     * specified. The operation is performed only on the visible items and no scrolling is performed
     * in this case.
     *
     * @param childPattern {@link UiSelector} selector of the child pattern to match and return
     * @param instance     int the desired matched instance of this <code>childPattern</code>
     *
     * @return boolean
     *
     * @since API Level 16
     */
    boolean selectChildByInstance(UiSelector childPattern, int instance);

    /**
     * Searches for child UI element within the constraints of this UiCollection {@link UiSelector}
     * selector.
     *
     * It looks for any child matching the <code>childPattern</code> argument that has
     * a child UI element anywhere within its sub hierarchy that has text attribute =
     * <code>text</code>. The returned UiObject will point at the <code>childPattern</code>
     * instance that matched the search and not at the identifying child element that matched the
     * text attribute.
     *
     * @param childPattern {@link UiSelector} selector of the child pattern to match and return
     * @param text         String of the identifying child contents of of the <code>childPattern</code>
     *
     * @return boolean
     *
     * @since API Level 16
     */
    boolean selectChildByText(UiSelector childPattern, String text);

    /**
     * Counts child UI element instances matching the <code>childPattern</code>
     * argument. The method returns the number of matching UI elements that are
     * currently visible. The count does not include items of a scrollable list
     * that are off-screen.
     *
     * @param childPattern a {@link UiSelector} that represents the matching child UI
     *                     elements to count
     *
     * @return the number of matched childPattern under the current {@link IUiCollection}
     *
     * @since API Level 16
     */
    int getChildCount(UiSelector childPattern);
}
