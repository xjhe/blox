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

import static org.junit.Assert.assertEquals;

import com.amazonaws.blox.dataservicemodel.v1.exception.ResourceExistsException;
import com.amazonaws.blox.dataservicemodel.v1.model.EnvironmentId;
import com.amazonaws.blox.dataservicemodel.v1.model.wrappers.CreateEnvironmentResponse;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CreateEnvironmentIntegrationTest extends DataServiceIntegrationTestBase {
  private static final String ACCOUNT_ID = "123456789012";
  private static final String ENVIRONMENT_NAME = "environmentName";
  private static final String CLUSTER_ONE = "cluster1";
  private static final String CLUSTER_TWO = "cluster2";
  private static final String TASK_DEFINITION = "taskDefinition";

  DataServiceModelBuilder models = DataServiceModelBuilder.builder().build();
  @Rule public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testCreateEnvironmentSuccessful() throws Exception {
    final EnvironmentId createdEnvironmentId =
        models
            .environmentId()
            .accountId(ACCOUNT_ID)
            .environmentName(ENVIRONMENT_NAME)
            .cluster(CLUSTER_ONE)
            .build();
    // I create an environment
    final CreateEnvironmentResponse createEnvironmentResponse =
        dataService.createEnvironment(
            models
                .createEnvironmentRequest()
                .taskDefinition(TASK_DEFINITION)
                .environmentId(createdEnvironmentId)
                .build());

    // The environment is valid
    checkEnvironmentValid(ENVIRONMENT_NAME, ACCOUNT_ID, CLUSTER_ONE, createEnvironmentResponse);
  }

  @Test
  public void testCreateAnEnvironmentAlreadyExist() throws Exception {
    final EnvironmentId createdEnvironmentId =
        models
            .environmentId()
            .accountId(ACCOUNT_ID)
            .environmentName(ENVIRONMENT_NAME)
            .cluster(CLUSTER_ONE)
            .build();
    // I create an environment
    dataService.createEnvironment(
        models
            .createEnvironmentRequest()
            .taskDefinition(TASK_DEFINITION)
            .environmentId(createdEnvironmentId)
            .build());
    thrown.expect(ResourceExistsException.class);
    thrown.expectMessage(
        String.format("environment with id %s already exists", createdEnvironmentId));

    // I try to create another environment with the same name and cluster
    dataService.createEnvironment(
        models
            .createEnvironmentRequest()
            .taskDefinition(TASK_DEFINITION)
            .environmentId(createdEnvironmentId)
            .build());
  }

  @Test
  public void testCreateTwoEnvironmentsWithTheSameNameButDifferentClusters() throws Exception {
    final EnvironmentId createdEnvironmentId1 =
        models
            .environmentId()
            .accountId(ACCOUNT_ID)
            .environmentName(ENVIRONMENT_NAME)
            .cluster(CLUSTER_ONE)
            .build();
    // I create an environment
    final CreateEnvironmentResponse createEnvironmentResponse1 =
        dataService.createEnvironment(
            models
                .createEnvironmentRequest()
                .taskDefinition(TASK_DEFINITION)
                .environmentId(createdEnvironmentId1)
                .build());
    // The environment is valid
    checkEnvironmentValid(ENVIRONMENT_NAME, ACCOUNT_ID, CLUSTER_ONE, createEnvironmentResponse1);

    final EnvironmentId createdEnvironmentId2 =
        models
            .environmentId()
            .accountId(ACCOUNT_ID)
            .environmentName(ENVIRONMENT_NAME)
            .cluster(CLUSTER_TWO)
            .build();
    // I create anohter environment with the same name but different cluster
    final CreateEnvironmentResponse createEnvironmentResponse2 =
        dataService.createEnvironment(
            models
                .createEnvironmentRequest()
                .taskDefinition(TASK_DEFINITION)
                .environmentId(createdEnvironmentId2)
                .build());

    // The second environment is also valid
    checkEnvironmentValid(ENVIRONMENT_NAME, ACCOUNT_ID, CLUSTER_TWO, createEnvironmentResponse2);
  }

  private void checkEnvironmentValid(
      final String environmentName,
      final String accountId,
      final String cluster,
      final CreateEnvironmentResponse createEnvironmentResponse) {
    assertEquals(
        environmentName,
        createEnvironmentResponse.getEnvironment().getEnvironmentId().getEnvironmentName());
    assertEquals(
        accountId, createEnvironmentResponse.getEnvironment().getEnvironmentId().getAccountId());
    assertEquals(
        cluster, createEnvironmentResponse.getEnvironment().getEnvironmentId().getCluster());
  }
}
