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
package steps.dataservice;

import com.amazonaws.blox.dataservicemodel.v1.model.Environment;
import com.amazonaws.blox.dataservicemodel.v1.model.EnvironmentId;
import com.amazonaws.blox.dataservicemodel.v1.model.EnvironmentType;
import com.amazonaws.blox.dataservicemodel.v1.model.InstanceGroup;
import com.amazonaws.blox.dataservicemodel.v1.model.wrappers.CreateEnvironmentRequest;
import com.amazonaws.blox.dataservicemodel.v1.model.wrappers.CreateEnvironmentResponse;
import com.amazonaws.blox.dataservicemodel.v1.model.wrappers.DescribeEnvironmentRequest;
import com.amazonaws.blox.dataservicemodel.v1.model.wrappers.DescribeEnvironmentResponse;
import com.amazonaws.blox.dataservicemodel.v1.model.wrappers.UpdateEnvironmentRequest;
import com.amazonaws.blox.dataservicemodel.v1.model.wrappers.UpdateEnvironmentResponse;
import configuration.CucumberConfiguration;
import cucumber.api.java8.En;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import steps.wrappers.DataServiceWrapper;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

@ContextConfiguration(classes = CucumberConfiguration.class)
public class DescribeEnvironmentSteps implements En {

  @Autowired private DataServiceWrapper dataServiceWrapper;
  private static final String ACCOUNT_ID = "accountID";
  private static final String TASK_DEFINITION_ARN =
            "arn:aws:ecs:us-east-1:" + ACCOUNT_ID + ":task-definition/sleep";
  private static final String ROLE_ARN = "arn:aws:iam::" + ACCOUNT_ID + ":role/testRole";
  private static final String CLUSTER_NAME_PREFIX = "cluster";
  private static final String ENVIRONMENT_VERSION = "version";
  private String postfix = null;

  public DescribeEnvironmentSteps() {
      Given("^I create an environment named \"([^\"]*)\"$", (final String environmentNamePrefix) -> {
          this.postfix = generatePostfix();
          dataServiceWrapper.createEnvironment(createEnvironmentRequest(environmentNamePrefix));
      });

      When("^I describe the environment named \"([^\"]*)\"$", (final String environmentNamePrefix) -> {
          dataServiceWrapper.describeEnvironment(describeEnvironmentRequest(environmentNamePrefix));
      });

      Then("^the created and described environments match$", () -> {
          final CreateEnvironmentResponse createEnvironmentResponse = dataServiceWrapper.getLastFromHistory(CreateEnvironmentResponse.class);
          final DescribeEnvironmentResponse describeEnvironmentResponse = dataServiceWrapper.getLastFromHistory(DescribeEnvironmentResponse.class);
          checkEnvironmentEquality(createEnvironmentResponse.getEnvironment(), describeEnvironmentResponse.getEnvironment());
      });

      Given("^I update the environment named \"([^\"]*)\"$", (final String environmentNamePrefix) -> {
          dataServiceWrapper.updateEnvironment(updateEnvironmentRequest(environmentNamePrefix));
      });

      Then("^the updated and described environments match$", () -> {
          final UpdateEnvironmentResponse updateEnvironmentResponse = dataServiceWrapper.getLastFromHistory(UpdateEnvironmentResponse.class);
          final DescribeEnvironmentResponse describeEnvironmentResponse = dataServiceWrapper.getLastFromHistory(DescribeEnvironmentResponse.class);
          checkEnvironmentEquality(updateEnvironmentResponse.getEnvironment(), describeEnvironmentResponse.getEnvironment());
      });

      When("^I try to describe a non-existent environment named \"([^\"]*)\"$", (final String environmentNamePrefix) -> {
          dataServiceWrapper.describeEnvironment(describeEnvironmentRequest(environmentNamePrefix));
      });

      Then("^there should be a ResourceNotFoundException thrown$", () -> {
          // Write code here that turns the phrase above into concrete actions
          throw new NotImplementedException("");
      });

      Then("^the resourceType is \"([^\"]*)\"$", (String arg1) -> {
          // Write code here that turns the phrase above into concrete actions
          throw new NotImplementedException("");
      });

      Then("^the resourceId contains \"([^\"]*)\"$", (String arg1) -> {
          // Write code here that turns the phrase above into concrete actions
          throw new NotImplementedException("");
      });

      Given("^I delete the created environment named \"([^\"]*)\"$", (String arg1) -> {
          // Write code here that turns the phrase above into concrete actions
          throw new NotImplementedException("");
      });

      When("^I try to describe the environment named \"([^\"]*)\"$", (String arg1) -> {
          // Write code here that turns the phrase above into concrete actions
          throw new NotImplementedException("");
      });
  }

  private DescribeEnvironmentRequest describeEnvironmentRequest(final String environmentNamePrefix) {
      final String environmentName = environmentNamePrefix + this.postfix;
      final String clusterName = CLUSTER_NAME_PREFIX + this.postfix;
      return describeEnvironmentRequest(environmentName, clusterName);
  }

  private DescribeEnvironmentRequest describeEnvironmentRequest(final String environmentName, final String cluster) {
      return DescribeEnvironmentRequest.builder()
              .environmentId(
                          EnvironmentId.builder()
                          .accountId(ACCOUNT_ID)
                          .cluster(cluster)
                          .environmentName(environmentName)
                          .build())
              .build();
  }

  private CreateEnvironmentRequest createEnvironmentRequest(final String environmentNamePrefix) {
      final String environmentName = environmentNamePrefix + this.postfix;
      final String clusterName = CLUSTER_NAME_PREFIX + this.postfix;
      return createEnvironmentRequest(environmentName, clusterName);
  }

  private CreateEnvironmentRequest createEnvironmentRequest(
          final String environmentName, final String cluster) {
      return CreateEnvironmentRequest.builder()
                .environmentId(
                        EnvironmentId.builder()
                                .accountId(ACCOUNT_ID)
                                .cluster(cluster)
                                .environmentName(environmentName)
                                .build())
                .role(ROLE_ARN)
                .taskDefinition(TASK_DEFINITION_ARN)
                .environmentType(EnvironmentType.Daemon)
                .build();
  }

    private UpdateEnvironmentRequest updateEnvironmentRequest(final String environmentNamePrefix) {
        final String environmentName = environmentNamePrefix + this.postfix;
        final String clusterName = CLUSTER_NAME_PREFIX + this.postfix;
        return updateEnvironmentRequest(environmentName, clusterName);
    }

  private UpdateEnvironmentRequest updateEnvironmentRequest(
          final String environmentName, final String cluster) {
      return UpdateEnvironmentRequest.builder()
              .environmentId(
                      EnvironmentId.builder()
                              .accountId(ACCOUNT_ID)
                              .cluster(cluster)
                              .environmentName(environmentName)
                              .build())
              .role(ROLE_ARN)
              .taskDefinition(TASK_DEFINITION_ARN)
              .instanceGroup(
                      InstanceGroup.builder()
                      .attributes(null)
                      .build()
              )
              .build();
  }

  // TODO: Currently just check if the environment are equal to the other. May only need to just compare the equality of some fields but not all
  private void checkEnvironmentEquality(final Environment thisEnvironment, final Environment otherEnvironment) {
      assertTrue(thisEnvironment.equals(otherEnvironment));
  }

  private String generatePostfix() {
      return UUID.randomUUID().toString();
  }

}
