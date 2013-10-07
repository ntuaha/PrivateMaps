package com.aha.privatemaps.services;

import android.os.Handler;
import android.os.Message;


public class MessageHandler extends Handler{
	public static final int MSG_REGISTER_CLIENT = 1;
	public static final int MSG_UNREGISTER_CLIENT = 2;
	public static final int MSG_SET_STATUS = 3;
	public static final int MSG_START_RECORD = 4;
	public static final int MSG_END_RECORD = 5;
	public static final int MSG_RECORD_STATUS = 6;
	public static final int MSG_DISPLAY = 7;
	public static final int MSG_PAUSE = 8;
	public static final int MSG_RESUME = 9;
	public static final int MSG_SET_TITLE=10;
	public static final int MSG_SET_REDRAWMAP = 11;
	protected int UI_state;
	public void display(Message msg){};
	public void end_record(Message msg){};
	public void start_record(Message msg){};
	public void register(Message msg){};
	public void unregister(Message msg){};
	public void record_status(Message msg){};
	public void set_status(Message msg){};
	public void set_title(Message msg){};
	public void redraw_map(Message msg){};
	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case MessageHandler.MSG_SET_REDRAWMAP:
			redraw_map(msg);
			break;
		case MessageHandler.MSG_DISPLAY:
			display(msg);
			break;
		case MessageHandler.MSG_END_RECORD:
			end_record(msg);
			break;			
		case MessageHandler.MSG_START_RECORD:
			start_record(msg);
			break;
		case MessageHandler.MSG_REGISTER_CLIENT:
			register(msg);
			break;
		case MessageHandler.MSG_UNREGISTER_CLIENT:
			unregister(msg);
			break;
		case MessageHandler.MSG_RECORD_STATUS:
			record_status(msg);
			break;
		case MessageHandler.MSG_SET_STATUS:
			set_status(msg);
			break;
		case MessageHandler.MSG_RESUME:
			UI_state = MSG_RESUME;
			break;
		case MessageHandler.MSG_PAUSE:
			UI_state = MSG_PAUSE;
			break;
		case MessageHandler.MSG_SET_TITLE:
			set_title(msg);
			break;
		default:
			super.handleMessage(msg);
		}
	}
}
