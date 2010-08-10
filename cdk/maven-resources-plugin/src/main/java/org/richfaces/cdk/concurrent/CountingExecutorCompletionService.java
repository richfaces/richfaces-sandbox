/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.cdk.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Nick Belaevski
 * 
 */
public class CountingExecutorCompletionService<T> extends ExecutorCompletionService<T> {

    private AtomicInteger tasksCounter = new AtomicInteger(0);

    public CountingExecutorCompletionService(Executor executor) {
        super(executor);
    }
    
    public CountingExecutorCompletionService(Executor executor, BlockingQueue<Future<T>> completionQueue) {
        super(executor, completionQueue);
    }

    @Override
    public Future<T> submit(Callable<T> task) {
        tasksCounter.getAndIncrement();
        return super.submit(task);
    }
    
    public Future<T> submit(Runnable task, T result) {
        tasksCounter.getAndIncrement();
        return super.submit(task, result);
    }
    
    @Override
    public Future<T> take() throws InterruptedException {
        if (tasksCounter.get() == 0) {
            return null;
        }

        try {
            return super.take();
        } finally {
            tasksCounter.getAndDecrement();
        }
    }
}
