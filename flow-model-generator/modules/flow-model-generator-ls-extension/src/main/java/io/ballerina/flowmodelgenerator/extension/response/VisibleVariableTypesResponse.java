/*
 *  Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com)
 *
 *  WSO2 LLC. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package io.ballerina.flowmodelgenerator.extension.response;

import io.ballerina.flowmodelgenerator.extension.Category;

import java.util.List;

/**
 * Represents the response containing visible variable types for the given cursor position.
 *
 * @since 1.4.0
 */
public class VisibleVariableTypesResponse extends AbstractFlowModelResponse {

    private List<Category> categories;

    public VisibleVariableTypesResponse() {

    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getVisibleTypes() {
        return categories;
    }
}
