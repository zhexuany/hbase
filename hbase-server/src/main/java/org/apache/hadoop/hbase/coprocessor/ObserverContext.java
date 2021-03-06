/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hbase.coprocessor;

import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.HBaseInterfaceAudience;
import org.apache.hadoop.hbase.security.User;
import org.apache.yetus.audience.InterfaceAudience;
import org.apache.yetus.audience.InterfaceStability;

import java.util.Optional;

/**
 * Carries the execution state for a given invocation of an Observer coprocessor
 * ({@link RegionObserver}, {@link MasterObserver}, or {@link WALObserver})
 * method.  The same ObserverContext instance is passed sequentially to all loaded
 * coprocessors for a given Observer method trigger, with the
 * <code>CoprocessorEnvironment</code> reference swapped out for each
 * coprocessor.
 * @param <E> The {@link CoprocessorEnvironment} subclass applicable to the
 *     revelant Observer interface.
 */
@InterfaceAudience.LimitedPrivate(HBaseInterfaceAudience.COPROC)
@InterfaceStability.Evolving
public interface ObserverContext<E extends CoprocessorEnvironment> {
  E getEnvironment();

  /**
   * Call to indicate that the current coprocessor's return value should be
   * used in place of the normal HBase obtained value.
   */
  void bypass();

  /**
   * Call to indicate that additional coprocessors further down the execution
   * chain do not need to be invoked.  Implies that this coprocessor's response
   * is definitive.
   */
  void complete();

  /**
   * Returns the active user for the coprocessor call. If an explicit {@code User} instance was
   * provided to the constructor, that will be returned, otherwise if we are in the context of an
   * RPC call, the remote user is used. May not be present if the execution is outside of an RPC
   * context.
   */
  Optional<User> getCaller();

}
