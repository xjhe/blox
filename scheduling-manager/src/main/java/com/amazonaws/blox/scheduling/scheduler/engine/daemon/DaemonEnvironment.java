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
package com.amazonaws.blox.scheduling.scheduler.engine.daemon;

import com.amazonaws.blox.scheduling.scheduler.engine.EnvironmentDescription;
import com.amazonaws.blox.scheduling.scheduler.engine.StartTask;
import com.amazonaws.blox.scheduling.scheduler.engine.StopTask;
import com.amazonaws.blox.scheduling.state.ClusterSnapshot.ContainerInstance;
import com.amazonaws.blox.scheduling.state.ClusterSnapshot.Task;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

/**
 * Wrapper around {@link EnvironmentDescription} that makes it easy to match {@link
 * com.amazonaws.blox.scheduling.state.ClusterSnapshot.Task} instances to a Daemon environment.
 */
@RequiredArgsConstructor
public class DaemonEnvironment {
  private static final Set<String> HEALTHY_STATES =
      new HashSet<>(Arrays.asList("RUNNING", "PENDING"));

  private final EnvironmentDescription environment;

  public boolean hasMatchingTask(List<Task> tasks) {
    return tasks.stream().noneMatch(this::matchesTask);
  }

  public boolean hasUnmatchingTask(List<Task> tasks) {
    return tasks.stream().anyMatch(this::unmatchesTask);
  }

  public StartTask startTaskFor(ContainerInstance i) {
    return StartTask.builder()
        .clusterName(environment.getClusterName())
        .containerInstanceArn(i.getArn())
        .taskDefinitionArn(environment.getTaskDefinitionArn())
        .group(environment.getEnvironmentName())
        .build();
  }

  public List<StopTask> stopTaskFor(final List<Task> tasks) {
    final List<String> tasksToStop =
        tasks
            .stream()
            .filter(this::unmatchesTask)
            .map(Task::getTaskDefinitionArn)
            .collect(Collectors.toList());
    return tasksToStop
        .stream()
        .map(
            t ->
                StopTask.builder()
                    .clusterName(environment.getClusterName())
                    .task(t)
                    .reason("replaceAfterTerminate")
                    .build())
        .collect(Collectors.toList());
  }

  public boolean unmatchesTask(Task t) {
    return !t.getGroup().equals(environment.getEnvironmentName())
        || !t.getTaskDefinitionArn().equals(environment.getTaskDefinitionArn());
  }

  public boolean matchesTask(Task t) {
    return t.getGroup().equals(environment.getEnvironmentName())
        && t.getTaskDefinitionArn().equals(environment.getTaskDefinitionArn())
        && HEALTHY_STATES.contains(t.getStatus());
  }
}
