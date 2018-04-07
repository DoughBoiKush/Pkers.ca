package com.osps.client.draw;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class RendererLock {

	static void completed() {
	      int num = RendererLock.post_cntr.incrementAndGet();
	      if(RendererLock.started && !RendererLock.finished && num == RendererLock.pre_cntr.get()) {
	         synchronized(RendererLock.lock) {
	            RendererLock.finished = true;
	            RendererLock.lock.notify();
	         }
	      }
	   }

	public static void prepare() {
	         RendererLock.pre_cntr.set(0);
	         RendererLock.post_cntr.set(0);
	         RendererLock.started = false;
	         RendererLock.finished = false;
	   }

	public static synchronized void startSoftware() {
	      RendererLock.started = true;
	   }

	public static void endSoftware() {
	    if(RendererLock.active) {
	       synchronized(RendererLock.lock) {
	          if(!RendererLock.finished && (!RendererLock.started || RendererLock.pre_cntr.get() != RendererLock.post_cntr.get())) {
	             try {
	                RendererLock.lock.wait(500L);
	             } catch (InterruptedException e) {
	                e.printStackTrace();
	             }
	          }
	       }
	    }
	 }

	public static void kill() {
		if(software_pool != null) {
            software_pool.shutdownNow();
            software_pool = null;
            System.gc();
         }
		
	}

	private static Object lock = new Object();
	static boolean active = false;
	public static final boolean MULTI = false;
	static ExecutorService software_pool = null;
	static AtomicInteger post_cntr = new AtomicInteger(0);
	static AtomicInteger pre_cntr = new AtomicInteger(0);
	static boolean finished = false;
	static boolean started = false;
	
	public static void init() {
		if(RendererLock.MULTI) {
			if(RendererLock.software_pool == null && RendererLock.software_pool == null) {
				int coresAvailable = Runtime.getRuntime().availableProcessors();
				if (coresAvailable > 3)
					coresAvailable = 3;
				RendererLock.software_pool = Executors.newFixedThreadPool(coresAvailable);
				System.out.println("cores" + coresAvailable);
			}
			RendererLock.active = true;
		} 
		
	}

	public static void submit(Runnable runnable) {
		RendererLock.pre_cntr.incrementAndGet();
		RendererLock.software_pool.submit(runnable);
	}

}
