/*
 * Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may
 * not use this file except in compliance with the License. A copy of the
 * License is located at
 *
 *     http://aws.amazon.com/apache2.0/
 *
 * or in the "LICENSE" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazonaws.blox.dataservice.integration;

import com.amazonaws.blox.dataservice.mapper.ApiModelMapper;
import com.amazonaws.blox.dataservice.mapper.EnvironmentMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataServiceIntegrationTestConfig {

  @Bean
  public EnvironmentMapper environmentMapper() {
    return Mappers.getMapper(EnvironmentMapper.class);
  }

  @Bean
  public ApiModelMapper apiModelMapper() {
    return Mappers.getMapper(ApiModelMapper.class);
  }
}
