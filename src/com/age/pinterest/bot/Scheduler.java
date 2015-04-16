package com.age.pinterest.bot;

import java.util.ArrayList;
import java.util.List;

import com.age.param.AddAccountParam;
import com.age.param.FollowParam;
import com.age.param.GeneratePinsParam;
import com.age.param.PinParam;
import com.age.param.RefreshParam;
import com.age.param.RepinParam;
import com.age.param.ScrapeParam;
import com.age.param.UnfollowParam;
import com.age.pinterest.task.AddAccountTask;
import com.age.pinterest.task.FollowTask;
import com.age.pinterest.task.GenerateBasicPinsTask;
import com.age.pinterest.task.PinTask;
import com.age.pinterest.task.RefreshUserTask;
import com.age.pinterest.task.RepinTask;
import com.age.pinterest.task.ScrapeTask;
import com.age.pinterest.task.Task;
import com.age.pinterest.task.TaskType;
import com.age.pinterest.task.UnFollowTask;

public class Scheduler {
	List<Thread> tasks;

	public Scheduler() {
		this.tasks = new ArrayList<Thread>();
	}

	public void schedule(RepinParam repin) {
		this.startNewTask(new RepinTask(repin));
	}

	public void schedule(FollowParam follow) {
		this.startNewTask(new FollowTask(follow));
	}

	public void schedule(UnfollowParam unfollow) {
		this.startNewTask(new UnFollowTask(unfollow));
	}

	public void schedule(PinParam pin) {
		this.startNewTask(new PinTask(pin));
	}

	public void schedule(RefreshParam refresh) {
		this.startNewTask(new RefreshUserTask(refresh));
	}

	public void schedule(ScrapeParam scrape) {
		this.startNewTask(new ScrapeTask(scrape));
	}

	public void schedule(AddAccountParam add) {
		this.startNewTask(new AddAccountTask(add));
	}

	public void schedule(GeneratePinsParam gen) {
		this.startNewTask(new GenerateBasicPinsTask(gen));
	}

	@SuppressWarnings("deprecation")
	public void terminateTask(TaskType type) {
		Thread task = checkForTask(type);
		if (task != null) {
			System.out.println("Terminating " + type.toString());
			task.stop();
			tasks.remove(task);
		}
	}

	public Thread checkForTask(TaskType type) {
		for (Thread task : tasks) {
			if (task.getName().equals(type.toString())) {
				return task;
			}
		}
		return null;
	}

	private void startNewTask(Task task) {
		Thread thread = new Thread(task);
		thread.setName(task.getType().toString());
		thread.start();
		tasks.add(thread);
	}
}
