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

import com.amazonaws.blox.scheduling.scheduler.engine.SchedulingAction;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReplaceAfterTerminateScheduler extends DaemonScheduler {
  public static final String ID = "ReplaceAfterTerminate";

  public List<SchedulingAction> schedule(DaemonEnvironment env, ClusterSummary summary) {

    List<SchedulingAction> stopTaskActions = new ArrayList<>();
    summary
        .getInstances()
        .stream()
        .filter(i -> env.hasUnmatchingTask(summary.tasksForInstance(i)))
        .forEach(i -> stopTaskActions.addAll(env.stopTaskFor(summary.tasksForInstance(i))));

    List<SchedulingAction> startTaskActions =
        summary
            .getInstances()
            .stream()
            .filter(i -> env.hasMatchingTask(summary.tasksForInstance(i)))
            .map(i -> env.startTaskFor(i))
            .collect(Collectors.toList());
    return (stopTaskActions.size() == 0) ? startTaskActions : stopTaskActions;
  }
}
