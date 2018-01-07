package com.gj.jc.collection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class BlockingQueueTest {
    private LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
    private ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(16);
    private PriorityBlockingQueue priorityBlockingQueue = new PriorityBlockingQueue();
    private SynchronousQueue synchronousQueue = new SynchronousQueue();


}
