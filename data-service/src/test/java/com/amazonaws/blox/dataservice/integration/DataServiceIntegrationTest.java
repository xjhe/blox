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

import static org.assertj.core.api.Assertions.assertThat;

import com.amazonaws.blox.dataservicemodel.v1.model.EnvironmentId;
import com.amazonaws.blox.dataservicemodel.v1.model.wrappers.*;
import java.util.Arrays;
import org.junit.Test;

public class DataServiceIntegrationTest extends DataServiceIntegrationTestBase {
  private static final String ACCOUNT_ID = "123456789012";
  private static final String ENVIRONMENT_NAME_ONE = "environmentName1";
  private static final String ENVIRONMENT_NAME_TWO = "environmentName2";
  private static final String CLUSTER_ONE = "cluster1";
  private static final String CLUSTER_TWO = "cluster2";
  private static final String TASK_DEFINITION = "taskDefinition";
  private static final DataServiceModelBuilder models = DataServiceModelBuilder.builder().build();
  private static final EnvironmentId createdEnvironmentId1 =
      models
          .environmentId()
          .accountId(ACCOUNT_ID)
          .environmentName(ENVIRONMENT_NAME_ONE)
          .cluster(CLUSTER_ONE)
          .build();
  private static final EnvironmentId createdEnvironmentId2 =
      models
          .environmentId()
          .accountId(ACCOUNT_ID)
          .environmentName(ENVIRONMENT_NAME_ONE)
          .cluster(CLUSTER_TWO)
          .build();
  private static final EnvironmentId createdEnvironmentId3 =
      models
          .environmentId()
          .accountId(ACCOUNT_ID)
          .environmentName(ENVIRONMENT_NAME_TWO)
          .cluster(CLUSTER_ONE)
          .build();

  @Test
  public void testDescribeEnvironment() throws Exception {
    dataService.createEnvironment(
        models
            .createEnvironmentRequest()
            .taskDefinition(TASK_DEFINITION)
            .environmentId(createdEnvironmentId1)
            .build());
    final DescribeEnvironmentResponse describeEnvironmentResponse =
        dataService.describeEnvironment(
            models.describeEnvironmentRequest().environmentId(createdEnvironmentId1).build());
    assertThat(describeEnvironmentResponse.getEnvironment().getEnvironmentId())
        .isEqualTo(createdEnvironmentId1);
  }

  @Test
  public void testListClusterWithTwoEnvironments() throws Exception {
    dataService.createEnvironment(
        models
            .createEnvironmentRequest()
            .taskDefinition(TASK_DEFINITION)
            .environmentId(createdEnvironmentId1)
            .build());
    dataService.createEnvironment(
        models
            .createEnvironmentRequest()
            .taskDefinition(TASK_DEFINITION)
            .environmentId(createdEnvironmentId2)
            .build());
    final ListClustersResponse listClustersResponse =
        dataService.listClusters(
            models.listClustersRequest().accountId(ACCOUNT_ID).clusterNamePrefix(null).build());
    assertThat(listClustersResponse.getClusters())
        .isEqualTo(
            Arrays.asList(
                models.cluster().clusterName(CLUSTER_ONE).build(),
                models.cluster().clusterName(CLUSTER_TWO).build()));
  }

  @Test
  public void testListEnvironments() throws Exception {
    dataService.createEnvironment(
        models
            .createEnvironmentRequest()
            .taskDefinition(TASK_DEFINITION)
            .environmentId(createdEnvironmentId1)
            .build());
    dataService.createEnvironment(
        models
            .createEnvironmentRequest()
            .taskDefinition(TASK_DEFINITION)
            .environmentId(createdEnvironmentId3)
            .build());
    final ListEnvironmentsResponse listEnvironmentsResponse =
        dataService.listEnvironments(
            models
                .listEnvironmentsRequest()
                .cluster(models.cluster().clusterName(CLUSTER_ONE).build())
                .environmentNamePrefix(null)
                .build());
    assertThat(listEnvironmentsResponse.getEnvironmentIds())
        .isEqualTo(Arrays.asList(createdEnvironmentId1, createdEnvironmentId3));
  }

  @Test
  public void testDeleteEnvironment() throws Exception {
    dataService.createEnvironment(
        models
            .createEnvironmentRequest()
            .taskDefinition(TASK_DEFINITION)
            .environmentId(createdEnvironmentId1)
            .build());
    final DeleteEnvironmentResponse deleteEnvironmentResponse =
        dataService.deleteEnvironment(
            models.deleteEnvironmentRequest().environmentId(createdEnvironmentId1).build());

    assertThat(deleteEnvironmentResponse.getEnvironment().getEnvironmentId())
        .isEqualTo(createdEnvironmentId1);
  }
}
