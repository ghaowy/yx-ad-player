package com.imprexion.adplayer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Author: Xianquan Feng
 * Email: xianquan.feng@imprexion.cn
 * Date: 2019/4/21
 * Desc:
 */
public class TimerStub {

    private Timer timer;
    private boolean isScheduling = false;

    public TimerStub() {

    }

    /**
     * 一次性延迟计时。
     *
     * @param onTimeListener
     * @param delay
     */
    public void postDelayed(final OnTimeListener onTimeListener, final long delay) {
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new MyTimeTask() {
            @Override
            public void run() {
                if (onTimeListener != null) {
                    onTimeListener.onTime();
                    cancel();
                }
            }
        }, delay);
        isScheduling = true;
    }

    /**
     * 周期性定时器
     *
     * @param onTimeListener
     * @param delay
     * @param period
     */
    public void post(final OnTimeListener onTimeListener, final long delay, long period) {
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new MyTimeTask() {
            @Override
            public void run() {
                if (onTimeListener != null) {
                    onTimeListener.onPeriod();
                    this.periodCount++;
                }
            }
        }, delay, period);
        isScheduling = true;
    }

    /**
     * 周期性定时器，可设置定时次数。
     *
     * @param onTimeListener
     * @param delay
     * @param period
     * @param pCount         如果为大于0的参数，则有效。否则无效。等同于无限定时
     */
    public void post(final OnTimeListener onTimeListener, final long delay, long period, final int pCount) {
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new MyTimeTask() {
            @Override
            public void run() {
                this.periodCount++;
                if (onTimeListener != null) {
                    onTimeListener.onPeriod();
                }
                if (this.periodCount == pCount) {
                    if (onTimeListener != null) {
                        onTimeListener.onTime();
                    }
                    cancel();
                }
            }
        }, delay, period);
        isScheduling = true;
    }

    public boolean isScheduling() {
        return this.isScheduling;
    }

    public void cancel() {
        if (timer != null) {
            timer.cancel();
            isScheduling = false;
            timer = null;
        }
    }

    abstract class MyTimeTask extends TimerTask {
        int periodCount;
    }

    public interface OnTimeListener {
        void onTime();

        void onPeriod();
    }
}
