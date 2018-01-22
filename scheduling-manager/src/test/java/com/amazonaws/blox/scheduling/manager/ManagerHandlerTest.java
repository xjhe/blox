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
package com.amazonaws.blox.scheduling.manager;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import com.amazonaws.blox.dataservicemodel.v1.client.DataService;
import com.amazonaws.blox.dataservicemodel.v1.model.Cluster;
import com.amazonaws.blox.dataservicemodel.v1.model.EnvironmentId;
import com.amazonaws.blox.dataservicemodel.v1.model.wrappers.ListEnvironmentsRequest;
import com.amazonaws.blox.dataservicemodel.v1.model.wrappers.ListEnvironmentsResponse;
import com.amazonaws.blox.lambda.LambdaFunction;
import com.amazonaws.blox.scheduling.scheduler.SchedulerInput;
import com.amazonaws.blox.scheduling.scheduler.SchedulerOutput;
import com.amazonaws.blox.scheduling.state.ECSState;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ManagerHandlerTest {
  public static final String ACCOUNT_ID = "12345678012";
  public static final String CLUSTER_NAME = "cluster1";
  public static final String FIRST_ENVIRONMENT_NAME = "environment1";
  public static final String SECOND_ENVIRONMENT_NAME = "environment2";

  private static final Cluster CLUSTER =
      Cluster.builder().accountId(ACCOUNT_ID).clusterName(CLUSTER_NAME).build();
  private static final EnvironmentId FIRST_ENVIRONMENT_ID =
      EnvironmentId.builder()
          .environmentName(FIRST_ENVIRONMENT_NAME)
          .accountId(ACCOUNT_ID)
          .cluster(CLUSTER_NAME)
          .build();
  private static final EnvironmentId SECOND_ENVIRONMENT_ID =
      EnvironmentId.builder()
          .environmentName(SECOND_ENVIRONMENT_NAME)
          .accountId(ACCOUNT_ID)
          .cluster(CLUSTER_NAME)
          .build();

  private ArgumentCaptor<SchedulerInput> schedulerArgument =
      ArgumentCaptor.forClass(SchedulerInput.class);
  @Mock private LambdaFunction<SchedulerInput, SchedulerOutput> scheduler;
  @Mock private ECSState ecs;
  @Mock private DataService dataService;

  @Test
  @SuppressWarnings("unchecked")
  public void invokesSchedulerForAllEnvironments() throws Exception {
    when(dataService.listEnvironments(ListEnvironmentsRequest.builder().cluster(CLUSTER).build()))
        .thenReturn(
            ListEnvironmentsResponse.builder()
                .environmentIds(Arrays.asList(FIRST_ENVIRONMENT_ID, SECOND_ENVIRONMENT_ID))
                .build());

    when(scheduler.callAsync(schedulerArgument.capture()))
        .thenReturn(
            CompletableFuture.completedFuture(
                new SchedulerOutput(CLUSTER_NAME, FIRST_ENVIRONMENT_ID, 1, 1)));

    ManagerHandler handler = new ManagerHandler(dataService, ecs, scheduler);
    handler.handleRequest(new ManagerInput(CLUSTER), null);

    assertThat(
        schedulerArgument.getAllValues(),
        contains(
            hasProperty("environmentId", is(FIRST_ENVIRONMENT_ID)),
            hasProperty("environmentId", is(SECOND_ENVIRONMENT_ID))));
  }
}
