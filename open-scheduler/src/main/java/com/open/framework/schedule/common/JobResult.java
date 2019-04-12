package com.open.framework.schedule.common;

import java.io.Serializable;

/**
 * 任务运行结果
 *
 * @date :2018-07-18
 */
public class JobResult<T extends Serializable> implements Serializable {

	private final State state;

	private T data;

	public JobResult() {
		this.state = State.unknow;
	}

	public JobResult(T data) {
		this.state = State.ok;
		this.data = data;
	}

	public JobResult(State state) {
		this.state = state;
	}

	public JobResult(State state, T data) {
		this.state = state;
		this.data = data;
	}

	public State getState() {
		return state;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "{" +
				"\"state\":\"" + state +
				"\", \"data\":\"" + data +
				"\"}";
	}

	/**
	 * 任务运行结果状态
	 *
	 * @author :<a href="mailto:yu_hu@neusoft.com">huyu</a>
	 * @date :2018-07-18
	 */
	public enum State {
		//
		unknow(0, "未知结果"),

		ok(1, "正常"),

		failed(-1, "失败"),

		exception(-2, "异常");

		private int code;

		private String desc;

		State(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public int getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}
}
