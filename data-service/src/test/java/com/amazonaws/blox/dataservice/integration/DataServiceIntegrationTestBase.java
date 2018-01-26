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

import com.amazonaws.blox.dataservice.api.CreateEnvironmentApi;
import com.amazonaws.blox.dataservice.api.DataServiceApi;
import com.amazonaws.blox.dataservice.api.DeleteEnvironmentApi;
import com.amazonaws.blox.dataservice.api.DescribeEnvironmentApi;
import com.amazonaws.blox.dataservice.api.DescribeEnvironmentRevisionApi;
import com.amazonaws.blox.dataservice.api.ListClustersApi;
import com.amazonaws.blox.dataservice.api.ListEnvironmentsApi;
import com.amazonaws.blox.dataservice.api.StartDeploymentApi;
import com.amazonaws.blox.dataservice.mapper.ApiModelMapper;
import com.amazonaws.blox.dataservice.mapper.EnvironmentMapper;
import com.amazonaws.blox.dataservice.repository.EnvironmentRepository;
import com.amazonaws.blox.dataservice.repository.EnvironmentRepositoryDDB;
import com.amazonaws.blox.dataservice.repository.model.EnvironmentDDBRecord;
import com.amazonaws.blox.dataservice.repository.model.EnvironmentRevisionDDBRecord;
import com.amazonaws.blox.dataservicemodel.v1.client.DataService;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.local.shared.access.AmazonDynamoDBLocal;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(classes = DataServiceIntegrationTestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class DataServiceIntegrationTestBase {
  @Autowired private ApiModelMapper apiModelMapper;
  @Autowired private EnvironmentMapper environmentMapper;
  private AmazonDynamoDBLocal amazonDynamoDBLocal;
  protected DataService dataService;

  @Before
  public void setup() {
    amazonDynamoDBLocal = DynamoDBEmbedded.create();
    AmazonDynamoDB amazonDynamoDB = amazonDynamoDBLocal.amazonDynamoDB();
    DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
    EnvironmentRepository repo = new EnvironmentRepositoryDDB(dynamoDBMapper, environmentMapper);
    dataService =
        new DataServiceApi(
            new CreateEnvironmentApi(apiModelMapper, repo),
            new DescribeEnvironmentApi(apiModelMapper, repo),
            new StartDeploymentApi(apiModelMapper, repo),
            new ListEnvironmentsApi(apiModelMapper, repo),
            new DescribeEnvironmentRevisionApi(apiModelMapper, repo),
            new ListClustersApi(apiModelMapper, repo),
            new DeleteEnvironmentApi(apiModelMapper, repo));

    ProvisionedThroughput throughput =
        new ProvisionedThroughput().withReadCapacityUnits(1000L).withWriteCapacityUnits(1000L);

    CreateTableRequest createEnvironments =
        dynamoDBMapper
            .generateCreateTableRequest(EnvironmentDDBRecord.class)
            .withProvisionedThroughput(throughput);
    createEnvironments
        .getGlobalSecondaryIndexes()
        .forEach(index -> index.withProvisionedThroughput(throughput));

    CreateTableRequest createEnvironmentRevisions =
        dynamoDBMapper
            .generateCreateTableRequest(EnvironmentRevisionDDBRecord.class)
            .withProvisionedThroughput(throughput);

    amazonDynamoDB.createTable(createEnvironments);
    amazonDynamoDB.createTable(createEnvironmentRevisions);
  }

  @After
  public void tearDown() {
    amazonDynamoDBLocal.shutdown();
  }
}
