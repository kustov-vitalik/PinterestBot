package com.age.pinterest.bot;

import java.util.ArrayList;
import java.util.List;

import com.age.param.Follow;
import com.age.param.Repin;
import com.age.pinterest.task.FollowTask;
import com.age.pinterest.task.RepinTask;
import com.age.pinterest.task.Task;

public class Scheduler {
	List<Thread> tasks;

	public Scheduler() {
		this.tasks = new ArrayList<Thread>();
	}

	public void scheduleRepin(Repin repin) {
		this.startNewTask(new RepinTask(repin));
	}
	public void scheduleFollow(Follow follow)
	{
		this.startNewTask(new FollowTask(follow));
	}
	

	private void startNewTask(Task task) {
		Thread thread = new Thread(task);
		thread.setName(task.getType().toString());
		thread.start();
		tasks.add(thread);
	}
}
