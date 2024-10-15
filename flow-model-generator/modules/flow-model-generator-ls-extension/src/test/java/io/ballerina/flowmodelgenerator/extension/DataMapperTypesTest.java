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

package io.ballerina.flowmodelgenerator.extension;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.ballerina.flowmodelgenerator.extension.request.DataMapperTypesRequest;
import io.ballerina.flowmodelgenerator.extension.request.FlowModelSourceGeneratorRequest;
import org.eclipse.lsp4j.TextEdit;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests for the flow model source generator service.
 *
 * @since 1.4.0
 */
public class DataMapperTypesTest extends AbstractLSTest {

    private static final Type textEditListType = new TypeToken<Map<String, List<TextEdit>>>() {
    }.getType();

    @Override
    @Test(dataProvider = "data-provider")
    public void test(Path config) throws IOException {
        Path configJsonPath = configDir.resolve(config);
        TestConfig testConfig = gson.fromJson(Files.newBufferedReader(configJsonPath), TestConfig.class);

        DataMapperTypesRequest request =
                new DataMapperTypesRequest(sourceDir.resolve(testConfig.source()).toAbsolutePath().toString(),
                        testConfig.diagram());
        JsonObject jsonMap = getResponse(request).getAsJsonObject("types");

        Map<String, List<TextEdit>> actualTextEdits = gson.fromJson(jsonMap, textEditListType);

//        boolean assertFailure = false;
//        Map<String, List<TextEdit>> newMap = new HashMap<>();
//        for (Map.Entry<String, List<TextEdit>> entry : actualTextEdits.entrySet()) {
//            Path fullPath = Paths.get(entry.getKey());
//            String relativePath = sourceDir.relativize(fullPath).toString();
//
//            List<TextEdit> textEdits = testConfig.types().get(relativePath.replace("\\", "/"));
//            if (textEdits == null) {
//                log.info("No text edits found for the file: " + relativePath);
//                assertFailure = true;
//            } else if (!assertArray("text edits", entry.getValue(), textEdits)) {
//                assertFailure = true;
//            }
//
//            newMap.put(relativePath, entry.getValue());
//        }
//
//        if (assertFailure) {
//            TestConfig updatedConfig =
//                    new TestConfig(testConfig.source(), testConfig.description(), testConfig.diagram(), newMap);
////            updateConfig(configJsonPath, updatedConfig);
//            Assert.fail(String.format("Failed test: '%s' (%s)", testConfig.description(), configJsonPath));
//        }
    }

    @Override
    protected String getResourceDir() {
        return "data_mapper_types";
    }

    @Override
    protected Class<? extends AbstractLSTest> clazz() {
        return DataMapperTypesTest.class;
    }

    @Override
    protected String getApiName() {
        return "types";
    }

    @Override
    protected String getServiceName() {
        return "dataMapper";
    }

    /**
     * Represents the test configuration for the source generator test.
     *
     * @param source      The source file name
     * @param description The description of the test
     * @param diagram     The diagram to generate the source code
     * @param types      The expected types list
     */
    private record TestConfig(String source, String description, JsonElement diagram,
                              List<org.ballerinalang.diagramutil.connector.models.connector.Type> types) {

        public String description() {
            return description == null ? "" : description;
        }
    }
}
