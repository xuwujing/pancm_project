/*
 *  Copyright 2021 Sonu Kumar
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package com.zans.demo;

import com.github.sonus21.rqueue.annotation.RqueueListener;
import com.github.sonus21.rqueue.utils.TimeoutUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

//
@Component
@Slf4j
public class MessageListener {

  private static final Random random = new Random();

  @Value("${job.fail.percentage:0}")
  private int percentageFailure;

  @Value("${job.execution.interval:100}")
  private int jobExecutionTime;

  protected boolean shouldFail() {
    if (percentageFailure == 0) {
      return false;
    }
    if (percentageFailure >= 100) {
      return true;
    }
    return random.nextInt(100) < percentageFailure;
  }

  protected void execute(String msg, Object any, boolean failingEnabled) {
    log.info(msg, any);
    TimeoutUtils.sleep(random.nextInt(jobExecutionTime));
    if (failingEnabled && shouldFail()) {
      throw new IllegalArgumentException("Failing On Purpose " + any);
    }
  }

  @RqueueListener(value = "${rqueue.simple.queue}")
  public void onSimpleMessage(String message) {
    execute("simple: {}", message, false);
  }

  @RqueueListener(
      value = {"${rqueue.delay.queue}", "${rqueue.delay2.queue}"},
      numRetries = "${rqueue.delay.queue.retries}",
      visibilityTimeout = "60*60*1000")
  public void onMessage(String message) {
    execute("delay: {}", message, true);
  }

  @RqueueListener(
      value = "job-queue",
      deadLetterQueue = "job-morgue",
      numRetries = "2",
      deadLetterQueueListenerEnabled = "false",
      concurrency = "10-20")
  public void onJobMessage(Job job) {
    execute("job-queue: {}", job, true);
  }

  @RqueueListener(value = "job-morgue", numRetries = "1", concurrency = "1-3")
  public void onJobDlqMessage(Job job) {
    execute("job-morgue: {}", job, true);
  }
}
