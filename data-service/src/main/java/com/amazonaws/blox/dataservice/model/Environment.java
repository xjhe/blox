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
package com.amazonaws.blox.dataservice.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class Environment {

  @NonNull private String accountId;
  @NonNull private String name;
  @NonNull private String taskDefinitionArn;
  @NonNull private String roleArn;
  @NonNull private InstanceGroup instanceGroup;
  @NonNull private EnvironmentStatus status;
  @NonNull private EnvironmentHealth health;
  @NonNull private EnvironmentType type;
  @NonNull private DeploymentConfiguration deploymentConfiguration;
  @NonNull private Instant createdTime;
  @NonNull private Instant lastUpdatedTime;
}
