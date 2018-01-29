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
package com.amazonaws.blox.dataservice.test.data;

import com.amazonaws.blox.dataservice.repository.model.EnvironmentDDBRecord;
import com.amazonaws.blox.dataservice.repository.model.EnvironmentRevisionDDBRecord;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;

public class DynamoDBLocalSetup {

  public void createTables(
      final DynamoDBMapper dynamoDBMapper, final AmazonDynamoDB amazonDynamoDB) {
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

  public void deleteTables(
      final DynamoDBMapper dynamoDBMapper, final AmazonDynamoDB amazonDynamoDB) {
    DeleteTableRequest deleteEnvironmentTableRequest =
        dynamoDBMapper.generateDeleteTableRequest(EnvironmentDDBRecord.class);
    amazonDynamoDB.deleteTable(deleteEnvironmentTableRequest);
    DeleteTableRequest deleteEnvironmentRevisionTableRequest =
        dynamoDBMapper.generateDeleteTableRequest(EnvironmentRevisionDDBRecord.class);
    amazonDynamoDB.deleteTable(deleteEnvironmentRevisionTableRequest);
  }
}
